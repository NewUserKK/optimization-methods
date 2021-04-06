package ru.itmo.optmethods.functions.quadratic

import ru.itmo.optmethods.common.Matrix
import ru.itmo.optmethods.common.Rational

import java.util.*

class Hessian(val n: Int, val values: Matrix) {
    operator fun get(i: Int, j: Int): Rational = values[i][j]
}

fun Random.genHessian(n: Int, maxCoefficientValue: Rational): Hessian {
    val values = Array(n) { DoubleArray(n) }

    for (i in 0 until n) {
        for (j in i until n) {
            values[i][j] = nextDouble() * maxCoefficientValue
            values[j][i] = values[i][j]
        }
    }

    return Hessian(n, values)
}

fun Random.genHessian(n: Int, k: Rational, maxCoefficientValue: Rational): Hessian {
    val values = Array(n) { DoubleArray(n) }

    val eigenValues = mutableListOf<Rational>()
    if (n < 2) {
        eigenValues += nextDouble() * maxCoefficientValue
    } else {
        val maxEigenValue = nextDouble().coerceAtLeast(0.1) * maxCoefficientValue
        val minEigenValue = maxEigenValue / k
        eigenValues += maxEigenValue
        eigenValues += minEigenValue
        repeat(n - 2) {
            eigenValues += minEigenValue + nextDouble() * (maxEigenValue - minEigenValue)
        }
        eigenValues.shuffle()
    }

    for (i in 0 until n) {
        for (j in i until n) {
            if (i == j) {
                values[i][i] = eigenValues[i]
            } else {
                values[i][j] = nextDouble() * maxCoefficientValue
                values[j][i] = values[i][j]
            }
        }
    }

    return Hessian(n, values)
}
