package ru.itmo.optmethods.methods

import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.functions.OneDimFunction

data class MinimizationResult(
    val argument: List<Rational>,
    val result: Rational,
    val iterations: Int,
    val functionsCall: Int
)

const val DEFAULT_EPS: Rational = 1e-8
const val DEFAULT_MAX_ITERATIONS = 100

val MinimizationMethod.name: String
    get() = this::class.simpleName!!

interface MinimizationMethod {
    fun findMinimum(
        rangeStart: Rational,
        rangeEnd: Rational,
        function: OneDimFunction
    ): MinimizationResult
}
