package ru.itmo.optmethods.comparison

import ru.itmo.optmethods.common.*
import ru.itmo.optmethods.functions.*
import ru.itmo.optmethods.methods.DEFAULT_MAX_ITERATIONS
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.newton.NewtonMethod
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.plot.PlotUtils
import kotlin.math.E
import kotlin.math.pow

data class MeasuredResult<R>(val result: R, val timeMs: Long)

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
    val func1Newton = DerivativeCountingFunction { (x, y) ->
        100.0 * (y - x).pow(2) + (1.0 - x).pow(2)
    }
    val grad1 = TwoDimGradient { x, y ->
        listOf(
            202.0 * x - 200.0 * y - 2,
            200.0 * (y - x)
        )
    }

    // 100.0 * (y - x^2)^2 + (1 - x)^2
    val func2 = TwoDimFunction { x, y -> 100.0 * (y - x * x).pow(2) + (1.0 - x).pow(2) }
    val func2Newton = DerivativeCountingFunction { (x, y) ->
        100.0 * (y - x * x).pow(2) + (1.0 - x).pow(2)
    }
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
    val func3Newton = DerivativeCountingFunction { (x, y) ->
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
        compare("100.0 * (y - x)^2 + (1 - x)^2", func1, func1Newton, grad1)
        compare("100.0 * (y - x^2)^2 + (1 - x)^2", func2, func2Newton, grad2)
        compare("func3-long", func3, func3Newton, grad3)
    }

    private fun compare(
        funcName: String,
        func: TwoDimFunction,
        newtonFunc: DerivativeCountingFunction,
        grad: Gradient
    ) {
        println("\n========== $funcName ==========")
        starts.forEach { start ->
            println("\n\n----- Start: (${start.joinToString(", ")}) -----")
            val simpleGradRes = withTimeMeasure { runGradientWith(func, grad, start) }
            printStats(
                "Simple gradient:",
                simpleGradRes.result,
                simpleGradRes.timeMs
            )

            val reevesGradRes = withTimeMeasure { runFletcherReevesGradientWith(func, grad, start) }
            printStats(
                "Fletcher-Reeves gradient:",
                reevesGradRes.result,
                reevesGradRes.timeMs
            )

//            val newtonRes = withTimeMeasure { listOf(MinimizationResult(listOf(0.0,0.0), 0.0, 0, 0)) }
            val newtonRes = withTimeMeasure { runNewtonWith(newtonFunc, start) }
            printStats(
                "newton:",
                newtonRes.result,
                newtonRes.timeMs
            )

            PlotUtils.buildContourPlot(
                "results/deep-compare/",
                func,
                "func=${funcName}, start=${start.joinToString(", ")}",
                labels = listOf("fletcher", "simple", "newton"),
                results = listOf(reevesGradRes.result, simpleGradRes.result, newtonRes.result),
                levelsCount = 7
            )
        }
        println("----------------------------------")
    }

    private fun <R> withTimeMeasure(block: () -> R): MeasuredResult<R> {
        val time = System.currentTimeMillis()
        val blockRes = block.invoke()
        return MeasuredResult(blockRes, System.currentTimeMillis() - time)
    }

    private fun printStats(
        title: String,
        res: List<MinimizationResult>,
        workTime: Long
    ) {
        println(title)
        println("iterations: ${res.size}, answer: ${res.last().argument}, work-time: ${workTime}ms\n")
    }

    private fun runNewtonWith(
        func: DerivativeCountingFunction,
        start: List<Rational>
    ): List<MinimizationResult> {
        val results = mutableListOf<MinimizationResult>()

        NewtonMethod().findMinimum(
            startPoint = start,
            function = func,
            onStep = { result -> results += result }
        )

        return results
    }

    private fun runFletcherReevesGradientWith(
        func: NDimFunction,
        grad: Gradient,
        start: List<Rational>
    ): List<MinimizationResult> {
        val results = ArrayList<MinimizationResult>()
        try {
            GradientMethod().findMinimumFletcherReeves(
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
            GradientMethod().findMinimum(
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
