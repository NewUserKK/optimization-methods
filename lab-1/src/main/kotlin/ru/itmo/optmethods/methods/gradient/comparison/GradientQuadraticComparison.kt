package ru.itmo.optmethods.methods.gradient.comparison

import com.github.sh0nk.matplotlib4j.NumpyUtils
import ru.itmo.optmethods.common.PlotUtils
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
        val path = "results/gradient/quadratic"
        PlotUtils.buildContourPlot(path, func1, "x^2+y^2", runGradientWith(func1, grad1), 30, -0.5, 1.2, -0.5, 1.2)
        PlotUtils.buildContourPlot(path, func2, "x^2+2y^2-xy+x+y+3", runGradientWith(func2, grad2), 30, -1.5, 1.2, -1.5, 1.2)
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
