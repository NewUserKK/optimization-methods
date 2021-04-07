package ru.itmo.optmethods.methods.gradient.comparison

import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.comparison.MethodComparison
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.methods.onedim.FibonacciMethod
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod
import ru.itmo.optmethods.methods.onedim.LinearSearchMethod
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points
import kotlin.math.abs

object GradientStepsFinderComparison : MethodComparison {
    // x^2 + 2y^2 - xy + x + y + 3
    val func = TwoDimFunction { x, y -> x * x + 2 * y * y - x * y + x + y + 3 }
    val grad = Gradient { (x, y) ->
        listOf(
            2 * x - y + 1,
            4 * y - x + 1
        )
    }

    override fun compare() {
        val grad = GradientMethod()
        buildConvergencePlot(
            "results/gradient",
            "convergence",
            listOf("Dichotomy", "Golden Ratio", "Fibonacci", "Constant Step = 0.25", "linear"),
            listOf(
                runGradientWith(DichotomyMethod(), grad),
                runGradientWith(GoldenRatioMethod(), grad),
                runGradientWith(FibonacciMethod(), grad),
                runGradientWith(GradientMethod.ConstantStep(0.25), grad),
                runGradientWith(LinearSearchMethod(), grad)
            ),
            17.0 / 7
        )
    }

    private fun buildConvergencePlot(
        path: String,
        fileName: String,
        titles: List<String>,
        results: List<List<MinimizationResult>>,
        realRes: Rational
    ) {
        plot(saveFigPath = "$path/$fileName.png") {
            ylim(-3.0, 3.0)
            title("convergence")
            xlabel("steps")
            ylabel("diff")

            results.forEachIndexed { i, it ->
                points {
                    when (i) {
                        3 -> linestyle("--")
                        4 -> linestyle("-.")
                    }
                    label(titles[i])
                    add((1..it.size).toList(), it.map { abs(realRes - it.result) })
                }
            }
        }
    }

    private fun runGradientWith(
        stepFinder: MinimizationMethod,
        gradMethod: GradientMethod
    ): List<MinimizationResult> {
        val results = ArrayList<MinimizationResult>()
        gradMethod.findMinimum(
            n = 2,
            start = listOf(10.0, 10.0),
            function = func,
            gradient = grad,
            stepFinder = stepFinder,
            onStep = { minimizationResult -> results.add(minimizationResult) }
        )

        return results
    }
}
