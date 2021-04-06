package ru.itmo.optmethods.methods.gradient.comparison

import ru.itmo.optmethods.comparison.MethodComparison
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points

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
        val grad = GradientMethod(1e-3)
        buildPlots(
            runGradientWith(DichotomyMethod(), grad),
            runGradientWith(GoldenRatioMethod(), grad),
            runGradientWith(GradientMethod.ConstantStep(0.25), grad)
        )
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

    private fun buildPlots(
        dichotomyResults: List<MinimizationResult>,
        goldenRatioResults: List<MinimizationResult>,
        constant: List<MinimizationResult>
    ) {
        plot(saveFigPath = "results/gradient/convergence.png") {
            ylim(2, 20)
            title("convergence")
            xlabel("steps")
            ylabel("value")

            points {
                label("Golden ratio")
                add((1..goldenRatioResults.size).toList(), goldenRatioResults.map { it.result })
            }

            points {
                linestyle("--")
                label("Dichotomy")
                add((1..dichotomyResults.size).toList(), dichotomyResults.map { it.result })
            }

            points {
                label("Constant step = 0.25")
                add((1..constant.size).toList(), constant.map { it.result })
            }


        }
    }
}
