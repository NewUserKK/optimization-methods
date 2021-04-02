package ru.itmo.optmethods.methods.onedim.comparison

import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points
import kotlin.math.log10
import kotlin.math.pow

private data class Segment(val start: Rational, val end: Rational)

object OneDimMethodsComparison {
    // Desmos: x^{4\ }+3x^{3\ }-2x
    private val testFunction = OneDimFunction { x -> x.pow(4) + 3 * x.pow(3) - 2 * x }

    fun compare() {
        val accuracies = (2..8).map { power -> 10.0.pow(-power) }
        println(accuracies)
        val methodCount = 2

        val results = Array(methodCount) { mutableListOf<MinimizationResult>() }

        accuracies.forEach { eps ->
            val methods = listOf(
                DichotomyMethod(eps = eps),
                GoldenRatioMethod(eps = eps)
            )

            require(methodCount == methods.size)

            val segments = methods.map { mutableListOf<Segment>() }
            methods.forEachIndexed { i, method ->
                method.setOnIterationEndListener { rangeStart, rangeEnd ->
                    segments[i] += Segment(rangeStart, rangeEnd)
                }
            }

            (0 until methodCount).forEach { i ->
                results[i].add(
                    methods[i].findMinimum(
                        rangeStart = -10.0,
                        rangeEnd = 10.0,
                        function = testFunction,
                    )
                )
            }
        }

        buildPlots(
            accuracies = accuracies.map { -log10(it) },
            dichotomyResults = results[0],
            goldenRatioResults = results[1]
        )
    }

    private fun buildPlots(
        accuracies: List<Double>,
        dichotomyResults: List<MinimizationResult>,
        goldenRatioResults: List<MinimizationResult>
    ) {
        plot(saveFigPath = "results/onedim/iterations.png") {
            title("Iterations count")
            xlabel("accuracy, negative pow of 10")
            ylabel("iterations")

            points {
                label("Dichotomy")
                add(accuracies, dichotomyResults.map { it.iterations })
            }

            points {
                label("Golden ratio")
                add(accuracies, goldenRatioResults.map { it.iterations })
            }
        }


        plot(saveFigPath = "results/onedim/calls.png") {
            title("Function calls")
            xlabel("accuracy, negative pow of 10")
            ylabel("function calls")

            points {
                label("Dichotomy")
                add(accuracies, dichotomyResults.map { it.functionsCall })
            }

            points {
                label("Golden ratio")
                add(accuracies, goldenRatioResults.map { it.functionsCall })
            }
        }
    }
}
