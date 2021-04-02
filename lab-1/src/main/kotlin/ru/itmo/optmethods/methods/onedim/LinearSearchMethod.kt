package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import ru.itmo.optmethods.common.OneDimFunction

private const val DEFAULT_DELTA = 1e-3

class LinearSearchMethod(private val delta: Rational = DEFAULT_DELTA): MinimizationMethod {
    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        var end = rangeStart + delta

        var iterations = 1

        var minArg = rangeStart
        var min = function(rangeStart)

        while (end <= rangeEnd) {
            iterations++
            end = rangeStart + delta * iterations

            val f = function(end)
            if (f < min) {
                min = f
                minArg = end
            }
        }

        return MinimizationResult(
            argument = listOf(minArg),
            result = min,
            iterations = iterations
        )
    }
}
