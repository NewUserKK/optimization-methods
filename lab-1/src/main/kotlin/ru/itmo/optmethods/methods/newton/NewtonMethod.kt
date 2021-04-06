package ru.itmo.optmethods.methods.newton

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure
import org.apache.commons.math3.linear.MatrixUtils
import ru.itmo.optmethods.common.avg
import ru.itmo.optmethods.common.plus
import ru.itmo.optmethods.functions.DerivativeCountingFunction
import ru.itmo.optmethods.methods.MinimizationResult

fun main() {
    val f: DerivativeCountingFunction = DerivativeCountingFunction { (x, y) ->
        x.pow(2) + y.pow(2)
    }

    NewtonMethod().findMinimum(2, f)
}

class NewtonMethod {
    fun findMinimum(
        argsCount: Int,
        function: DerivativeCountingFunction
    ): MinimizationResult {
        var x = generateSequence { 1.0 }
            .take(argsCount)
            .toList()
            .toDoubleArray()

        do {
            val gradient = calculateGradient(function, x)
        } while (TODO())
    }

    private fun calculateGradient(
        function: DerivativeCountingFunction,
        argumentVector: DoubleArray
    ): DoubleArray {
        val argument = argumentVector.mapIndexed { i, value ->
            DerivativeStructure(argumentVector.size, 2, i, value)
        }

        val result = function(argument)

        val identityMatrix = MatrixUtils.createRealIdentityMatrix(argument.size)
        val identityVectors = argumentVector.indices
            .map { i -> identityMatrix.getRow(i).map { it.toInt() }.toIntArray() }

        return DoubleArray(argumentVector.size) { i ->
            result.getPartialDerivative(*identityVectors[i])
        }
    }

    /**
     * H(x) =
     * [[ d2f/dx1^2 d2f/dx1dx2 d2f/dx1dx3 .. d2f/dx1dxn ]]
     * [[ d2f/dx1dx2 d2f/dx2^2 d2f/dx2dx3 .. d2f/dx2dxn ]]
     * [[ ... ]]
     * [[ d2f/dx1dxn d2f/dx2dxn d2f/dx3dxn .. d2f/dxn^2 ]]
     *
     * Partial derivative vectors:
     * [[ (2, 0...) (1, 1, 0...) (1, 0, 1, 0...) ... (...0, 2) ]]
     * [[ (1, 1...) (0, 2, 0...) (0, 1, 1, 0...) ... (0, 1, 0 ... 0, 1) ]]
     * [[ ... ]]
     * [[ (1, 0 ... 0, 1) (0, 1, 0 ... 0, 1) (0, 0, 1, 0 ... 0, 1) ... (...0, 2) ]]
     */
    private fun calculateHessian(
        function: DerivativeCountingFunction,
        argumentVector: DoubleArray
    ): Array<DoubleArray> {
        val argument = argumentVector.mapIndexed { i, value ->
            DerivativeStructure(argumentVector.size, 2, i, value)
        }

        val result = function(argument)

        val identityMatrix = MatrixUtils.createRealIdentityMatrix(argument.size)
        val identityVectors = argumentVector.indices
            .map { i -> identityMatrix.getRow(i).map { it.toInt() }.toIntArray() }

        return Array(argumentVector.size) { i ->
            val identityVector = identityVectors[i]
            val partialDerivativeVectors = argumentVector.indices.map { k->
                identityVector.copyOf().also { it[k] += 1 }
            }

            DoubleArray(argumentVector.size) { j ->
                result.getPartialDerivative(*partialDerivativeVectors[j])
            }
        }
    }
}
