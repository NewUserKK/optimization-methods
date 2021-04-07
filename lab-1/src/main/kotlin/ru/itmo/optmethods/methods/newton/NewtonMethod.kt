package ru.itmo.optmethods.methods.newton

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure
import org.apache.commons.math3.linear.EigenDecomposition
import org.apache.commons.math3.linear.MatrixUtils.createRealIdentityMatrix
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.SingularValueDecomposition
import ru.itmo.optmethods.common.*
import ru.itmo.optmethods.functions.DerivativeCountingFunction
import ru.itmo.optmethods.methods.DEFAULT_EPS
import ru.itmo.optmethods.methods.DEFAULT_MAX_ITERATIONS
import ru.itmo.optmethods.methods.MinimizationResult
import java.math.BigDecimal
import java.math.RoundingMode

class NewtonMethod(
    private val eps: Rational = DEFAULT_EPS,
    private val maxIterations: Int = DEFAULT_MAX_ITERATIONS
) {
    fun findMinimum(
        startPoint: List<Rational>,
        function: DerivativeCountingFunction,
        step: Rational = 1.0,
        onStep: (MinimizationResult) -> Unit = {}
    ): MinimizationResult {
        var x = startPoint.toDoubleArray().toColumnRealMatrix()
        var iterations = 0

        do {
            val xVector = x.getColumn(0)
            val result = function(order = 2, args = xVector)

            if (result.allDerivatives.any { it.isInfinite() || it.isNaN() }) {
                break
            }

            val gradient = calculateGradient(result, xVector)
            val hessian = calculateHessian(result, xVector)

            val invertedHessian = SingularValueDecomposition(hessian).solver.inverse
            x -= step * (invertedHessian * gradient)

            onStep(MinimizationResult(
                argument = xVector.toList(),
                result = result.value,
                iterations = iterations,
                functionsCall = iterations
            ))

//            if (iterations % 100000 == 0) {
//                println("i=${iterations}, x=${x.getColumn(0).toList()}")
//            }

            iterations++
        } while (gradient.norm > eps && iterations < maxIterations)

        val xVector = x.getColumn(0)

        return MinimizationResult(
            argument = xVector.toList(),
            result = function(order = 2, args = xVector).value,
            iterations = iterations,
            functionsCall = iterations
        ).also { onStep(it) }
    }

    private fun calculateGradient(
        functionResult: DerivativeStructure,
        argumentVector: RationalArray
    ): RealMatrix {
        val identityMatrix = createRealIdentityMatrix(argumentVector.size)
        val identityVectors = argumentVector.indices
            .map { i -> identityMatrix.getRow(i).map { it.toInt() }.toIntArray() }

        return RationalArray(argumentVector.size) { i ->
            functionResult.getPartialDerivative(*identityVectors[i])
        }.toColumnRealMatrix()
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
        functionResult: DerivativeStructure,
        argumentVector: RationalArray
    ): RealMatrix {
        val identityMatrix = createRealIdentityMatrix(argumentVector.size)
        val identityVectors = argumentVector.indices
            .map { i -> identityMatrix.getRow(i).map { it.toInt() }.toIntArray() }

        return Array(argumentVector.size) { i ->
            val identityVector = identityVectors[i]
            val partialDerivativeVectors = argumentVector.indices.map { k ->
                identityVector.copyOf().also { it[k] += 1 }
            }

            RationalArray(argumentVector.size) { j ->
                functionResult.getPartialDerivative(*partialDerivativeVectors[j])
            }
        }.toRealMatrix()
    }
}
