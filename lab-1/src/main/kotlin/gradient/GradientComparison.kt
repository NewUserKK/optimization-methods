package gradient

import MinimizationMethod
import MinimizationResult
import common.NDimFunction
import onedim.DichotomyMethod
import onedim.GoldenRatioMethod
import kotlin.math.pow

// func: x^3 + 3xy^2 - 15x - 12y
private val testFunction =
    NDimFunction { args -> args[0].pow(3) + 3.0 * args[0] * args[1].pow(2) - 15.0 * args[0] - 12.0 * args[1] }
private val gradient: Gradient = { args ->
    listOf(
        3.0 * args[0].pow(2) + 3.0 * args[1].pow(2) - 15.0,
        6.0 * args[1] * args[0] - 12.0
    )
}

fun main(args: Array<String>) {
    val grad = GradientMethod()
    println("Dichotomy: ")
    println(runGradientWith(DichotomyMethod(), grad))
    println("Golder Ration")
    println(runGradientWith(GoldenRatioMethod(), grad))
    // TODO add third method
    //val dichRes = runGradientWith(DichotomyMethod(), grad)
}

private fun runGradientWith(stepFinder: MinimizationMethod, grad: GradientMethod): MinimizationResult {
    return grad.findMinimum(
        2,
        listOf(0.0, 0.0),
        testFunction,
        gradient,
        DichotomyMethod()
    )
}
