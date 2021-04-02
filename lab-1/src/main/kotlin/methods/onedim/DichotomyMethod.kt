package methods.onedim

import methods.DEFAULT_EPS
import methods.MinimizationMethod
import methods.MinimizationResult
import methods.Rational
import common.OneDimFunction
import common.avg
import kotlin.math.abs
import kotlin.random.Random

class DichotomyMethod(private val eps: Rational = DEFAULT_EPS) : MinimizationMethod {
    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        var iterations = 0
        var start = rangeStart
        var end = rangeEnd

        while (abs(end - start) > eps) {
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
            argument = listOf(end),
            result = function(end),
            iterations = iterations
        )
    }
}
