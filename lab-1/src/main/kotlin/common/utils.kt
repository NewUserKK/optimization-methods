package common

import Rational

inline fun avg(x1: Rational, x2: Rational) = (x1 + x2) / 2

fun List<Rational>.minus(another: List<Rational>): List<Rational> {
    assert(this.size == another.size)
    return this.zip(another).map { it.first - it.second }
}

fun List<Rational>.mul(another: List<Rational>): List<Rational> {
    assert(this.size == another.size)
    return this.zip(another).map { it.first * it.second }
}

fun List<Rational>.mul(another: Rational): List<Rational> {
    return this.map { it * another }
}

