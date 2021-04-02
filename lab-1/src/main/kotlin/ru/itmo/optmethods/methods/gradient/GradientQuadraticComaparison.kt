package ru.itmo.optmethods.methods.gradient

import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.common.*
import ru.itmo.optmethods.methods.onedim.DichotomyMethod
import kotlin.math.pow

val func1 = TwoDimFunction { x, y -> x.pow(4) + y * y }
val grad1 = TwoDimGradient { x, y ->
    listOf(
        4 * x.pow(3),
        2 * y
    )
}

val func2 = TwoDimFunction { x, y -> x * x + 2 * y * y - x * y + x + y + 3 }
val grad2 = TwoDimGradient { x, y ->
    listOf(
        2 * x - y + 1,
        4 * y - x + 1
    )
}

fun main() {
    writeStats("grad_x^4+y^2", runGradientWith(func1, grad1))
    writeStats("grad_x^2+2y^2-xy+x+y+3", runGradientWith(func2, grad2))
}

private fun runGradientWith(
    func: NDimFunction,
    grad: Gradient
): List<MinimizationResult> {
    val res = ArrayList<MinimizationResult>()
    GradientMethod().findMinimum(
        2,
        listOf(1.0, 1.0),
        func,
        grad,
        DichotomyMethod(),
        { r -> res.add(r) }
    )
    return res
}

private fun writeStats(name: String, res: List<MinimizationResult>) {
    res.joinToString("\n") { "${it.result}, ${it.iterations}" }.let {
        saveToCSV(name, it)
    }
}
