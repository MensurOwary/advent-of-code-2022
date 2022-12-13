package day_4

import java.nio.file.Files
import java.nio.file.Path

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
        }.count { it.overlapping() }

    println("Result: $result")
}