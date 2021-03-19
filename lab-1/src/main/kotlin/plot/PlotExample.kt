package plot

import com.github.sh0nk.matplotlib4j.Plot
import com.github.sh0nk.matplotlib4j.PythonConfig

fun main() {
    Plot.create(PythonConfig.pythonBinPathConfig("/usr/bin/python3")).apply {
        this.plot().apply {
            add(listOf(1,2,3,4,5,6,8), listOf(1,2,6,3,1,4,7))
        }
    }.show()
}
