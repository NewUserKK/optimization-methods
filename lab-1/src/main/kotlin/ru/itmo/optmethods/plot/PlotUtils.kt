package ru.itmo.optmethods.plot

import com.github.sh0nk.matplotlib4j.NumpyUtils
import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.methods.MinimizationResult
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.roundToLong

object PlotUtils {
    fun buildContourPlot(
        path: String,
        func: TwoDimFunction,
        title: String,
        labels: List<String>,
        results: List<List<MinimizationResult>>,
        levelsCount: Int = 7,
        xMin: Rational? = null, xMax: Rational? = null,
        yMin: Rational? = null, yMax: Rational? = null
    ) {
        val minX = results.minOf { it.minOf { it.argument[0] } }
        val maxX = results.maxOf { it.maxOf { it.argument[0] } }
        val minY = results.minOf { it.minOf { it.argument[1] } }
        val maxY = results.maxOf { it.maxOf { it.argument[1] } }
        val lastX = results[0].last().argument[0]
        val lastY = results[0].last().argument[1]
        val trueMinX = xMin ?: max(lastX - 10.0, minX)
        val trueMaxX = xMax ?: min(lastX + 10.0, maxX)
        val trueMinY = yMin ?: max(lastY - 10.0, minY)
        val trueMaxY = yMax ?: min(lastY + 10.0, maxY)

        plot(saveFigPath = "$path/${title.replace(" ", "_")}.png") {
            title(title)

            val contourBuilder = contour().apply {
                val steps = 50
                val xs = NumpyUtils.linspace(trueMinX, trueMaxX, steps)
                val ys = NumpyUtils.linspace(trueMinY, trueMaxY, steps)
                val grid = NumpyUtils.meshgrid(xs, ys)
                val zs = grid.calcZ { xi: Double, yj: Double ->
                    func.invoke(xi, yj)
                }
                val levels =
                    NumpyUtils.linspace(
                        func.invoke(trueMinX, trueMinY).roundValue(),
                        func.invoke(trueMaxX, trueMaxY).roundValue(),
                        levelsCount
                    )
                val additional = results[0].map { it.result }.takeLast(levelsCount).map {it.roundValue()}
                levels((levels + additional).sorted().distinct())
                add(xs, ys, zs)
            }

            results.forEachIndexed { i, res ->
                points {
                    when(i) {
                        1 -> linestyle("-.")
                        2 -> linestyle("--")
                    }
                    label(labels[i])
                    add(
                        res.map { it.argument[0].coerceIn(trueMinX, trueMaxX) },
                        res.map { it.argument[1].coerceIn(trueMinY, trueMaxY) }
                    )
                }
            }

            clabel(contourBuilder)
                .inline(true)
                .fontsize(10.0)
        }
    }

    private fun Rational.roundValue() : Rational {
        return (this * 1000.0).roundToLong() / 1000.0
    }
}
