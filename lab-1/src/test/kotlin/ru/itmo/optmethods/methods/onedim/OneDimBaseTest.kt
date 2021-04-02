package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.methods.MinimizationMethod
import ru.itmo.optmethods.methods.MinimizationResult
import ru.itmo.optmethods.common.OneDimFunction
import ru.itmo.optmethods.common.matchAny
import ru.itmo.optmethods.common.shouldBeAround
import io.kotest.core.spec.style.FreeSpec
import kotlin.math.pow

abstract class OneDimBaseTest(private val method: MinimizationMethod): FreeSpec({
    // Desmos: x^{4\ }+3x^{3\ }-2x
    val testFunction = OneDimFunction { x -> x.pow(4) + 3 * x.pow(3) - 2 * x }

    "should correctly find one local minimum" {
        val expected = MinimizationResult(listOf(-2.141), -4.148, 0)
        val actual = method.findMinimum(
            rangeStart = -4.0,
            rangeEnd = -1.0,
            function = testFunction
        )

        expected shouldBeAround actual
    }

    "should correctly find any of local minimums" {
        val expectedVariants = listOf(
            MinimizationResult(listOf(0.432), -0.587, 0),
            MinimizationResult(listOf(-2.141), -4.148, 0)
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
})
