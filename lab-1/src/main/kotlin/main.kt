package common

import dichotomy.DichotomyMethod
import kotlin.math.pow

fun main() {
    val functions = listOf<OneDimFunction>(
        { x -> x.pow(2) + 2 },  // expected
        { x-> x.pow(4) + 3 * x.pow(3) }
    )
    val method = DichotomyMethod()
}
