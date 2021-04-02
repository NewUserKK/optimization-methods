package ru.itmo.optmethods.plot

import com.github.sh0nk.matplotlib4j.Plot
import com.github.sh0nk.matplotlib4j.PythonConfig

fun main() {
    val pythonPath = System.getenv("pythonPath")
        ?: error("Set 'pythonPath' with matplotlib in your local.properties!")

    Plot.create(PythonConfig.pythonBinPathConfig(pythonPath)).apply {
        this.plot().apply {
            add(listOf(1,2,3,4,5,6,8), listOf(1,2,6,3,1,4,7))
        }
    }.show()
}
