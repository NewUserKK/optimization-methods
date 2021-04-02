package methods

import common.OneDimFunction

typealias Rational = Double

data class MinimizationResult(
    val argument: List<Rational>,
    val result: Rational,
    val iterations: Int
)

const val DEFAULT_EPS: Rational = 1e-8
const val DEFAULT_MAX_ITERATIONS = 100

interface MinimizationMethod {
    fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult
}



