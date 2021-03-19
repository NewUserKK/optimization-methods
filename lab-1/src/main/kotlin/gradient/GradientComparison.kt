package gradient

import MinimizationMethod
import MinimizationResult
import common.TwoDimFunction
import onedim.DichotomyMethod
import onedim.GoldenRatioMethod

fun main(args: Array<String>) {
    val grad = GradientMethod()
    println("Dichotomy: ")
    println(runGradientWith(DichotomyMethod(), grad))
    println("Golder Ration")
    println(runGradientWith(GoldenRatioMethod(), grad))
    // TODO add third method
    //val dichRes = runGradientWith(DichotomyMethod(), grad)
}

private fun runGradientWith(
    stepFinder: MinimizationMethod,
    grad: GradientMethod
): MinimizationResult {
    return grad.findMinimum(
        2,
        listOf(10000.0, 10000.0),
        TwoDimFunction { x, y -> x * x + 2 * y * y - x * y + x + y + 3 },
        { (x, y) ->
            listOf(
                2 * x - y + 1,
                4 * y - x + 1
            )
        },
        DichotomyMethod()
    )
}
