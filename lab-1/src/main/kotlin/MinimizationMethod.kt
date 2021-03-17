typealias OneDimFunction = (Rational) -> Rational
typealias Rational = Double

data class MinimizationResult(
    val argument: Rational,
    val result: Rational,
    val iterations: Int
)

const val DEFAULT_EPS: Rational = 1e-8

interface MinimizationMethod {
    fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        eps: Rational = DEFAULT_EPS,
        function: OneDimFunction
    ): MinimizationResult
}
