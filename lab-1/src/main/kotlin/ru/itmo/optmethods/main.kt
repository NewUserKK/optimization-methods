package ru.itmo.optmethods

import ru.itmo.optmethods.methods.gradient.comparison.GradientStepsFinderComparison
import ru.itmo.optmethods.methods.onedim.comparison.OneDimMethodsComparison


fun main() {
    OneDimMethodsComparison.compare()
    GradientStepsFinderComparison.compare()
}
