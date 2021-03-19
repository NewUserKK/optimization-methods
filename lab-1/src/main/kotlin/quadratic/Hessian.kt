package quadratic

import Rational
import common.Matrix
import java.util.*

class Hessian(val n: Int, val values: Matrix) {

    operator fun get(i: Int, j: Int) : Rational = values[i][j]
}

fun Random.genHessian(n: Int, maxCoefficientValue: Rational): Hessian {
    val values = Array(n) { Array(n) { 0.0 } }

    for (i in 0 until n) {
        for (j in i until n) {
            values[i][j] = nextDouble() * maxCoefficientValue
            values[j][i] = values[i][j]
        }
    }

    return Hessian(n, values)
}
