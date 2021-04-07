package ru.itmo.optmethods.functions

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure
import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.common.RationalArray


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

fun interface DerivativeCountingFunction {
    operator fun invoke(args: List<DerivativeStructure>): DerivativeStructure
    operator fun invoke(vararg args: DerivativeStructure): DerivativeStructure =
        invoke(args.toList())

    operator fun invoke(order: Int, args: List<Rational>): DerivativeStructure =
        invoke(order, args.toDoubleArray())

    operator fun invoke(order: Int, args: RationalArray): DerivativeStructure =
        invoke(
            args.mapIndexed { i, value ->
                DerivativeStructure(args.size, order, i, value)
            }
        )
}
