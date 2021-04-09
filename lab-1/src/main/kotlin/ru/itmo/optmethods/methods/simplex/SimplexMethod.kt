package ru.itmo.optmethods.methods.simplex

import ru.itmo.optmethods.common.Rational

object SimplexMethod {

    const val EPS = 1e-10

    fun findExtremum(
        task: LinearProgrammingTask,
        basis: Array<Rational>?,
        searchForMin: Boolean
    ): OptimizationResult {
        var canonicalTask = if (task.isCanonical()) task else task.asCanonical()
        if (searchForMin) canonicalTask = canonicalTask.negate()

        return findExtremumWithBasisSolution(
            canonicalTask,
            basis ?: ArtificialBasisMethod.findBasis(canonicalTask),
            searchForMin
        )
    }

    private fun findExtremumWithBasisSolution(
        task: LinearProgrammingTask,
        originBasis: Array<Rational>,
        searchForMin: Boolean
    ): OptimizationResult = with(task) {
        val basis = getBasisIndices(originBasis).toMutableList()
        val constraints = handleConstraints(constraints, basis)
        val simplexTable = fillSimplexTable(constraints, basis, function)

        val n = constraints.size
        val m = function.size + 1
        while (true) {
            var maxDiff = 0.0
            var leadElementIndex = -1
            for (i in 1 until m) {
                if (simplexTable[n][i] > maxDiff) {
                    maxDiff = simplexTable[n][i]
                    leadElementIndex = i
                }
            }
            if (maxDiff < EPS) break

            var minFraction = 1e10
            if (simplexTable[0][leadElementIndex] != 0.0) {
                if (isPositive(simplexTable[0][0]) == isPositive(simplexTable[0][leadElementIndex])) {
                    minFraction = simplexTable[0][0] / simplexTable[0][leadElementIndex]
                }
            }

            var leadIndex = 0

            for (i in 0 until n) {
                val fraction = when {
                    simplexTable[i][leadElementIndex] == 0.0 -> 1e10
                    else -> simplexTable[i][0] / simplexTable[i][leadElementIndex]
                }
                if (isPositive(simplexTable[i][0]) == isPositive(simplexTable[i][leadElementIndex]) && fraction < minFraction) {
                    minFraction = fraction
                    leadIndex = i
                }
            }

            val leadElement = simplexTable[leadIndex][leadElementIndex]
            (0 until m).forEach { simplexTable[leadIndex][it] /= leadElement }

            for (i in 0..n) {
                if (i == leadIndex) continue
                val d = -simplexTable[i][leadElementIndex]
                for (j in 0 until m) {
                    simplexTable[i][j] += d * simplexTable[leadIndex][j]
                }
            }

            basis[leadIndex] = leadElementIndex - 1
        }

        val point = zeroArray(originBasis.size)
        for (i in 0 until n) {
            point[basis[i]] = simplexTable[i][0]
        }

        val value = if (searchForMin) simplexTable[n][0] else -simplexTable[n][0]
        OptimizationResult(point, value, originBasis)
    }

    private fun handleConstraints(
        originalConstraints: Array<ConstraintEquation>,
        basis: List<Int>,
    ): List<ConstraintEquation> {
        var constraints = originalConstraints.sortedWith(basisComparator(basis)).toMutableList()

        var i = 0
        while (i < constraints.size) {
            val constraint = constraints[i]
            if (constraint.coefficients.all { it < EPS }) {
                constraints.removeAt(i--)
                continue
            }

            val basisIndex = basis[i]
            val basisValue = constraint[basisIndex]
            val newConstraint = constraint.apply { it / basisValue }

            constraints = constraints.mapIndexed { j, con ->
                if (i != j) con.add(newConstraint, -con[basisIndex]) else newConstraint
            }.sortedWith(basisComparator(basis)).toMutableList()
            i++
        }

        return constraints
    }

    private fun getBasisIndices(originBasis: Array<Rational>): List<Int> {
        val basis = mutableListOf<Int>()
        for ((i, v) in originBasis.withIndex()) {
            if (v != 0.0) basis.add(i)
            originBasis[i] = if (v == ArtificialBasisMethod.BASIS_EPS) 0.0 else v
        }
        return basis
    }

    private fun fillSimplexTable(
        constraints: List<ConstraintEquation>,
        basis: List<Int>,
        function: Array<Rational>
    ): List<Array<Rational>> {
        val m = function.size + 1
        val simplexTable = constraints.map { arrayOf(it.value) + it.coefficients }.toMutableList()

        val f = zeroArray(m)
        for (i in 0 until m - 1) {
            if (i in basis) {
                for (constraint in constraints) {
                    if (constraint[i] == 0.0) continue
                    for (j in constraint.coefficients.indices) {
                        if (i == j) continue
                        f[j + 1] -= constraint[j] * function[i] / constraint[i]

                    }
                    f[0] -= constraint.value * function[i] / constraint[i]
                }
            } else {
                f[i + 1] += function[i]
            }
        }
        simplexTable += f
        return simplexTable
    }

    private fun isPositive(value: Rational) = value >= 0
}

class OptimizationResult(
    val point: Array<Rational>,
    val value: Rational,
    val basis: Array<Rational>
)
