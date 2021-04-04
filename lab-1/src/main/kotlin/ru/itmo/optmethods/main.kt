package ru.itmo.optmethods

import ru.itmo.optmethods.functions.quadratic.comparison.ArgsCountInfluenceComparison
import ru.itmo.optmethods.functions.quadratic.comparison.ConditionNumberInfluenceComparison
import ru.itmo.optmethods.methods.gradient.comparison.GradientQuadraticComparison
import ru.itmo.optmethods.methods.gradient.comparison.GradientStepsFinderComparison
import ru.itmo.optmethods.methods.onedim.comparison.OneDimMethodsComparison


fun main() {
//    OneDimMethodsComparison.compare()
//    GradientStepsFinderComparison.compare()
//    GradientQuadraticComparison.compare()
//    ConditionNumberInfluenceComparison.compare()
//    ArgsCountInfluenceComparison.compare()
    NewtonAndGradComparison.compare()
}
