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
    val testFunction = NDimFunction { (x, y) ->
        x.pow(3) + 3.0 * x * y.pow(2) - 15.0 * x - 12.0 * y
    }
    val gradient = Gradient { (x, y) ->
        listOf(
            3.0 * x.pow(2) + 3.0 * y.pow(2) - 15.0,
            6.0 * y * x - 12.0
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
