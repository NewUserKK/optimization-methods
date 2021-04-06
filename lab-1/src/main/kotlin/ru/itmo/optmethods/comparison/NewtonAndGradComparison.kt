package ru.itmo.optmethods.comparison

import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.functions.Gradient
import ru.itmo.optmethods.functions.NDimFunction
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.functions.TwoDimGradient
import ru.itmo.optmethods.methods.MinimizationResult

import ru.itmo.optmethods.methods.gradient.GradientMethod
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import kotlin.math.E
import kotlin.math.pow

object NewtonAndGradComparison : MethodComparison {
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
        println("for func: 100.0 * (y - x)^2 + (1 - x)^2")
        compare(func1, grad1)

        println("for func: 100.0 * (y - x^2)^2 + (1 - x)^2")
        compare(func2, grad2)

        println("for func: 2 * e^(-((x-1)/2)^2 - ((y-1)/1)^2) + 3 * e^(-((x-2)/3)^2 - ((y-3)/2)^2)")
        compare(func3, grad3)
    }

    private fun compare(func: NDimFunction, grad: Gradient) {
        starts.forEach {
            printStats(
                it,
                runGradientWith(func, grad, it),
                runNewtonWith(func, it)
            )
        }
    }

    private fun printStats(
        start: List<Rational>,
        gradRes: List<MinimizationResult>,
        newtonRes: List<MinimizationResult>
    ) {
        println("start: ${start.joinToString(", ")}")
        println("gradient: iterations: ${gradRes.size}, minimum: ${gradRes.last().argument}")
        //println("newton: iterations: ${newtonRes.size}, minimum: ${newtonRes.last().argument}")
    }

    private fun runNewtonWith(func: NDimFunction, start: List<Rational>): List<MinimizationResult> {
        // TODO
        return emptyList()
    }

    private fun runGradientWith(
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
}
