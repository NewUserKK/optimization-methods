package ru.itmo.optmethods.methods.gradient

import ru.itmo.optmethods.common.*
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.InvocationsCountingFunction
import ru.itmo.optmethods.functions.NDimFunction
import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.DEFAULT_EPS
import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import kotlin.math.abs


class GradientMethod(private val epsilon: Rational = DEFAULT_EPS) {
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

            val grad = gradient.invoke(w)
            val step = findStep(mFunction, w, grad, stepFinder)
            val newW = w - grad * step

            if (abs(function(newW) - function(w)) < epsilon) {
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
        stepFinder: MinimizationMethod
    ): Rational {
        return stepFinder.findMinimum(
            rangeStart = 0.0, rangeEnd = 1.0
        ) { step: Rational -> function(grad - newGrad * step) }.argument[0]
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
