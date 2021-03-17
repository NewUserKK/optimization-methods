package common

import io.kotest.matchers.doubles.shouldBeLessThan
import kotlin.math.abs

const val DEFAULT_EPS = 1e-3

infix fun Double.shouldBeAround(other: Double) =
    shouldBeAround(other, DEFAULT_EPS)

fun Double.shouldBeAround(other: Double, eps: Double) {
    abs(this - other) shouldBeLessThan eps
}

infix fun MinimizationResult.shouldBeAround(other: MinimizationResult) =
    shouldBeAround(other, DEFAULT_EPS)

fun MinimizationResult.shouldBeAround(other: MinimizationResult, eps: Double) {
    this.argument.shouldBeAround(other.argument, eps)
    this.result.shouldBeAround(other.result, eps)
}
