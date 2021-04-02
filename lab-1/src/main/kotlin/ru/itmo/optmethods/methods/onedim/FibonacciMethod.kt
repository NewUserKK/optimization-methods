package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.functions.InvocationsCountingFunction
import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.DEFAULT_MAX_ITERATIONS
import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import ru.itmo.optmethods.common.avg

class FibonacciMethod(
    private val maxIterations: Int = DEFAULT_MAX_ITERATIONS
) : MinimizationMethod {
    private val fibonacci = when {
        fibonacciCache.size > maxIterations -> fibonacciCache
        else -> constructFibonacciNumbers(maxIterations).also { fibonacciCache = it }
    }

    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        val mFunction = InvocationsCountingFunction(function)

        var start = rangeStart
        var end = rangeEnd

        val n = maxIterations - 1

        val kStart = { i: Int -> fibonacci[i - 2] / fibonacci[i] }
        val kEnd = { i: Int -> fibonacci[i - 1] / fibonacci[i] }

        var x1 = start + (end - start) * kStart(n)
        var x2 = start + (end - start) * kEnd(n)

        var f1 = mFunction(x1)
        var f2 = mFunction(x2)

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
        }

        return MinimizationResult(
            argument = listOf(avg(x1, x2)),
            result = mFunction(avg(x1, x2)),
            iterations = n + 1,
            functionsCall = mFunction.invocationsCount
        )
    }

    companion object {
        private var fibonacciCache = constructFibonacciNumbers(DEFAULT_MAX_ITERATIONS)

        private fun constructFibonacciNumbers(n: Int): DoubleArray {
            val array = DoubleArray(n)
            array[0] = 1.0
            array[1] = 1.0
            for (i in 2 until n) {
                array[i] = array[i - 1] + array[i - 2]
            }

            return array
        }
    }
}
