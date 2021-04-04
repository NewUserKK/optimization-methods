package ru.itmo.optmethods.methods.onedim.comparison

import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.methods.onedim.FibonacciMethod
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod
import ru.itmo.optmethods.methods.onedim.OneDimMinimizationMethod
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

private data class Segment(val start: Rational, val end: Rational) {
    val size = abs(end - start)
}

private data class MethodComparisonResult(
    val calculationResult: MinimizationResult,
    val segments: List<Segment>
)

private data class ComparisonResult(
    val accuracy: Rational,
    val dichotomyResult: MethodComparisonResult,
    val goldenRatioResult: MethodComparisonResult,
    val fibonacciResult: MethodComparisonResult
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

            val dichotomyResult = computeMethodResults(
                DichotomyMethod(eps),
                start, end
            )

            val goldenRatioResult = computeMethodResults(
                GoldenRatioMethod(eps),
                start, end
            )

            val fibonacciResult = computeMethodResults(
                FibonacciMethod(eps),
                start, end
            )

            results += ComparisonResult(
                accuracy = eps,
                dichotomyResult = dichotomyResult,
                goldenRatioResult = goldenRatioResult,
                fibonacciResult = fibonacciResult
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
                add(accuracies, results.map { it.dichotomyResult.calculationResult.iterations })
            }

            points {
                label("Golden ratio")
                add(accuracies, results.map { it.goldenRatioResult.calculationResult.iterations })
            }

            points {
                label("Fibonacci")
                linestyle("--")
                add(accuracies, results.map { it.fibonacciResult.calculationResult.iterations })
            }
        }


        plot(saveFigPath = "results/onedim/calls.png") {
            title("Function calls")
            xlabel("accuracy, negative pow of 10")
            ylabel("function calls")

            points {
                label("Dichotomy")
                add(
                    accuracies,
                    results.map { it.dichotomyResult.calculationResult.functionsCall }
                )
            }

            points {
                label("Golden ratio")
                add(
                    accuracies,
                    results.map { it.goldenRatioResult.calculationResult.functionsCall }
                )
            }

            points {
                label("Fibonacci")
                linestyle("--")
                add(
                    accuracies,
                    results.map { it.fibonacciResult.calculationResult.functionsCall }
                )
            }
        }

        plot(saveFigPath = "results/onedim/segments.png") {
            val dichotomySegments = results.last().dichotomyResult.segments
            val goldenRatioSegments = results.last().goldenRatioResult.segments
            val fibonacciSegments = results.last().fibonacciResult.segments

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

            points {
                label("Fibonacci")
                linestyle("--")
                add(
                    fibonacciSegments.indices.toList(),
                    fibonacciSegments.map { segment -> segment.size }
                )
            }
        }
    }

    private fun computeMethodResults(
        minimizationMethod: OneDimMinimizationMethod,
        start: Rational,
        end: Rational
    ): MethodComparisonResult {
        val segments = mutableListOf<Segment>()
        val method = minimizationMethod.apply {
            setOnIterationEndListener { rangeStart, rangeEnd ->
                segments += Segment(rangeStart, rangeEnd)
            }
        }
        val result = method.findMinimum(
            rangeStart = start,
            rangeEnd = end,
            function = testFunction
        )

        return MethodComparisonResult(calculationResult = result, segments = segments)
    }
}
