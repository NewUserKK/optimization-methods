package ru.itmo.optmethods.methods.onedim

class LinearSearchMethodTest : OneDimBaseTest(LinearSearchMethod()) {
    override fun expectedFunctionCallCount(iterations: Int): Int = iterations
}
