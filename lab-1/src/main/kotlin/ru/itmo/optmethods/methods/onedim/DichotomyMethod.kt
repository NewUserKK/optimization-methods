package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.common.InvocationsCountingFunction
import ru.itmo.optmethods.common.OneDimFunction
import ru.itmo.optmethods.methods.DEFAULT_EPS
import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import ru.itmo.optmethods.common.avg
import kotlin.math.abs
import kotlin.random.Random

class DichotomyMethod(private val eps: Rational = DEFAULT_EPS) : MinimizationMethod {
    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        val mFunction = InvocationsCountingFunction(function)

        var iterations = 0
        var start = rangeStart
        var end = rangeEnd

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
        }

        return MinimizationResult(
            argument = listOf(end),
            result = mFunction(end),
            iterations = iterations,
            functionsCall = mFunction.invocationsCount
        )
    }
}
