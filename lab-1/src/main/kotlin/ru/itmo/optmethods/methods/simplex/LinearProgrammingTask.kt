package ru.itmo.optmethods.methods.simplex

import ru.itmo.optmethods.common.Rational

class LinearProgrammingTask(
    val function: Array<Rational>,
    val constraints: Array<ConstraintEquation>,
) {

    fun negate(): LinearProgrammingTask = LinearProgrammingTask(
        function.map { -it }.toTypedArray(),
        constraints
    )

    fun isCanonical(): Boolean = constraints.all { it.isCanonical() }

    fun asCanonical(): LinearProgrammingTask {
        val constraints = constraints.copyOf()
        var inequalities = 0

        for ((i, constraint) in constraints.withIndex()) {
            if (!constraint.sign.isEquals()) {
                inequalities++
                constraints[i] =
                    if (constraint.sign.isGreater()) constraint.negate() else constraint
            }
        }

        val function = function + zeroArray(inequalities)
        var fixedInequalities = 0

        for ((i, constraint) in constraints.withIndex()) {
            val newConstraint = when {
                constraint.sign.isEquals() -> constraint + zeroArray(inequalities)
                else -> constraint.addVariable(fixedInequalities++, inequalities)
            }
            constraints[i] = if (newConstraint.value < 0) newConstraint.negate() else newConstraint
        }

        return LinearProgrammingTask(
            function,
            constraints,
        )
    }
}

interface LinearProgrammingTaskBuilder {

    fun func(vararg values: Number): Array<Rational> =
        values.map { it.toDouble() }.toTypedArray()

    fun constraint(vararg values: Number): Array<Rational> =
        values.map { it.toDouble() }.toTypedArray()

    fun basis(vararg values: Number): Array<Rational>? =
        values.map { it.toDouble() }.toTypedArray()

    fun noBasis(): Array<Rational>? = null

    infix fun Array<Rational>.eq(value: Number) =
        ConstraintEquation(this, value.toDouble(), Comparison.E)

    infix fun Array<Rational>.ge(value: Number) =
        ConstraintEquation(this, value.toDouble(), Comparison.GE)

    infix fun Array<Rational>.le(value: Number) =
        ConstraintEquation(this, value.toDouble(), Comparison.LE)

    fun task(
        function: Array<Rational>,
        vararg constraints: ConstraintEquation
    ) = LinearProgrammingTask(function, constraints.map { it }.toTypedArray())
}


fun zeroArray(size: Int): Array<Rational> = Array(size) { 0.0 }
