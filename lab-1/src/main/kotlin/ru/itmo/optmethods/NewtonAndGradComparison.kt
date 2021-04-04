package ru.itmo.optmethods

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
import kotlin.math.pow

object NewtonAndGradComparison {

    // 100.0 * (y - x)^2 + (1 - x)^2
    val func1 = TwoDimFunction { x, y -> 100.0 * (y - x).pow(2) + (1 - x).pow(2) }
    val grad1 = TwoDimGradient { x, y ->
        listOf(
            202.0 * x - 200.0 * y - 2,
            200.0 * (y - x)
        )
    }

    fun compare() {
        PlotUtils.buildContourPlot(
            "results/newtonAndGrad/gradient",
            func1,
            "100.0 * (y - x)^2 + (1 - x)^2",
            runGradientWith(func1, grad1, listOf(0.0, 0.0)),
            3,
            0.0,
            2.0,
            0.0,
            2.0
        )
    }

    private fun runGradientWith(
        func: NDimFunction,
        grad: Gradient,
        start: List<Rational>
    ): List<MinimizationResult> {
        val results = ArrayList<MinimizationResult>()
        GradientMethod().findMinimumAnti(
            n = 2,
            start = start,
            function = func,
            gradient = grad,
            stepFinder = DichotomyMethod(),
            onStep = { minimizationResult -> results.add(minimizationResult) },
            3
        )

        return results
    }
}
