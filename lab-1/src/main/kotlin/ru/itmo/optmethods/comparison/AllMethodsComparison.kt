package ru.itmo.optmethods.comparison

import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.NDimFunction
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.functions.TwoDimGradient
import ru.itmo.optmethods.methods.MinimizationResult

import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.plot.PlotUtils
import kotlin.math.E
import kotlin.math.pow

object AllMethodsComparison : MethodComparison {
    val starts = listOf(
        listOf(0.0, 0.0),
        listOf(5.0, 10.0),
        listOf(-20.0, 10.0),
        listOf(100.0, 100.0),
        listOf(-3000.0, 1001.0)
    )

    // 100.0 * (y - x)^2 + (1 - x)^2
    val func1 = TwoDimFunction { x, y -> 100.0 * (y - x).pow(2) + (1 - x).pow(2) }
    val grad1 = TwoDimGradient { x, y ->
        listOf(
            202.0 * x - 200.0 * y - 2,
            200.0 * (y - x)
        )
    }

    // 100.0 * (y - x^2)^2 + (1 - x)^2
    val func2 = TwoDimFunction { x, y -> 100.0 * (y - x * x).pow(2) + (1.0 - x).pow(2) }
    val grad2 = TwoDimGradient { x, y ->
        listOf(
            400.0 * x.pow(3) + x * (2.0 - 400.0 * y) - 2.0,
            200.0 * (y - x * x)
        )
    }

    // 2 * e^(-((x-1)/2)^2 - ((y-1)/1)^2) + 3 * e^(-((x-2)/3)^2 - ((y-3)/2)^2)
    val func3 = TwoDimFunction { x, y ->
        2.0 * E.pow(-((x - 1.0) / 2.0).pow(2.0) - ((y - 1.0) / 1.0).pow(2)) + 3.0 * E.pow(
            -((x - 2.0) / 3.0).pow(
                2
            ) - ((y - 3.0) / 2.0).pow(2)
        )
    }

    // Ищем максимум для этой ф-ции
    // берем антиградиент, чтобы потом он вычитался, и получался положительный градиент
    val grad3 = TwoDimGradient { x, y ->
        listOf(
            -2.0 / 3.0 * (x - 2.0) * E.pow(
                -1.0 / 9.0 * (x - 2.0).pow(2) - 1.0 / 4.0 * (y - 3.0).pow(
                    2
                )
            ) - (x - 1.0) * E.pow(-1.0 / 4.0 * (x - 1.0).pow(2) - (y - 1.0).pow(2)),
            -3.0 / 2.0 * (y - 3.0) * E.pow(
                -1.0 / 9.0 * (x - 2.0).pow(2) - 1.0 / 4.0 * (y - 3.0).pow(
                    2
                )
            ) - 4.0 * (y - 1.0) * E.pow(-1.0 / 4.0 * (x - 1.0).pow(2) - (y - 1.0).pow(2))
        ).map { -it }
    }


    override fun compare() {
        compare("100.0 * (y - x)^2 + (1 - x)^2", func1, grad1)
        compare("100.0 * (y - x^2)^2 + (1 - x)^2",func2, grad2)
        compare("func3-long",func3, grad3)
    }

    private fun compare(funcName: String, func: TwoDimFunction, grad: Gradient) {
        println("for func: $funcName")
        starts.forEach { start ->
            println("start: ${start.joinToString(", ")}")
            val simpleGradRes = withTimeMeasure { runGradientWith(func, grad, start) }
            printStats(
                "Simple gradient:",
                simpleGradRes.first,
                simpleGradRes.second
            )
            val reevesGradRes = withTimeMeasure { runFletcherReevesGradientWith(func, grad, start) }
            printStats(
                "Fletcher-Reeves gradient:",
                reevesGradRes.first,
                reevesGradRes.second
            )
            val newtonRes = withTimeMeasure { runNewtonWith(func, start) }
            printStats(
                "newton:",
                newtonRes.first,
                newtonRes.second
            )
            PlotUtils.buildContourPlot(
                "results/deep-compare/",
                func,
                "func=${funcName}, start=${start.joinToString(", ")}",
                labels = listOf("fletcher", "simple", "newton"),
                results = listOf(reevesGradRes.first, simpleGradRes.first, newtonRes.first),
                levelsCount = 7,
                xMin = -10.0, xMax = 10.0, yMin = -10.0, yMax = 10.0
            )
        }
        println("----------------------------------")
    }

    private fun <R> withTimeMeasure(block: () -> R): Pair<R, Long> {
        val time = System.currentTimeMillis()
        val blockRes = block.invoke()
        return Pair(blockRes, System.currentTimeMillis() - time)
    }

    private fun printStats(
        title: String,
        res: List<MinimizationResult>,
        workTime: Long
    ) {
        println(title)
        println("iterations: ${res.size}, answer: ${res.last().argument}, work-time: ${workTime}ms")
    }

    private fun runNewtonWith(func: NDimFunction, start: List<Rational>): List<MinimizationResult> {
        // TODO
        return listOf(MinimizationResult(listOf(0.0, 0.0), 0.0, 0, 0))
    }

    private fun runFletcherReevesGradientWith(
        func: NDimFunction,
        grad: Gradient,
        start: List<Rational>
    ): List<MinimizationResult> {
        val results = ArrayList<MinimizationResult>()
        try {
            GradientMethod(maxIterations = 3000).findMinimumFletcherReeves(
                n = 2,
                start = start,
                function = func,
                gradient = grad,
                stepFinder = DichotomyMethod(),
                onStep = { minimizationResult -> results.add(minimizationResult) },
                10
            )
        } catch (ignored: Exception) {
        }

        return results
    }

    private fun runGradientWith(
        func: NDimFunction,
        grad: Gradient,
        start: List<Rational>
    ): List<MinimizationResult> {
        val results = ArrayList<MinimizationResult>()
        try {
            GradientMethod(maxIterations = 3000).findMinimum(
                n = 2,
                start = start,
                function = func,
                gradient = grad,
                stepFinder = DichotomyMethod(),
                onStep = { minimizationResult -> results.add(minimizationResult) }
            )
        } catch (ignored: Exception) {
        }

        return results
    }
}
