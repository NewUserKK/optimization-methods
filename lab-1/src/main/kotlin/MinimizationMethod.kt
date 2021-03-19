typealias OneDimFunction = (Rational) -> Rational
typealias NDimFunction = (List<Rational>) -> Rational
typealias Rational = Double

data class MinimizationResult(
    val argument: Rational,
    val result: Rational,
    val iterations: Int
)

data class MultiDimMinimizationResult(
    val argument: List<Rational>,
    val result: Rational,
    val iterations: Int
)

const val DEFAULT_EPS: Rational = 1e-8

interface MinimizationMethod {
    fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult
}

abstract class EpsilonCheckMinimizationMethod(
    val epsilon: Rational
) : MinimizationMethod

abstract class IterationCountCheckMinimizationMethod(
    val maxIterations: Long
) : MinimizationMethod
