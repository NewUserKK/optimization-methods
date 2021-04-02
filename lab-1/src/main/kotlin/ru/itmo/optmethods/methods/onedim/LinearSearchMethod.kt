package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.common.InvocationsCountingFunction
import ru.itmo.optmethods.common.OneDimFunction
import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational

private const val DEFAULT_DELTA = 1e-3

class LinearSearchMethod(private val delta: Rational = DEFAULT_DELTA): MinimizationMethod {
    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        val mFunction = InvocationsCountingFunction(function)

        var end = rangeStart + delta

        var iterations = 1

        var minArg = rangeStart
        var min = mFunction(rangeStart)

        while (end <= rangeEnd) {
            iterations++
            end = rangeStart + delta * iterations

            val f = mFunction(end)
            if (f < min) {
                min = f
                minArg = end
            }
        }

        return MinimizationResult(
            argument = listOf(minArg),
            result = min,
            iterations = iterations,
            functionsCall = mFunction.invocationsCount
        )
    }
}
