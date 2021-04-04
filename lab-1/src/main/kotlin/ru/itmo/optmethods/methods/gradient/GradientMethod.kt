package ru.itmo.optmethods.methods.gradient

import ru.itmo.optmethods.common.minus
import ru.itmo.optmethods.common.plus
import ru.itmo.optmethods.common.times
import ru.itmo.optmethods.common.vectorLength
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.InvocationsCountingFunction
import ru.itmo.optmethods.functions.NDimFunction
import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.DEFAULT_EPS
import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import kotlin.math.abs
import kotlin.math.sqrt


class GradientMethod(
    private val epsilon: Rational = DEFAULT_EPS,
    private val maxIterations: Int = Int.MAX_VALUE,
    private val maxGradientLength: Rational = Double.MAX_VALUE
) {
    fun findMinimum(
        n: Int,
        start: List<Rational>,
        function: NDimFunction,
        gradient: Gradient,
        stepFinder: MinimizationMethod,
        onStep: ((MinimizationResult) -> Unit)? = null
    ): MinimizationResult {
        require(start.size == n)

        val mFunction = InvocationsCountingFunction(function)

        var w = start
        var iters = 0

        while (true) {
            onStep?.invoke(
                MinimizationResult(
                    argument = w,
                    result = mFunction(w),
                    iterations = iters,
                    functionsCall = mFunction.invocationsCount
                )
            )

            iters++
            if (iters >= maxIterations) throw GradientMethodException("Iteration limit exceeded")

            val grad = gradient.invoke(w)
            if (!grad.vectorLength().isFinite()) throw GradientMethodException("Gradient value is not finite")
            if (grad.vectorLength() > maxGradientLength) throw GradientMethodException("Gradient value is too large");

            val step = findStep(mFunction, w, grad, stepFinder)
            val newW = w - grad * step

            val newValue = function(newW)
            if (!newValue.isFinite()) throw GradientMethodException("New function value is not finite")

            if (abs(newValue - function(w)) < epsilon) {
                return MinimizationResult(
                    argument = newW,
                    result = mFunction.invoke(newW),
                    iterations = iters,
                    functionsCall = mFunction.invocationsCount
                ).also { onStep?.invoke(it) }
            }

            w = newW
        }
    }

    private fun findStep(
        function: InvocationsCountingFunction,
        grad: List<Rational>,
        newGrad: List<Rational>,
        stepFinder: MinimizationMethod,
        useMinus: Boolean = true
    ): Rational {
        return stepFinder.findMinimum(
            rangeStart = 0.0,
            rangeEnd = 1.0,
            function = { step: Rational -> function(if (useMinus) grad - newGrad * step else grad + newGrad * step) }
        ).argument[0]
    }

    fun findMinimumAnti(
        n: Int,
        start: List<Rational>,
        function: NDimFunction,
        gradient: Gradient,
        stepFinder: MinimizationMethod,
        onStep: ((MinimizationResult) -> Unit)? = null,
        freq: Int
    ): MinimizationResult {
        require(start.size == n)

        val mFunction = InvocationsCountingFunction(function)

        var iters = 0
        var w = start
        while (true) {
            var curIter = 0
            var direction = gradient.invoke(w).map { -it }
            while (true) {
                onStep?.invoke(
                    MinimizationResult(
                        argument = w,
                        result = mFunction(w),
                        iterations = iters,
                        functionsCall = mFunction.invocationsCount
                    )
                )

                if (iters >= maxIterations) throw GradientMethodException("Iteration limit exceeded")

                val grad = gradient.invoke(w)
                if (grad.vectorLength() > maxGradientLength) throw GradientMethodException("Gradient value is too large")

                val step = findStep(mFunction, w, direction, stepFinder, false)
                val newW = w + direction * step
                w = newW
                val newGrad = gradient.invoke(newW)

                if (sqrt(newGrad.map { it * it }.sum()) < epsilon) {
                    return MinimizationResult(
                        argument = newW,
                        result = mFunction.invoke(newW),
                        iterations = iters,
                        functionsCall = mFunction.invocationsCount
                    ).also { onStep?.invoke(it) }
                }

                if (curIter + 1 == freq) {
                    break
                }

                val beta = newGrad.map { it * it }.sum() / grad.map { it * it }.sum()
                direction = newGrad.map { -it } + direction.map { it * beta }
                curIter++
                iters++
            }
        }
    }

    class ConstantStep(private val step: Rational) : MinimizationMethod {
        override fun findMinimum(
            rangeStart: Rational,
            rangeEnd: Rational,
            function: OneDimFunction
        ): MinimizationResult = MinimizationResult(
            argument = listOf(step),
            result = 0.0,
            iterations = 0,
            functionsCall = 0
        )
    }
}
