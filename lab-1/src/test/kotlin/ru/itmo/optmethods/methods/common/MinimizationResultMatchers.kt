package ru.itmo.optmethods.methods.common

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.shouldBe
import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.methods.MinimizationResult
import kotlin.math.abs

const val DEFAULT_EPS = 1e-3

infix fun MinimizationResult.shouldBeAround(other: MinimizationResult) =
    shouldBeAround(other, DEFAULT_EPS)

fun MinimizationResult.shouldBeAround(expected: MinimizationResult, eps: Double) {
    this shouldBe minimizationResultMatcher(expected, eps)
}

infix fun MinimizationResult.shouldHaveSameCallCountAs(expected: MinimizationResult) {
    this shouldBe minimizationResultCallingMatcher(expected)
}

infix fun Rational.shouldBeAround(other: Rational) = this shouldBe epsMatcher(other, 1e-10)

fun epsMatcher(
    expected: Rational,
    eps: Double
): Matcher<Rational> =
    object : Matcher<Rational> {
        override fun test(value: Rational): MatcherResult {
            val passed = abs(value - expected) < eps

            return MatcherResult(
                passed = passed,
                failureMessage = "Expected $value to be equal $expected",
                negatedFailureMessage = "Did not expect $value not to be equal $expected "
            )
        }
    }

fun minimizationResultMatcher(
    expected: MinimizationResult,
    eps: Double
): Matcher<MinimizationResult> =
    object : Matcher<MinimizationResult> {
        override fun test(value: MinimizationResult): MatcherResult {
            val passed =
                value.argument.zip(expected.argument).all { abs(it.first - it.second) < eps } &&
                    abs(value.result - expected.result) < eps

            return MatcherResult(
                passed = passed,
                failureMessage = "Expected $value to be equal $expected (except counters)",
                negatedFailureMessage = "Did not expect $value not to be equal $expected " +
                    "(except iteration count)"
            )
        }
    }

fun minimizationResultCallingMatcher(expected: MinimizationResult): Matcher<MinimizationResult> =
    object : Matcher<MinimizationResult> {
        override fun test(value: MinimizationResult): MatcherResult {
            return MatcherResult(
                passed = value.functionsCall == expected.functionsCall,
                failureMessage = "Expected function to be called ${expected.functionsCall} " +
                    "times, got ${value.functionsCall}",
                negatedFailureMessage = "Did not expect function to be called " +
                    "${value.functionsCall} times instead of ${expected.functionsCall}"
            )
        }
    }
