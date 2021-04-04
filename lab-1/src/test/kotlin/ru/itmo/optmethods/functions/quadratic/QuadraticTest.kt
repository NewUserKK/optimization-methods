package ru.itmo.optmethods.functions.quadratic

import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.gradient.GradientMethodException
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod
import java.util.*

fun main() {
    val n = 2
    val start = Array(n) { -10.0 }.toList()
    val funPerK = 100
    val maxK = 4

    val rnd = Random()
    val gm = GradientMethod(
        maxIterations = 1000,
        maxGradientLength = 1000.0
    )

    repeat(100){
        val k = 1.0 + (maxK - 1.0) * it / 100.0

        var checkedFunctions = 0
        var sumIterations = 0
        var failsCount = 0

        while (checkedFunctions < funPerK){
            val qf = rnd.genQuadraticFunction(n, k, 10.0)
            val gradient = qf.getGradient()

            try {
                val result = gm.findMinimum(n, start, qf, gradient, GoldenRatioMethod())
                checkedFunctions++
                sumIterations += result.iterations
            } catch (e: GradientMethodException){
                failsCount++
//            println("ERROR: " + e.message)
            }
        }

        val avgIters = sumIterations.toDouble() / funPerK
        val failedPercentage = failsCount.toDouble() / (funPerK + failsCount)
        //println("$k, $avgIters")
        println("$k, $failedPercentage")
    }
}

