package ru.itmo.optmethods.plot

import com.github.sh0nk.matplotlib4j.Plot
import com.github.sh0nk.matplotlib4j.PythonConfig
import com.github.sh0nk.matplotlib4j.builder.PlotBuilder
import java.io.File

fun plot(show: Boolean = false, saveFigPath: String? = null, block: Plot.() -> Unit): Plot {
    val pythonConfig = when (val pythonPath = System.getenv("pythonPath")) {
        null -> {
            println("No 'pythonPath' set in local.properties or, " +
                "if you're launching via IDEA configurations, at IDEA env variables, " +
                "falling back to system default python installation"
            )
            PythonConfig.systemDefaultPythonConfig()
        }
        else -> PythonConfig.pythonBinPathConfig(pythonPath)
    }

    return Plot.create(pythonConfig).apply {
        block()

        if (show) {
            show()
        }

        if (saveFigPath != null) {
            val file = File(saveFigPath)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }

            savefig(saveFigPath)
            executeSilently()
        }
    }
}

fun Plot.points(block: PlotBuilder.() -> Unit): Unit = plot().run { block() }
