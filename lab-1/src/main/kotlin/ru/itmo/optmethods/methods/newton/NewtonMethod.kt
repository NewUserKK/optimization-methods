package ru.itmo.optmethods.methods.newton

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure
import org.apache.commons.math3.exception.MaxCountExceededException
import org.apache.commons.math3.linear.*
import org.apache.commons.math3.linear.MatrixUtils.createRealIdentityMatrix
import ru.itmo.optmethods.common.*
import ru.itmo.optmethods.functions.DerivativeCountingFunction
import ru.itmo.optmethods.methods.DEFAULT_EPS
import ru.itmo.optmethods.methods.DEFAULT_MAX_ITERATIONS
import ru.itmo.optmethods.methods.MinimizationResult

class NewtonMethod(
    private val eps: Rational = DEFAULT_EPS,
    private val maxIterations: Int = DEFAULT_MAX_ITERATIONS
) {
    fun findExtremum(
        startPoint: List<Rational>,
        function: DerivativeCountingFunction,
        step: Rational = 1.0,
        onStep: (MinimizationResult) -> Unit = {},
        findMax: Boolean = false
    ): MinimizationResult {
        var x = startPoint.toDoubleArray().toColumnRealMatrix()
        var iterations = 0

        val invertCoefficient = when {
            findMax -> -1.0
            else -> 1.0
        }

        do {
            val xVector = x.getColumn(0)
            val result = function(order = 2, args = xVector)

            if (result.allDerivatives.any { it.isInfinite() || it.isNaN() }) {
                break
            }

            val gradient = invertCoefficient * calculateGradient(result, xVector)
            val hessian = invertCoefficient * makePositiveDefinite(calculateHessian(result, xVector))

            val invertedHessian = LUDecomposition(hessian).solver.inverse

            x -= step * (invertedHessian * gradient)

            onStep(
                MinimizationResult(
                    argument = xVector.map { invertCoefficient * it },
                    result = result.value,
                    iterations = iterations,
                    functionsCall = iterations
                )
            )

            iterations++
        } while (gradient.norm > eps && iterations < maxIterations - 1)

        val xVector = x.getColumn(0)

        return MinimizationResult(
            argument = xVector.map { it * invertCoefficient },
            result = function(order = 2, args = xVector).value,
            iterations = iterations,
            functionsCall = iterations
        ).also { onStep(it) }
    }

    private fun makePositiveDefinite(hessian: RealMatrix): RealMatrix {
        fun isPositiveDefinite(hessian: RealMatrix): Boolean =
            LUDecomposition(hessian).determinant > eps && hessian.norm >= 0

        if (isPositiveDefinite(hessian)) {
            return hessian
        }

        var hessianCopy = hessian.copy()
        var lambda = 1e-3
        do {
            val identity = createRealIdentityMatrix(hessian.rowDimension) * lambda
            hessianCopy += identity
            lambda *= 2
        } while (!isPositiveDefinite(hessianCopy))

        return hessianCopy
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
