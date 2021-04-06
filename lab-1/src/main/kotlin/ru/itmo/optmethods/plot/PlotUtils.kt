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
        funcName: String,
        results: List<MinimizationResult>,
        levelsCount: Int,
        xMin: Rational, xMax: Rational,
        yMin: Rational, yMax: Rational
    ) {
        plot(saveFigPath = "$path/$funcName.png") {
            title(funcName)

            val contourBuilder = contour().apply {
                val steps = 100
                val xs = NumpyUtils.linspace(xMin, xMax, steps)
                val ys = NumpyUtils.linspace(yMin, yMax, steps)
                val grid = NumpyUtils.meshgrid(xs, ys)
                val zs = grid.calcZ { xi: Double, yj: Double ->
                    func.invoke(xi, yj)
                }
                val levels =
                    NumpyUtils.linspace(
                        results.minOf { it.result },
                        results.maxOf { it.result },
                        levelsCount
                    )
                val additional = results.map { it.result }.sorted().take(levelsCount)
                levels((additional + levels).sorted().distinct())
                add(xs, ys, zs)
            }

            points {
                add(
                    results.map { it.argument[0] },
                    results.map { it.argument[1] }
                )
            }

            clabel(contourBuilder)
                .inline(true)
                .fontsize(10.0)
        }
    }
}
