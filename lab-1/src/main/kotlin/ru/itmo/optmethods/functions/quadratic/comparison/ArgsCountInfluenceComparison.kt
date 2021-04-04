package ru.itmo.optmethods.functions.quadratic.comparison

import ru.itmo.optmethods.functions.quadratic.genQuadraticFunction
import ru.itmo.optmethods.functions.quadratic.getGradient
import ru.itmo.optmethods.methods.Rational
import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.gradient.GradientMethodException
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points
import java.util.*

object ArgsCountInfluenceComparison {

    private val rnd = Random()

    private val gm = GradientMethod(
        maxIterations = 1000,
        maxGradientLength = 1000.0
    )

    private val funPerArgsCount = 100
    private val maxArgsCount = 20
    private val conditionNumber = 1.1

    fun compare() {
        compareWithIterationCount()
    }

    private fun compareWithIterationCount() {
        val points = mutableListOf<Pair<Int, Rational>>()

        repeat(maxArgsCount - 1){
            val n = 2 + it
            val start = Array(n) { -1.0 }.toList()

            var checkedFunctions = 0
            var sumIterations = 0
            var failsCount = 0

            while (checkedFunctions < funPerArgsCount){
                val qf = rnd.genQuadraticFunction(n, conditionNumber, 1.0)
                val gradient = qf.getGradient()

                try {
                    val result = gm.findMinimum(n, start, qf, gradient, GoldenRatioMethod())
                    checkedFunctions++
                    sumIterations += result.iterations
                } catch (e: GradientMethodException){
                    failsCount++
                }
            }

            val avgIters = sumIterations.toDouble() / funPerArgsCount
            points += n to avgIters
        }

        plot(saveFigPath = "results/gradient/args-count/influence-to-iterations.png") {
            title("Influence to iterations")
            xlabel("Args count")
            ylabel("Avg iterations")

            points {
                add(
                    points.map { it.first },
                    points.map { it.second },
                    "o"
                )
            }
        }
    }
}
