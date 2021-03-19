package onedim

import DEFAULT_MAX_ITERATIONS
import IterationsCheckMinimizationMethod
import MinimizationResult
import OneDimFunction
import Rational
import common.avg

class FibonacciMethod(
    maxIterations: Int = DEFAULT_MAX_ITERATIONS
) : IterationsCheckMinimizationMethod(maxIterations) {
    private val fibonacci = when {
        fibonacciCache.size > maxIterations -> fibonacciCache
        else -> constructFibonacciNumbers(maxIterations).also { fibonacciCache = it }
    }

    override fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult {
        var start = rangeStart
        var end = rangeEnd

        val n = maxIterations - 1

        val kStart = { i: Int -> fibonacci[i - 2] / fibonacci[i] }
        val kEnd = { i: Int -> fibonacci[i - 1] / fibonacci[i] }

        var x1 = start + (end - start) * kStart(n)
        var x2 = start + (end - start) * kEnd(n)

        var f1 = function(x1)
        var f2 = function(x2)

        for (i in (2 until maxIterations - 1).reversed()) {
            when {
                f1 < f2 -> {
                    end = x2
                    x2 = x1
                    f2 = f1
                    x1 = start + kStart(i) * (end - start)
                    f1 = function(x1)
                }
                else -> {
                    start = x1
                    x1 = x2
                    f1 = f2
                    x2 = start + kEnd(i) * (end - start)
                    f2 = function(x2)
                }
            }
        }

        return MinimizationResult(
            argument = avg(x1, x2),
            result = function(avg(x1, x2)),
            iterations = maxIterations - 1
        )
    }

    companion object {
        private var fibonacciCache = constructFibonacciNumbers(100)

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
