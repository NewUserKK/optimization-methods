package common

import methods.Rational
import java.io.File
import java.io.FileWriter

typealias Matrix = Array<Array<Rational>>

inline fun avg(x1: Rational, x2: Rational) = (x1 + x2) / 2

operator fun List<Rational>.minus(another: List<Rational>): List<Rational> {
    assert(this.size == another.size)
    return this.zip(another).map { it.first - it.second }
}

operator fun List<Rational>.times(another: List<Rational>): List<Rational> {
    assert(this.size == another.size)
    return this.zip(another).map { it.first * it.second }
}

operator fun List<Rational>.times(another: Rational): List<Rational> {
    return this.map { it * another }
}

fun saveToCSV(name: String, str: String) {
    val file = File("lab-1/res/${name}.csv").also {
        it.parentFile.mkdirs()
        it.createNewFile()
    }
    FileWriter(file).use {
        it.write(str)
    }
}
