package ru.itmo.optmethods.methods.gradient.comparison

import com.github.sh0nk.matplotlib4j.NumpyUtils
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.NDimFunction
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.functions.TwoDimGradient
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.Rational
import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points

object GradientQuadraticComparison {

    // x^2 + y^2
    val func1 = TwoDimFunction { x, y -> x * x + y * y }
    val grad1 = TwoDimGradient { x, y ->
        listOf(
            2 * x,
            2 * y
        )
    }

    // x^2 + 2y^2 - xy + x + y + 3
    val func2 = TwoDimFunction { x, y -> x * x + 2 * y * y - x * y + x + y + 3 }
    val grad2 = TwoDimGradient { x, y ->
        listOf(
            2 * x - y + 1,
            4 * y - x + 1
        )
    }

    fun compare() {
        buildPlots(func1, "x^2+y^2", runGradientWith(func1, grad1), -0.5, 1.2, -0.5, 1.2)
        buildPlots(func2, "x^2+2y^2-xy+x+y+3", runGradientWith(func2, grad2), -1.5, 1.2, -1.5, 1.2)
    }

    private fun runGradientWith(
        func: NDimFunction,
        grad: Gradient
    ): List<MinimizationResult> {
        val results = ArrayList<MinimizationResult>()
        GradientMethod().findMinimum(
            n = 2,
            start = listOf(1.0, 1.0),
            function = func,
            gradient = grad,
            stepFinder = DichotomyMethod(),
            onStep = { minimizationResult -> results.add(minimizationResult) }
        )

        return results
    }

    private fun buildPlots(
        func: TwoDimFunction,
        funcName: String,
        results: List<MinimizationResult>,
        xMin: Rational,
        xMax: Rational,
        yMin: Rational,
        yMax: Rational
    ) {
        plot(saveFigPath = "results/gradient/quadratic_$funcName.png") {
            title(funcName)

            val contourBuilder = contour().apply {
                val steps = 100
                val xs = NumpyUtils.linspace(xMin, xMax, steps)
                val ys = NumpyUtils.linspace(yMin, yMax, steps)
                val grid = NumpyUtils.meshgrid(xs, ys)
                val zs = grid.calcZ { xi: Double, yj: Double ->
                    func.invoke(xi, yj)
                }
                val levels =
                    NumpyUtils.linspace(
                        results.minOf { it.result },
                        results.maxOf { it.result },
                        30
                    )
                val additional = results.map { it.result }.sorted().take(30)
                levels(additional + levels)
                add(xs, ys, zs)
            }

            points {
                add(
                    results.map { it.argument[0] },
                    results.map { it.argument[1] }
                )
            }

            clabel(contourBuilder)
                .inline(true)
                .fontsize(10.0)
        }
    }
}
