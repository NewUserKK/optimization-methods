package quadratic

import Rational
import common.NDimFunction
import gradient.Gradient
import java.util.*

class QuadraticFunction(
    val n: Int,
    val a: Hessian,
    val b: Array<Rational>,
    val c: Rational
) : NDimFunction {

    init {
        assert(n == a.n)
        assert(n == b.size)
    }

    override fun invoke(args: List<Rational>): Rational {
        var value = c

        for (i in 0 until n) {
            for (j in i until n) {
                value += args[i] * args[j] * a[i, j]
            }
            value += args[i] * b[i]
        }

        return value
    }
}

fun Random.genQuadraticFunction(n: Int, maxCoefficientValue: Rational): QuadraticFunction {
    val a = genHessian(n, maxCoefficientValue)
    val b = Array(n) { nextDouble() * maxCoefficientValue }
    val c = nextDouble() * maxCoefficientValue
    return QuadraticFunction(n, a, b, c)
}

fun QuadraticFunction.getConditionNumber(): Rational {
    val minEigenvalue = (0 until n).minOf { a.values[it][it] }
    val maxEigenvalue = (0 until n).maxOf { a.values[it][it] }
    return maxEigenvalue / minEigenvalue
}

fun QuadraticFunction.getGradient(): Gradient = { args ->
    (0 until n).map { getPartialDerivative(it, args) }
}

fun QuadraticFunction.getPartialDerivative(argIndex: Int, args: List<Rational>): Rational {
    assert(argIndex < n)
    var value = b[argIndex]
    for (i in 0 until n) {
        value += 2 * a[argIndex, i] * args[i]
    }
    return value
}
