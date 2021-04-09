package ru.itmo.optmethods.methods.simplex

import io.kotest.core.spec.style.FreeSpec
import ru.itmo.optmethods.common.Rational
import ru.itmo.optmethods.methods.common.shouldBeAround

class SimplexTest : FreeSpec(), LinearProgrammingTaskBuilder {

    init {
        test(
            "Task 1",
            task(
                func(-6, -1, -4, 5),
                constraint(3, 1, -1, 1) eq 4,
                constraint(5, 1, 1, -1) eq 4
            ),
            basis(1, 0, 0, 1),
            -4.0
        )

        test(
            "Task 2",
            task(
                func(-1, -2, -3, 1),
                constraint(1, -3, -1, -2) eq -4,
                constraint(1, -1, 1, 0) eq 0
            ),
            basis(0, 1, 1, 0),
            -6.0
        )

        test(
            "Task 3",
            task(
                func(-1, -2, -1, 3, -1),
                constraint(1, 1, 0, 2, 1) eq 5,
                constraint(1, 1, 1, 3, 2) eq 9,
                constraint(0, 1, 1, 2, 1) eq 6,
            ),
            basis(0, 0, 1, 2, 1),
            -11.0
        )

        test(
            "Task 4",
            task(
                func(-1, -1, -1, 1, -1),
                constraint(1, 1, 2, 0, 0) eq 4,
                constraint(0, -2, -2, 1, -1) eq -6,
                constraint(1, -1, 6, 1, 1) eq 12,
            ),
            basis(1, 1, 2, 0, 0),
            -10.0
        )

        test(
            "Task 5",
            task(
                func(-1, 4, -3, 10),
                constraint(1, 1, -1, -10) eq 0,
                constraint(1, 14, 10, -10) eq 11
            ),
            null,
            -4.0
        )

        test(
            "Task 6",
            task(
                func(-1, 5, 1, -1),
                constraint(1, 3, 3, 1) le 3,
                constraint(2, 0, 3, -1) le 4
            ),
            null,
            -3.0
        )

        test(
            "Task 7",
            task(
                func(-1, -1, 1, -1, 2),
                constraint(3, 1, 1, 1, -2) eq 10,
                constraint(6, 1, 2, 3, -4) eq 20,
                constraint(10, 1, 3, 6, -7) eq 30
            ),
            null,
            10.0
        )
    }

    private fun test(
        title: String,
        task: LinearProgrammingTask,
        basis: Array<Rational>?,
        expected: Rational
    ) {
        if (basis != null) {
            "$title with basis solution" {
                SimplexMethod.findExtremum(
                    task,
                    basis,
                    searchForMin = true
                ).value shouldBeAround expected
            }
        }

        "$title without basis solution" {
            val result = SimplexMethod.findExtremum(
                task,
                null,
                searchForMin = true
            )

            println("Value: " + result.value)
            println("Point: [${result.point.joinToString()}]")
            println("Basis: [${result.basis.joinToString()}]")

            result.value shouldBeAround expected
        }
    }
}
