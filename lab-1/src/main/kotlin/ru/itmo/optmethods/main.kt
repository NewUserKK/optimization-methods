package ru.itmo.optmethods

import ru.itmo.optmethods.comparison.MethodComparison
import ru.itmo.optmethods.comparison.NewtonAndGradComparison
import ru.itmo.optmethods.functions.quadratic.comparison.ArgsCountInfluenceComparison
import ru.itmo.optmethods.functions.quadratic.comparison.ConditionNumberInfluenceComparison
import ru.itmo.optmethods.methods.gradient.comparison.GradientQuadraticComparison
import ru.itmo.optmethods.methods.gradient.comparison.GradientStepsFinderComparison
import ru.itmo.optmethods.methods.onedim.comparison.OneDimMethodsComparison


fun main() {
    val fastComparisons: List<MethodComparison> = listOf(
        OneDimMethodsComparison,
        GradientStepsFinderComparison,
        GradientQuadraticComparison,
        NewtonAndGradComparison,
    )

    val heavyComparisons: List<MethodComparison> = listOf(
        ConditionNumberInfluenceComparison,
        ArgsCountInfluenceComparison,
    )

//    val comparisons = fastComparisons + heavyComparisons
    val comparisons = fastComparisons

    comparisons.forEach { comparison -> comparison.compare() }
}
