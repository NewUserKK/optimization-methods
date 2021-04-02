package ru.itmo.optmethods.methods.onedim.comparison

import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

private data class Segment(val start: Rational, val end: Rational) {
    val size = abs(end - start)
}

private data class ComparisonResult(
    val accuracy: Rational,
    val dichotomyResult: MinimizationResult,
    val dichotomySegments: List<Segment>,
    val goldenRatioResult: MinimizationResult,
    val goldenRatioSegments: List<Segment>
)

object OneDimMethodsComparison {
    // Desmos: x^{4\ }+3x^{3\ }-2x
    private val testFunction = OneDimFunction { x -> x.pow(4) + 3 * x.pow(3) - 2 * x }

    fun compare() {
        val accuracies = (2..8).map { power -> 10.0.pow(-power) }

        val results = mutableListOf<ComparisonResult>()

        accuracies.forEach { eps ->
            val start = -10.0
            val end = 10.0

            val dichotomySegments = mutableListOf<Segment>()
            val dichotomyMethod = DichotomyMethod(eps = eps).apply {
                setOnIterationEndListener { rangeStart, rangeEnd ->
                    dichotomySegments += Segment(rangeStart, rangeEnd)
                }
            }
            val dichotomyResult = dichotomyMethod.findMinimum(
                rangeStart = start,
                rangeEnd = end,
                function = testFunction
            )


            val goldenRatioSegments = mutableListOf<Segment>()
            val goldenRatioMethod = GoldenRatioMethod(eps = eps).apply {
                setOnIterationEndListener { rangeStart, rangeEnd ->
                    goldenRatioSegments += Segment(rangeStart, rangeEnd)
                }
            }
            val goldenRatioResult = goldenRatioMethod.findMinimum(
                rangeStart = start,
                rangeEnd = end,
                function = testFunction,
            )


            results += ComparisonResult(
                accuracy = eps,
                dichotomyResult = dichotomyResult,
                dichotomySegments = dichotomySegments,
                goldenRatioResult = goldenRatioResult,
                goldenRatioSegments = goldenRatioSegments
            )
        }

        buildPlots(results)
    }

    private fun buildPlots(results: List<ComparisonResult>) {
        val accuracies = results.map { -log10(it.accuracy) }

        plot(saveFigPath = "results/onedim/iterations.png") {
            title("Iterations count")
            xlabel("accuracy, negative pow of 10")
            ylabel("iterations")

            points {
                label("Dichotomy")
                add(accuracies, results.map { it.dichotomyResult.iterations })
            }

            points {
                label("Golden ratio")
                add(accuracies, results.map { it.goldenRatioResult.iterations })
            }
        }


        plot(saveFigPath = "results/onedim/calls.png") {
            title("Function calls")
            xlabel("accuracy, negative pow of 10")
            ylabel("function calls")

            points {
                label("Dichotomy")
                add(accuracies, results.map { it.dichotomyResult.functionsCall })
            }

            points {
                label("Golden ratio")
                add(accuracies, results.map { it.goldenRatioResult.functionsCall })
            }
        }

        plot(saveFigPath = "results/onedim/segments.png") {
            val dichotomySegments = results.last().dichotomySegments
            val goldenRatioSegments = results.last().goldenRatioSegments

            title("Segment lengths, accuracy=${results.last().accuracy}")
            xlabel("iterations")
            ylabel("length of segment")

            points {
                label("Dichotomy")
                add(
                    dichotomySegments.indices.toList(),
                    dichotomySegments.map { segment -> segment.size }
                )
            }

            points {
                label("Golden ratio")
                add(
                    goldenRatioSegments.indices.toList(),
                    goldenRatioSegments.map { segment -> segment.size }
                )
            }
        }
    }
}
