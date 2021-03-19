package gradient

import MinimizationMethod
import MinimizationResult
import common.Gradient
import common.TwoDimFunction
import common.saveToCSV
import onedim.DichotomyMethod
import onedim.GoldenRatioMethod

val func = TwoDimFunction { x, y -> x * x + 2 * y * y - x * y + x + y + 3 }
val grad = Gradient { (x, y) ->
    listOf(
        2 * x - y + 1,
        4 * y - x + 1
    )
}

fun main(args: Array<String>) {
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
    val res = ArrayList<MinimizationResult>()
    gradMethod.findMinimum(
        2,
        listOf(10000.0, 10000.0),
        func,
        grad,
        stepFinder,
        { r -> res.add(r) }
    )
    return res
}

private fun writeStats(name: String, res: List<MinimizationResult>) {
    res.joinToString("\n") { "${it.result}, ${it.iterations}" }.let {
        saveToCSV(name, it)
    }
}
