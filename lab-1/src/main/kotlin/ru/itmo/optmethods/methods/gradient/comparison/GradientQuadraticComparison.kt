package ru.itmo.optmethods.methods.gradient.comparison

import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.comparison.MethodComparison
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.NDimFunction
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.functions.TwoDimGradient
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.plot.PlotUtils
import ru.itmo.optmethods.plot.plot
import ru.itmo.optmethods.plot.points
import kotlin.math.cos
import kotlin.math.sin

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
        checkStartPoint("x^2+y^2", func1, grad1)
        checkStartPoint("x^2+2y^2-xy+x+y+3", func2, grad2)


        val path = "results/gradient/quadratic"
        PlotUtils.buildContourPlot(
            path = path,
            func = func1, title = "x^2+y^2 from (1.0,1.0)",
            labels = listOf(""),
            results = listOf(runGradientWith(func1, grad1, listOf(1.0, 1.0))),
            levelsCount = 30,
            xMin = -0.5, xMax = 1.2,
            yMin = -0.5, yMax = 1.2
        )

        PlotUtils.buildContourPlot(
            path = path,
            func = func1, title = "x^2+y^2 from (0.1,1.0)",
            labels = listOf(""),
            results = listOf(runGradientWith(func1, grad1, listOf(0.1, 1.0))),
            levelsCount = 30,
            xMin = -0.5, xMax = 1.2,
            yMin = -0.5, yMax = 1.2
        )

        PlotUtils.buildContourPlot(
            path = path,
            func = func1, title = "x^2+y^2 from (1.0,0.3)",
            labels = listOf(""),
            results = listOf(runGradientWith(func1, grad1, listOf(1.0, 0.3))),
            levelsCount = 30,
            xMin = -0.5, xMax = 1.2,
            yMin = -0.5, yMax = 1.2
        )

        PlotUtils.buildContourPlot(
            path = path,
            func = func1, title = "x^2+y^2 from (1.0,0.0)",
            labels = listOf(""),
            results = listOf(runGradientWith(func1, grad1, listOf(1.0, 0.0))),
            levelsCount = 30,
            xMin = -0.5, xMax = 1.2,
            yMin = -0.5, yMax = 1.2
        )

        PlotUtils.buildContourPlot(
            path = path,
            func = func2, title = "x^2+2y^2-xy+x+y+3 from (1.0,1.0)",
            labels = listOf(""),
            results = listOf(runGradientWith(func2, grad2, listOf(1.0, 1.0))),
            levelsCount = 30,
            xMin = -1.5, xMax = 1.2,
            yMin = -1.5, yMax = 1.2
        )

        PlotUtils.buildContourPlot(
            path = path,
            func = func2, title = "x^2+2y^2-xy+x+y+3 from(0.1,1.0)",
            labels = listOf(""),
            results = listOf(runGradientWith(func2, grad2, listOf(0.1, 1.0))),
            levelsCount = 30,
            xMin = -1.5, xMax = 1.2,
            yMin = -1.5, yMax = 1.2
        )

        PlotUtils.buildContourPlot(
            path = path,
            func = func2, title = "x^2+2y^2-xy+x+y+3 from (1.0,0.3)",
            labels = listOf(""),
            results = listOf(runGradientWith(func2, grad2, listOf(1.0, 0.3))),
            levelsCount = 30,
            xMin = -1.5, xMax = 1.2,
            yMin = -1.5, yMax = 1.2
        )

        PlotUtils.buildContourPlot(
            path = path,
            func = func2, title = "x^2+2y^2-xy+x+y+3 from (1.0,0.0)",
            labels = listOf(""),
            results = listOf(runGradientWith(func2, grad2, listOf(1.0, 0.0))),
            levelsCount = 30,
            xMin = -1.5, xMax = 1.2,
            yMin = -1.5, yMax = 1.2
        )
    }

    private fun checkStartPoint(
        title: String,
        func: NDimFunction,
        grad: Gradient,
    ) {
        val points = mutableListOf<Pair<Double, Double>>()

        for (angle in 0 until 360) {
            val angleRad = Math.toRadians(angle.toDouble())
            val start = listOf(cos(angleRad), sin(angleRad))

            val results = ArrayList<MinimizationResult>()
            GradientMethod().findMinimum(
                n = 2,
                start = start,
                function = func,
                gradient = grad,
                stepFinder = DichotomyMethod(),
                onStep = results::add
            )

            points += angleRad to results.size.toDouble()
        }

        plot(saveFigPath = "results/gradient/start-points/$title.png") {
            title("Number of iterations from each start point")
            xlabel("Angle of start point")
            ylabel("Number of iterations")

            points {
                add(
                    points.map { it.first },
                    points.map { it.second },
                )
            }
        }

    }

    private fun runGradientWith(
        func: NDimFunction,
        grad: Gradient,
        start: List<Rational>
    ): List<MinimizationResult> {
        val results = ArrayList<MinimizationResult>()
        GradientMethod().findMinimum(
            n = 2,
            start = start,
            function = func,
            gradient = grad,
            stepFinder = DichotomyMethod(),
            onStep = { minimizationResult -> results.add(minimizationResult) }
        )

        return results
    }
}
