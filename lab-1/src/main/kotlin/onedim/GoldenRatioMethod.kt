package onedim

import DEFAULT_EPS
import MinimizationMethod
import MinimizationResult
import Rational
import common.OneDimFunction
import common.avg
import kotlin.math.abs
import kotlin.math.sqrt

class GoldenRatioMethod(private val eps: Rational = DEFAULT_EPS) : MinimizationMethod {
    private val PHI = (sqrt(5.0) + 1) / 2
    private val K = 2 - PHI   // equals to 1 / (PHI + 1)

    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        var iterations = 1

        var start = rangeStart
        var end = rangeEnd

        var x1 = start + K * (end - start)
        var x2 = end - K * (end - start)

        var f1 = function(x1)
        var f2 = function(x2)

        while (abs(end - start) > eps) {
            when {
                f1 < f2 -> {
                    end = x2
                    x2 = x1
                    f2 = f1
                    x1 = start + K * (end - start)
                    f1 = function(x1)
                }
                else -> {
                    start = x1
                    x1 = x2
                    f1 = f2
                    x2 = end - K * (end - start)
                    f2 = function(x2)
                }
            }

            iterations++
        }

        return MinimizationResult(
            argument = listOf(avg(x1, x2)),
            result = function(avg(x1, x2)),
            iterations = iterations
        )
    }
}
