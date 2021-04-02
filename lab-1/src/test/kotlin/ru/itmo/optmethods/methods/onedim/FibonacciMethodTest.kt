package ru.itmo.optmethods.methods.onedim

class FibonacciMethodTest : OneDimBaseTest(FibonacciMethod()) {
    override fun expectedFunctionCallCount(iterations: Int): Int = iterations
}
