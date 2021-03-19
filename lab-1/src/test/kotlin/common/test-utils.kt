package common

import MinimizationResult
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.shouldBe
import kotlin.math.abs

const val DEFAULT_EPS = 1e-3

infix fun MinimizationResult.shouldBeAround(other: MinimizationResult) =
    shouldBeAround(other, DEFAULT_EPS)

fun MinimizationResult.shouldBeAround(expected: MinimizationResult, eps: Double) {
    this shouldBe minimizationResultMatcher(expected, eps)
}

fun minimizationResultMatcher(expected: MinimizationResult, eps: Double): Matcher<MinimizationResult> =
    object : Matcher<MinimizationResult> {
        override fun test(value: MinimizationResult): MatcherResult {
            val passed =
                value.argument.zip(expected.argument).all { abs(it.first - it.second) < eps }&&
                abs(value.result - expected.result) < eps

            return MatcherResult(
                passed = passed,
                failureMessage = "Expected $value to be equal $expected (except iteration count)",
                negatedFailureMessage = "Did not expect $value not to be equal $expected " +
                    "(except iteration count)"
            )
        }
    }


fun matchAny(assertions: List<() -> Unit>) {
    var passed = false
    var lastAssertionError: AssertionError? = null
    assertions.forEach { assert ->
        try {
            assert()
            passed = true
        } catch (e: AssertionError) {
            lastAssertionError = e
        }
    }

    if (!passed) {
        throw AssertionError(
            "No matchers passed! See last assertion error below",
            lastAssertionError
        )
    }
}

fun matchAny(vararg assertions: () -> Unit) = matchAny(assertions.toList())
