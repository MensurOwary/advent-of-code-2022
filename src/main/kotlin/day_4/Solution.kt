package day_4

import java.nio.file.Files
import java.nio.file.Path

data class RangePair(val first: Range, val second: Range) {
    fun fullyContains(): Boolean {
        val longer = if (first.length >= second.length) first else second
        val shorter = if (first.length >= second.length) second else first
        return (longer.start <= shorter.start) && (longer.end >= shorter.end)
    }

    fun overlapping(): Boolean {
        val left = if (first.start <= second.start) first else second
        val right = if (first.start <= second.start) second else first
        return left.end >= right.start
    }
}

data class Range(val start: Int, val end: Int) {
    val length = end - start + 1
}

fun String.toRange(): Range {
    val pieces = this.split("-")
    return Range(
        start = Integer.parseInt(pieces[0]),
        end = Integer.parseInt(pieces[1])
    )
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_4/input")
    val lines = Files.readAllLines(inputFile)

    val result = lines
        .map { it.split(",") }
        .map {
            RangePair(
                it[0].toRange(),
                it[1].toRange()
            )
        }.count { it.fullyContains() }

    println("Result: $result")
}