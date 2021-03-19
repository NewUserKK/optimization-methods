package gradient

import DEFAULT_EPS
import MinimizationMethod
import MultiDimMinimizationResult
import NDimFunction
import Rational
import common.minus
import common.mul
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
        onStep: (MultiDimMinimizationResult) -> Unit
    ): MultiDimMinimizationResult {
        assert(start.size == n)

        var w = start
        var iters = 0

        while (true) {
            onStep.invoke(
                MultiDimMinimizationResult(
                    w,
                    function.invoke(w),
                    iters
                )
            )

            iters++

            val grad = gradient.invoke(w)
            val step = findStep(function, w, grad, stepFinder)
            val newW = w.minus(grad.mul(step))
            if (abs(function(newW) - function(w)) < epsilon) {
                return MultiDimMinimizationResult(
                    newW,
                    function.invoke(newW),
                    iters
                )
            }
            w = newW
        }
    }

    private fun findStep(
        function: (List<Rational>) -> Rational,
        grad: List<Rational>,
        newGrad: List<Rational>,
        stepFinder: MinimizationMethod
    ): Rational {
        return stepFinder.findMinimum(
            0.0,
            1.0
        ) { step -> function.invoke(grad.minus(newGrad.mul(step))) }.argument
    }
}
