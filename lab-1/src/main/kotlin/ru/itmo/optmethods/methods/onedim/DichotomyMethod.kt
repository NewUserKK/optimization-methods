package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.common.avg
import ru.itmo.optmethods.functions.InvocationsCountingFunction
import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.DEFAULT_EPS
import ru.itmo.optmethods.methods.MinimizationResult

import kotlin.math.abs
import kotlin.random.Random

class DichotomyMethod(
    private val eps: Rational = DEFAULT_EPS
) : OneDimMinimizationMethod() {
    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        val mFunction = InvocationsCountingFunction(function)

        var iterations = 0
        var start = rangeStart
        var end = rangeEnd

        onIterationEnd(start, end)

        while (abs(end - start) > eps) {
            val delta = Random.nextDouble(0.0, (end - start) / 2)
            val mid = avg(start, end)

            val x1 = mid - delta
            val x2 = mid + delta

            val f1 = mFunction(x1)
            val f2 = mFunction(x2)

            when {
                f1 > f2 -> start = x1
                else -> end = x2
            }

            iterations++

            onIterationEnd(start, end)
        }

        return MinimizationResult(
            argument = listOf(end),
            result = mFunction(end),
            iterations = iterations,
            functionsCall = mFunction.invocationsCount
        )
    }
}
