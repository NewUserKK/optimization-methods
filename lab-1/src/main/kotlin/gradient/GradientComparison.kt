package gradient

import MinimizationMethod
import MinimizationResult
import common.TwoDimFunction
import common.saveToCSV
import onedim.DichotomyMethod
import onedim.GoldenRatioMethod

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
    grad: GradientMethod
): List<MinimizationResult> {
    val res = ArrayList<MinimizationResult>()
    grad.findMinimum(
        2,
        listOf(10000.0, 10000.0),
        TwoDimFunction { x, y -> x * x + 2 * y * y - x * y + x + y + 3 },
        { (x, y) ->
            listOf(
                2 * x - y + 1,
                4 * y - x + 1
            )
        },
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
