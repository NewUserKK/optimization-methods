package onedim

import DEFAULT_EPS
import EpsilonCheckMinimizationMethod
import MinimizationResult
import Rational
import common.OneDimFunction
import common.avg
import kotlin.math.abs
import kotlin.random.Random

class DichotomyMethod(eps: Rational = DEFAULT_EPS) : EpsilonCheckMinimizationMethod(eps) {
    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        var iterations = 0
        var start = rangeStart
        var end = rangeEnd

        while (abs(end - start) > epsilon) {
            val delta = Random.nextDouble(0.0, (end - start) / 2)
            val mid = avg(start, end)

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
