package dichotomy

import common.MinimizationMethod
import common.MinimizationResult
import common.OneDimFunction
import common.shouldBeAround
import io.kotest.core.spec.style.FreeSpec
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

        actual shouldBeAround expected
    }

    "should correctly find minimum when there is two extrema" {
        val expected = MinimizationResult(0.432, -0.587, 0)
        val actual = method.findMinimum(
            rangeStart = -1.0,
            rangeEnd = 1.0,
            function = testFunction
        )

        actual shouldBeAround expected
    }
})
