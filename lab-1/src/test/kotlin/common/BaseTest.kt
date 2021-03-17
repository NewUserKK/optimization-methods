package dichotomy

import common.*
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlin.math.pow

abstract class BaseTest(private val method: MinimizationMethod): FreeSpec({
    // Desmos: x^{4\ }+3x^{3\ }-2x
    val testFunction: OneDimFunction = { x -> x.pow(4) + 3 * x.pow(3) - 2 * x }

    "should correctly find one local minimum" {
        val expected = MinimizationResult(-2.141, -4.148, 0)
        val actual = method.findMinimum(
            rangeStart = -4.0,
            rangeEnd = -1.0,
            function = testFunction
        )

        expected shouldBeAround actual
    }

    "should correctly find any of local minimums" {
        val expectedVariants = listOf(
            MinimizationResult(0.432, -0.587, 0),
            MinimizationResult(-2.141, -4.148, 0)
        )

        val actual = method.findMinimum(
            rangeStart = -20.0,
            rangeEnd = 20.0,
            function = testFunction
        )

        val matchers = expectedVariants.map { result ->
            { actual shouldBeAround result }
        }

        matchAny(matchers)
    }
})
