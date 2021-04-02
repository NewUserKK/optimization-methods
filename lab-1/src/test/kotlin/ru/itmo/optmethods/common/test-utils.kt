package ru.itmo.optmethods.common

fun matchAny(assertions: List<() -> Unit>) {
    var passed = false
    var lastAssertionError: AssertionError? = null
    assertions.forEach { assertion ->
        try {
            assertion()
            passed = true
        } catch (e: AssertionError) {
            lastAssertionError = e
        }
    }

    if (!passed) {
        throw AssertionError(
            "No matchers passed! See last assertion error below",
            lastAssertionError
        )
    }
}

fun matchAny(vararg assertions: () -> Unit) = matchAny(assertions.toList())
