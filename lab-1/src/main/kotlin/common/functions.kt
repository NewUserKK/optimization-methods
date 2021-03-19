package common

import Rational

fun interface NDimFunction {
    operator fun invoke(args: List<Rational>) : Rational
}

fun interface OneDimFunction : NDimFunction{
    override fun invoke(args: List<Rational>): Rational = this(args[0])

    operator fun invoke(x: Rational) : Rational
}

fun interface TwoDimFunction : NDimFunction{
    override fun invoke(args: List<Rational>): Rational = this(args[0], args[1])

    operator fun invoke(x: Rational, y: Rational) : Rational
}

