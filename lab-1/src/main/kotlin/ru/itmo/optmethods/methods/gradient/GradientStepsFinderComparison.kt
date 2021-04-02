package ru.itmo.optmethods.methods.gradient

import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.common.Gradient
import ru.itmo.optmethods.common.InvocationsCountingFunction
import ru.itmo.optmethods.common.TwoDimFunction
import ru.itmo.optmethods.common.saveToCSV
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import ru.itmo.optmethods.methods.onedim.GoldenRatioMethod

// x^2 + 2y^2 - xy + x + y + 3
val func = TwoDimFunction { x, y -> x * x + 2 * y * y - x * y + x + y + 3 }
val grad = Gradient { (x, y) ->
    listOf(
        2 * x - y + 1,
        4 * y - x + 1
    )
}

fun main() {
    val grad = GradientMethod(1e-3)
    writeStats("Dichotomy", runGradientWith(DichotomyMethod(), grad))
    writeStats("Golder_Ration", runGradientWith(GoldenRatioMethod(), grad))
    writeStats("Constant", runGradientWith(GradientMethod.ConstantStep(0.25), grad))
    // TODO add another
    //val dichRes = runGradientWith(DichotomyMethod(), grad)
}

private fun runGradientWith(
    stepFinder: MinimizationMethod,
    gradMethod: GradientMethod
): List<MinimizationResult> {
    val results = ArrayList<MinimizationResult>()
    gradMethod.findMinimum(
        n = 2,
        start = listOf(10000.0, 10000.0),
        function = func,
        gradient = grad,
        stepFinder = stepFinder,
        onStep = { minimizationResult -> results.add(minimizationResult) }
    )

    return results
}

private fun writeStats(name: String, res: List<MinimizationResult>) {
    res.joinToString("\n") { "${it.result}, ${it.iterations}" }.let {
        saveToCSV(name, it)
    }
}
