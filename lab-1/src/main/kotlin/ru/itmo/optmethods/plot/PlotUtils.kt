package ru.itmo.optmethods.plot

import com.github.sh0nk.matplotlib4j.NumpyUtils
import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.functions.TwoDimFunction
import ru.itmo.optmethods.methods.MinimizationResult

import kotlin.collections.plus

object PlotUtils {
    fun buildContourPlot(
        path: String,
        func: TwoDimFunction,
        title: String,
        labels: List<String>,
        results: List<List<MinimizationResult>>,
        levelsCount: Int,
        xMin: Rational, xMax: Rational,
        yMin: Rational, yMax: Rational
    ) {
        plot(saveFigPath = "$path/$title.png") {
            title(title)

            val contourBuilder = contour().apply {
                val steps = 100
                val xs = NumpyUtils.linspace(results[0].minOf { it.argument[0] }, results[0].maxOf { it.argument[0] }, steps)
                val ys = NumpyUtils.linspace(results[0].minOf { it.argument[1] }, results[0].maxOf { it.argument[1] }, steps)
                val grid = NumpyUtils.meshgrid(xs, ys)
                val zs = grid.calcZ { xi: Double, yj: Double ->
                    func.invoke(xi, yj)
                }
                val levels =
                    NumpyUtils.linspace(
                        results[0].minOf { it.result },
                        results[0].maxOf { it.result },
                        levelsCount
                    )
                val additional = results[0].map { it.result }.sorted().take(levelsCount)
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
                        res.map { it.argument[0] },
                        res.map { it.argument[1] }
                    )
                }
            }

            clabel(contourBuilder)
                .inline(true)
                .fontsize(10.0)
        }
    }
}
