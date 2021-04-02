package ru.itmo.optmethods.plot

fun main() {
    plot(show = true) {
        points {
            add(listOf(1, 2, 3, 4, 5, 6, 8), listOf(1, 2, 6, 3, 1, 4, 7))
        }
    }
}
