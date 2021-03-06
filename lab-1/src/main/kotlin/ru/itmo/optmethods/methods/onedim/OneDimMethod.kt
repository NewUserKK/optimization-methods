package ru.itmo.optmethods.methods.onedim

import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.methods.MinimizationMethod


abstract class OneDimMinimizationMethod : MinimizationMethod {
    protected var onIterationEnd: (rangeStart: Rational, rangeEnd: Rational) -> Unit = { _, _ -> }
        private set

    fun setOnIterationEndListener(block: (rangeStart: Rational, rangeEnd: Rational) -> Unit) {
        onIterationEnd = block
    }
}
