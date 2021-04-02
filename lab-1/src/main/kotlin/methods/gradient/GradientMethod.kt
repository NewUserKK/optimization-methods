package methods.gradient

import methods.DEFAULT_EPS
import methods.MinimizationMethod
import methods.MinimizationResult
import methods.Rational
import common.*
import kotlin.math.abs


class GradientMethod(
    val epsilon: Rational = DEFAULT_EPS
) {
    fun findMinimum(
        n: Int,
        start: List<Rational>,
        function: NDimFunction,
        gradient: Gradient,
        stepFinder: MinimizationMethod,
        onStep: ((MinimizationResult) -> Unit)? = null
    ): MinimizationResult {
        assert(start.size == n)

        var w = start
        var iters = 0

        while (true) {
            onStep?.invoke(
                MinimizationResult(
                    w,
                    function.invoke(w),
                    iters
                )
            )

            iters++

            val grad = gradient.invoke(w)
            val step = findStep(function, w, grad, stepFinder)
            val newW = w - grad * step
            if (abs(function(newW) - function(w)) < epsilon) {
                return MinimizationResult(
                    newW,
                    function.invoke(newW),
                    iters
                ).also {
                    onStep?.invoke(it)
                }
            }
            w = newW
        }
    }

    private fun findStep(
        function: NDimFunction,
        grad: List<Rational>,
        newGrad: List<Rational>,
        stepFinder: MinimizationMethod
    ): Rational {
        return stepFinder.findMinimum(
            0.0,
            1.0
        ) { step -> function.invoke(grad - newGrad * step) }.argument[0]
    }

    class ConstantStep(private val step: Rational): MinimizationMethod {
        override fun findMinimum(
            rangeStart: Rational,
            rangeEnd: Rational,
            function: OneDimFunction
        ): MinimizationResult {
            return MinimizationResult(listOf(step), 0.0, 0)
        }
    }
}
