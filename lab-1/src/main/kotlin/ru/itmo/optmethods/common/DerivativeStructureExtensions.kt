package ru.itmo.optmethods.common

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure

operator fun DerivativeStructure.plus(other: DerivativeStructure): DerivativeStructure =
    add(other)

operator fun DerivativeStructure.times(other: DerivativeStructure): DerivativeStructure =
    multiply(other)

operator fun DerivativeStructure.minus(other: DerivativeStructure): DerivativeStructure =
    subtract(other)

operator fun DerivativeStructure.div(other: DerivativeStructure): DerivativeStructure =
    divide(other)
