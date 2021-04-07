package ru.itmo.optmethods.common

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

operator fun DerivativeStructure.plus(other: DerivativeStructure): DerivativeStructure =
    add(other)

operator fun DerivativeStructure.minus(other: DerivativeStructure): DerivativeStructure =
    subtract(other)

operator fun DerivativeStructure.times(other: DerivativeStructure): DerivativeStructure =
    multiply(other)

operator fun DerivativeStructure.div(other: DerivativeStructure): DerivativeStructure =
    divide(other)

operator fun DerivativeStructure.unaryMinus(): DerivativeStructure = 0.0 - this

operator fun DerivativeStructure.plus(other: Rational): DerivativeStructure = add(other)
operator fun DerivativeStructure.minus(other: Rational): DerivativeStructure = subtract(other)
operator fun DerivativeStructure.times(other: Rational): DerivativeStructure = multiply(other)
operator fun DerivativeStructure.div(other: Rational): DerivativeStructure = divide(other)


operator fun Rational.plus(other: DerivativeStructure): DerivativeStructure = other.add(this)
operator fun Rational.minus(other: DerivativeStructure): DerivativeStructure = other.subtract(this)
operator fun Rational.times(other: DerivativeStructure): DerivativeStructure = other.multiply(this)
operator fun Rational.div(other: DerivativeStructure): DerivativeStructure = other.divide(this)

fun Rational.pow(other: DerivativeStructure): DerivativeStructure =
    DerivativeStructure.pow(this, other)


operator fun RealMatrix.plus(other: RealMatrix): RealMatrix = add(other)
operator fun RealMatrix.minus(other: RealMatrix): RealMatrix = subtract(other)
operator fun RealMatrix.times(other: RealMatrix): RealMatrix = multiply(other)

operator fun RealMatrix.plus(other: Rational): RealMatrix = scalarAdd(other)
operator fun RealMatrix.minus(other: Rational): RealMatrix = scalarAdd(-other)
operator fun RealMatrix.times(other: Rational): RealMatrix = scalarMultiply(other)
operator fun Rational.plus(other: RealMatrix): RealMatrix = other + this
operator fun Rational.times(other: RealMatrix): RealMatrix = other * this

operator fun RealMatrix.get(i: Int, j: Int): Rational = getEntry(i, j)

fun RationalArray.toColumnRealMatrix(): RealMatrix = MatrixUtils.createColumnRealMatrix(this)
fun RationalArray.toRowRealMatrix(): RealMatrix = MatrixUtils.createRowRealMatrix(this)

fun Array<RationalArray>.toRealMatrix(): RealMatrix = MatrixUtils.createRealMatrix(this)
