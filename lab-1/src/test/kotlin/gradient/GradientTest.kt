package gradient

import MinimizationResult
import common.Gradient
import common.NDimFunction
import common.shouldBeAround
import io.kotest.core.spec.style.FreeSpec
import onedim.DichotomyMethod
import kotlin.math.pow

class GradientTest : FreeSpec({
    // func: x^3 + 3xy^2 - 15x - 12y
    val testFunction = NDimFunction { args ->
        args[0].pow(3) + 3.0 * args[0] * args[1].pow(2) - 15.0 * args[0] - 12.0 * args[1]
    }
    val gradient = Gradient { args ->
        listOf(
            3.0 * args[0].pow(2) + 3.0 * args[1].pow(2) - 15.0,
            6.0 * args[1] * args[0] - 12.0
        )
    }
    val method = GradientMethod()

    "should correctly find one local minimum" {
        val expected = MinimizationResult(listOf(2.0, 1.0), -28.0, 0)
        val actual = method.findMinimum(
            2,
            listOf(1.0, 1.0),
            testFunction,
            gradient,
            DichotomyMethod()
        )
        expected shouldBeAround actual
    }

})
