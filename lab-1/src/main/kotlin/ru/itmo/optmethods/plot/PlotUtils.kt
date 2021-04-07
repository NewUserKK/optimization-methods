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
        val trueMinX = xMin ?: minX
        val trueMaxX = xMax ?: maxX
        val trueMinY = yMin ?: minY
        val trueMaxY = yMax ?: maxY

        plot(saveFigPath = "$path/${title.replace(" ", "_")}.png") {
            title(title)

            val contourBuilder = contour().apply {
                val steps = 50
                xlim(trueMinX, trueMaxX)
                ylim(trueMinY, trueMaxY)
                val xs = NumpyUtils.linspace(trueMinX, trueMaxX, steps)
                val ys = NumpyUtils.linspace(trueMinY, trueMaxY, steps)
                val grid = NumpyUtils.meshgrid(xs, ys)
                val zs = grid.calcZ { xi: Double, yj: Double ->
                    func.invoke(xi, yj)
                }
//                val levels =
//                    NumpyUtils.linspace(
//                        func.invoke(trueMinX, trueMinY).roundValue(),
//                        func.invoke(trueMaxX, trueMaxY).roundValue(),
//                        levelsCount
//                    )
//                val additional = results.last().map { it.result }.takeLast(levelsCount).map {it.roundValue()}
//                levels(additional.sorted().distinct())
                add(xs, ys, zs)
            }

            results.forEachIndexed { i, res ->
                points {
                    when(i) {
                        1 -> linestyle("-.")
                        2 -> linestyle("--")
                    }
                    label(labels[i])
                    xlim(trueMinX, trueMaxX)
                    ylim(trueMinY, trueMaxY)
                    add(
                        res.map { it.argument[0]},
                        res.map { it.argument[1]}
                    )
                }
            }

            clabel(contourBuilder)
                .inline(true)
                .fontsize(10.0)
        }
    }
}
