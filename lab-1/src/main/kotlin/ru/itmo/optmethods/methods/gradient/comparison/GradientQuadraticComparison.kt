package ru.itmo.optmethods.methods.gradient.comparison

import ru.itmo.optmethods.comparison.MethodComparison
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.NDimFunction
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.functions.TwoDimGradient
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.plot.PlotUtils

object GradientQuadraticComparison : MethodComparison {
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

    override fun compare() {
        val path = "results/gradient/quadratic"
        PlotUtils.buildContourPlot(
            path = path,
            func = func1, funcName = "x^2+y^2",
            results = runGradientWith(func1, grad1),
            levelsCount = 30,
            xMin = -0.5, xMax = 1.2,
            yMin = -0.5, yMax = 1.2
        )

        PlotUtils.buildContourPlot(
            path = path,
            func = func2, funcName = "x^2+2y^2-xy+x+y+3",
            results = runGradientWith(func2, grad2),
            levelsCount = 30,
            xMin = -1.5, xMax = 1.2,
            yMin = -1.5, yMax = 1.2
        )
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
}
