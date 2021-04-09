package ru.itmo.optmethods.methods.simplex

import ru.itmo.optmethods.common.Rational

object ArtificialBasisMethod {

    const val BASIS_EPS = 1e15

    fun findBasis(task: LinearProgrammingTask): Array<Rational> = with(task) {
        val xc = constraints.first().coefficients.size
        val yc = constraints.size

        val yConstraints = mutableListOf<ConstraintEquation>()
        val yBasis = zeroArray(xc).toMutableList()

        for ((i, constraint) in constraints.withIndex()) {
            yConstraints += constraint.addVariable(i, yc)
            yBasis += if (constraint.value == 0.0) BASIS_EPS else constraint.value
        }

        SimplexMethod.findExtremum(
            LinearProgrammingTask(
                zeroArray(xc) + Array(yc) { -1.0 },
                yConstraints.toTypedArray(),
            ),
            yBasis.toTypedArray(), searchForMin = false
        ).point.sliceArray(0 until xc)
    }
}
