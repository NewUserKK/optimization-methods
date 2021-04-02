package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.functions.InvocationsCountingFunction
import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational

private const val DEFAULT_DELTA = 1e-3

class LinearSearchMethod(
    private val delta: Rational = DEFAULT_DELTA
): OneDimMinimizationMethod() {
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

        onIterationEnd(rangeStart, end)

        while (end <= rangeEnd) {
            iterations++
            end = rangeStart + delta * iterations

            val f = mFunction(end)
            if (f < min) {
                min = f
                minArg = end
            }

            onIterationEnd(end, end)
        }

        return MinimizationResult(
            argument = listOf(minArg),
            result = min,
            iterations = iterations,
            functionsCall = mFunction.invocationsCount
        )
    }
}
