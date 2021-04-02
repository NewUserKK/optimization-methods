package ru.itmo.optmethods.common

import ru.itmo.optmethods.methods.Rational

fun interface NDimFunction {
    operator fun invoke(args: List<Rational>): Rational
    operator fun invoke(vararg args: Rational): Rational = invoke(args.toList())
}

fun interface OneDimFunction : NDimFunction {
    override fun invoke(args: List<Rational>): Rational = this(args[0])
    operator fun invoke(x: Rational): Rational
}

fun interface TwoDimFunction : NDimFunction {
    override fun invoke(args: List<Rational>): Rational = this(args[0], args[1])
    operator fun invoke(x: Rational, y: Rational): Rational
}

fun interface Gradient {
    operator fun invoke(args: List<Rational>): List<Rational>
}

fun interface TwoDimGradient : Gradient {
    override fun invoke(args: List<Rational>): List<Rational> = this(args[0], args[1])
    operator fun invoke(x: Rational, y: Rational): List<Rational>
}


class InvocationsCountingFunction(val function: NDimFunction) {
    var invocationsCount = 0
        private set

    operator fun invoke(args: List<Rational>): Rational =
        function(args).also { invocationsCount++ }
    operator fun invoke(vararg args: Rational): Rational = invoke(args.toList())

    fun copy(): InvocationsCountingFunction = InvocationsCountingFunction(function)
}
