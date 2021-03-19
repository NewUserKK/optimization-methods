package gradiend

import common.TwoDimFunction
import gradient.GradientMethod
import onedim.DichotomyMethod

fun main() {
    val gm = GradientMethod()
    val min = gm.findMinimum(
        n = 2,
        start = listOf(10000.0, 10000.0),
        TwoDimFunction { x, y -> x * x + 2 * y * y - x * y + x + y + 3 },
        { (x, y) ->
            listOf(
                2 * x - y + 1,
                4 * y - x + 1
            )
        },
        DichotomyMethod(),
        {
            println(it.argument[0].toString() + ", " + it.argument[1].toString())
        }
    ).result
    println(min)
}
