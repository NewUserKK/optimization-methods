package ru.itmo.optmethods.functions.quadratic.comparison

import ru.itmo.optmethods.comparison.MethodComparison
import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.functions.quadratic.genQuadraticFunction
import ru.itmo.optmethods.functions.quadratic.getGradient

import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.gradient.GradientMethodException
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points
import java.util.*

object ConditionNumberInfluenceComparison : MethodComparison {
    private val rnd = Random()

    private val gm = GradientMethod(
        maxIterations = 1000,
        maxGradientLength = 1000.0
    )

    private val funPerConditionNumber = 1000
    private val maxConditionNumber = 4
    private val conditionNumberStepsCount = 100

    override fun compare() {
        val n = 2
        val start = Array(n) { -10.0 }.toList()
        val pointsIters = mutableListOf<Pair<Rational, Rational>>()
        val pointsFails = mutableListOf<Pair<Rational, Rational>>()

        repeat(conditionNumberStepsCount) { step ->
            val k = 1.0 + (maxConditionNumber - 1.0) * step / conditionNumberStepsCount.toDouble()

            var checkedFunctions = 0
            var sumIterations = 0
            var failsCount = 0

            while (checkedFunctions < funPerConditionNumber) {
                val qf = rnd.genQuadraticFunction(n, k, 10.0)
                val gradient = qf.getGradient()

                try {
                    val result = gm.findMinimum(n, start, qf, gradient, GoldenRatioMethod())
                    checkedFunctions++
                    sumIterations += result.iterations
                } catch (e: GradientMethodException) {
                    failsCount++
                }
            }

            val avgIters = sumIterations.toDouble() / funPerConditionNumber
            pointsIters += k to avgIters

            val failedPercentage = failsCount.toDouble() / (funPerConditionNumber + failsCount)
            pointsFails += k to failedPercentage
        }

        plot(saveFigPath = "results/gradient/condition-number/influence-to-iterations.png") {
            title("Influence to iterations")
            xlabel("Condition number")
            ylabel("Avg iterations")

            points {
                add(
                    pointsIters.map { it.first },
                    pointsIters.map { it.second },
                    "o"
                )
            }
        }

        plot(saveFigPath = "results/gradient/condition-number/influence-to-fail-ratio.png") {
            title("Influence to fail ratio")
            xlabel("Condition number")
            ylabel("Fail ratio")

            points {
                add(
                    pointsFails.map { it.first },
                    pointsFails.map { it.second },
                    "o"
                )
            }
        }
    }
}
