package gradient

import DEFAULT_EPS
import MinimizationMethod
import MinimizationResult
import Rational
import common.NDimFunction
import common.minus
import common.mult
import kotlin.math.abs

typealias Gradient = (List<Rational>) -> List<Rational>

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
            val newW = w.minus(grad.mult(step))
            if (abs(function(newW) - function(w)) < epsilon) {
                return MinimizationResult(
                    newW,
                    function.invoke(newW),
                    iters
                )
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
        ) { step -> function.invoke(grad.minus(newGrad.mult(step))) }.argument[0]
    }
}
