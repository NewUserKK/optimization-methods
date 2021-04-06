package ru.itmo.optmethods.common

typealias Rational = Double

/** (sqrt(5.0) + 1) / 2 */
const val PHI = 1.618033988749895

inline fun avg(x1: Rational, x2: Rational) = (x1 + x2) / 2
