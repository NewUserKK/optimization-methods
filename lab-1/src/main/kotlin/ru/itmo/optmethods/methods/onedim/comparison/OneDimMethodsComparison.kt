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
    val dichotomyResults: List<MinimizationResult>,
    val goldenRatioResults: List<MinimizationResult>
)

object OneDimMethodsComparison {
    // Desmos: x^{4\ }+3x^{3\ }-2x
    private val testFunction = OneDimFunction { x -> x.pow(4) + 3 * x.pow(3) - 2 * x }

    fun compare() {
        val accuracies = (2..8).map { power -> 10.0.pow(-power) }
        val methodCount = 2

        val results = Array(methodCount) { mutableListOf<MinimizationResult>() }
        val segmentsByAccuracy = Array(accuracies.size) {
            Array(methodCount) { mutableListOf<Segment>() }
        }

        accuracies.forEachIndexed { accuracyIndex, eps ->
            val methods = listOf(
                DichotomyMethod(eps = eps),
                GoldenRatioMethod(eps = eps)
            )

            require(methodCount == methods.size)

            val segments = Array(methodCount) { mutableListOf<Segment>() }
            methods.forEachIndexed { i, method ->
                method.setOnIterationEndListener { rangeStart, rangeEnd ->
                    segments[i].add(Segment(rangeStart, rangeEnd))
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
                segmentsByAccuracy[accuracyIndex] = segments
            }
        }

        buildPlots(
            accuracies = accuracies.map { -log10(it) },
            dichotomySegments = segmentsByAccuracy[accuracies.lastIndex][0],
            dichotomyResults = results[0],
            goldenRatioSegments = segmentsByAccuracy[accuracies.lastIndex][1],
            goldenRatioResults = results[1]
        )
    }

    private fun buildPlots(
        accuracies: List<Double>,
        dichotomySegments: List<Segment>,
        dichotomyResults: List<MinimizationResult>,
        goldenRatioSegments: List<Segment>,
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

        plot(saveFigPath = "results/onedim/segments.png", show = true) {
            title("Segment lengths")
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
