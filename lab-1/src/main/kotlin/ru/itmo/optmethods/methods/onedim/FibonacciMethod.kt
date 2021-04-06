package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.common.PHI
import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.common.avg
import ru.itmo.optmethods.functions.InvocationsCountingFunction
import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.DEFAULT_EPS
import ru.itmo.optmethods.methods.MinimizationResult

import kotlin.math.*

class FibonacciMethod(
    private val eps: Rational = DEFAULT_EPS
) : OneDimMinimizationMethod() {
    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        val mFunction = InvocationsCountingFunction(function)

        var start = rangeStart
        var end = rangeEnd

        val maxIterations = findMaxIterations(rangeStart, rangeEnd, eps)
        val n = maxIterations - 1

        val kStart = { i: Int -> nthFibonacci(i - 2) / nthFibonacci(i) }
        val kEnd = { i: Int -> nthFibonacci(i - 1) / nthFibonacci(i) }

        var x1 = start + (end - start) * kStart(n)
        var x2 = start + (end - start) * kEnd(n)

        var f1 = mFunction(x1)
        var f2 = mFunction(x2)

        onIterationEnd(start, end)

        for (i in (2 until n).reversed()) {
            when {
                f1 < f2 -> {
                    end = x2
                    x2 = x1
                    f2 = f1
                    x1 = start + kStart(i) * (end - start)
                    f1 = mFunction(x1)
                }
                else -> {
                    start = x1
                    x1 = x2
                    f1 = f2
                    x2 = start + kEnd(i) * (end - start)
                    f2 = mFunction(x2)
                }
            }

            onIterationEnd(start, end)
        }

        return MinimizationResult(
            argument = listOf(avg(x1, x2)),
            result = mFunction(avg(x1, x2)),
            iterations = n + 1,
            functionsCall = mFunction.invocationsCount
        )
    }

    private fun nthFibonacci(n: Int): Rational =
        round((PHI.pow(n) - (-PHI).pow(-n)) / sqrt(5.0))

    private fun findMaxIterations(rangeStart: Rational, rangeEnd: Rational, eps: Rational): Int {
        val rangeLength = abs(rangeEnd - rangeStart)
        val limit = rangeLength / eps

        var fibonacciN = 0
        while (nthFibonacci(fibonacciN) < limit) {
            fibonacciN++
        }

        return fibonacciN
    }
}
