package ru.itmo.optmethods.methods.onedim

class DichotomyMethodTest : OneDimBaseTest(DichotomyMethod()) {
    override fun expectedFunctionCallCount(iterations: Int): Int = iterations * 2 + 1
}
