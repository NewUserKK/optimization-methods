package ru.itmo.optmethods.methods.onedim

import io.kotest.core.spec.style.FreeSpec
import ru.itmo.optmethods.functions.OneDimFunction
import ru.itmo.optmethods.common.matchAny
import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.methods.common.shouldBeAround
import kotlin.math.pow

abstract class OneDimBaseTest(private val method: MinimizationMethod) : FreeSpec() {
    // Desmos: x^{4\ }+3x^{3\ }-2x
    private val testFunction = OneDimFunction { x -> x.pow(4) + 3 * x.pow(3) - 2 * x }

    abstract fun expectedFunctionCallCount(iterations: Int): Int

    init {
        "should correctly find one local minimum" {
            val expected = MinimizationResult(
                argument = listOf(-2.141),
                result = -4.148,
                iterations = 0,
                functionsCall = 0
            )

            val actual = method.findMinimum(
                rangeStart = -4.0,
                rangeEnd = -1.0,
                function = testFunction
            )

            actual shouldBeAround expected
        }

        "should correctly find any of local minimums" {
            val expectedVariants = listOf(
                MinimizationResult(
                    argument = listOf(0.432),
                    result = -0.587,
                    iterations = 0,
                    functionsCall = 0
                ),
                MinimizationResult(
                    argument = listOf(-2.141),
                    result = -4.148,
                    iterations = 0,
                    functionsCall = 0
                )
            )

            val actual = method.findMinimum(
                rangeStart = -2000.0,
                rangeEnd = 2000.0,
                function = testFunction
            )

            val matchers = expectedVariants.map { result ->
                { actual shouldBeAround result }
            }

            matchAny(matchers)
        }

        "function should be called proper amount of times" {
            val actual = method.findMinimum(
                rangeStart = -4.0,
                rangeEnd = -1.0,
                function = testFunction
            )
            val expected = actual.copy(
                functionsCall = expectedFunctionCallCount(actual.iterations)
            )

            actual shouldBeAround expected
        }
    }
}
