package dichotomy

import common.MinimizationMethod
import common.MinimizationResult
import common.OneDimFunction
import common.Rational
import kotlin.random.Random

class DichotomyMethod : MinimizationMethod {
    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        eps: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        var iterations = 0
        var start = rangeStart
        var end = rangeEnd

        while (end - start > eps) {
            val delta = Random.nextDouble(0.0, (end - start) / 2)
            val mid = (start + end) / 2

            val x1 = mid - delta
            val x2 = mid + delta

            val f1 = function(x1)
            val f2 = function(x2)

            when {
                f1 > f2 -> start = x1
                else -> end = x2
            }

            iterations++
        }

        return MinimizationResult(
            argument = end,
            result = function(end),
            iterations = iterations
        )
    }
}
