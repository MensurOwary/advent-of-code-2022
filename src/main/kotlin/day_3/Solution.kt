package day_3

import java.nio.file.Files
import java.nio.file.Path

private data class CompleteRucksack(private val input: String) {
    val first = input.substring(0, input.length / 2).groupingBy { it }.eachCount()
    val second = input.substring(input.length / 2, input.length).groupingBy { it }.eachCount()

    fun commons(): Collection<Char> {
        val list = ArrayList<Char>()
        for (char in first.keys) {
            if (second.containsKey(char)) {
                list.add(char)
            }
        }
        return list
    }
}

fun priorities(it: Char) =
    if (it in 'a'..'z') {
        it - 'a' + 1
    } else {
        it - 'A' + 1 + 26
    }

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_3/input")
    val lines = Files.readAllLines(inputFile)

    val result = lines
        .map { CompleteRucksack(it) }
        .flatMap { it.commons() }.sumOf {
            priorities(it)
        }

    println("Result : $result")
}