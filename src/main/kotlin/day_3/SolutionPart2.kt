package day_3

import java.nio.file.Files
import java.nio.file.Path

fun commonChar(input: List<String>): Char {
    return input.map { it.toSet() }.reduce { a, b -> a.intersect(b) }.first()
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_3/input")
    val rucksacksByElfGroups = Files.readAllLines(inputFile).chunked(3)

    val result = rucksacksByElfGroups.map { commonChar(it) }.sumOf { priorities(it) }
    println("Result: $result")
}