package ru.itmo.optmethods.methods.simplex

import ru.itmo.optmethods.common.Rational

class ConstraintEquation(
    val coefficients: Array<Rational>,
    val value: Rational,
    val sign: Comparison
) {

    fun isCanonical(): Boolean = sign.isEquals() && value >= 0

    fun negate(): ConstraintEquation = ConstraintEquation(
        coefficients.map { -it }.toTypedArray(),
        -value,
        sign.invert()
    )

    operator fun plus(coefficients: Array<Rational>): ConstraintEquation = ConstraintEquation(
        this.coefficients + coefficients, value, sign
    )

    fun addVariable(i: Int, j: Int): ConstraintEquation = ConstraintEquation(
        coefficients + zeroArray(i) + arrayOf(1.0) + zeroArray(j - i - 1),
        value,
        Comparison.E
    )

    fun apply(f: (Rational) -> Rational): ConstraintEquation = ConstraintEquation(
        coefficients.map(f).toTypedArray(),
        f(value),
        sign
    )

    fun add(other: ConstraintEquation, c: Rational): ConstraintEquation = ConstraintEquation(
        coefficients.mapIndexed { i, v -> v + c * other.coefficients[i] }.toTypedArray(),
        value + c * other.value,
        Comparison.E
    )

    operator fun get(i: Int) : Rational = coefficients[i]
}

fun basisComparator(basis: List<Int>) = Comparator<ConstraintEquation> { c1, c2 ->
    -compareLists(c1.coefficients.slice(basis), c2.coefficients.slice(basis))
}

fun compareLists(list1: List<Comparable<*>>, list2: List<Comparable<*>>): Int {
    for (i in 0 until list1.size.coerceAtMost(list2.size)) {
        val elem1 = list1[i]
        val elem2 = list2[i]

        compareValues(elem1, elem2).let {
            if (it != 0) return it
        }
    }
    return compareValues(list1.size, list2.size)
}

enum class Comparison {
    LE,
    E,
    GE;

    fun isEquals(): Boolean = this == E
    fun isLess(): Boolean = this == LE
    fun isGreater(): Boolean = this == GE

    fun invert(): Comparison = when (this) {
        LE -> GE
        E -> E
        GE -> LE
    }
}


