package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.common.avg
import ru.itmo.optmethods.functions.InvocationsCountingFunction
import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.DEFAULT_EPS
import ru.itmo.optmethods.methods.MinimizationResult

import ru.itmo.optmethods.common.PHI
import ru.itmo.optmethods.common.Rational
import kotlin.math.abs

class GoldenRatioMethod(
    private val eps: Rational = DEFAULT_EPS
) : OneDimMinimizationMethod() {
    private val K = 2 - PHI   // equals to 1 / (PHI + 1)

    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        val mFunction = InvocationsCountingFunction(function)

        var iterations = 1

        var start = rangeStart
        var end = rangeEnd

        var x1 = start + K * (end - start)
        var x2 = end - K * (end - start)

        var f1 = mFunction(x1)
        var f2 = mFunction(x2)

        onIterationEnd(start, end)

        while (abs(end - start) > eps) {
            when {
                f1 < f2 -> {
                    end = x2
                    x2 = x1
                    f2 = f1
                    x1 = start + K * (end - start)
                    f1 = mFunction(x1)
                }
                else -> {
                    start = x1
                    x1 = x2
                    f1 = f2
                    x2 = end - K * (end - start)
                    f2 = mFunction(x2)
                }
            }

            onIterationEnd(start, end)
            iterations++
        }

        return MinimizationResult(
            argument = listOf(avg(x1, x2)),
            result = mFunction(avg(x1, x2)),
            iterations = iterations,
            functionsCall = mFunction.invocationsCount
        )
    }
}
