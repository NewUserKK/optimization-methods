package ru.itmo.optmethods.methods.onedim

class GoldenRatioMethodTest : OneDimBaseTest(GoldenRatioMethod()) {
    override fun expectedFunctionCallCount(iterations: Int): Int = iterations + 2
}
