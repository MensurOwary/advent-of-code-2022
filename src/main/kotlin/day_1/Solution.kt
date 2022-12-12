package day_1

import java.nio.file.Files
import java.nio.file.Path

fun elfWithMostCalories(allCaloriesByElfId: List<Pair<Int, Int>>) {
    val bestElf = allCaloriesByElfId.maxBy { it.second }
    println("==============Elf with Most Calories=================")
    println("Elf ${bestElf.first} had ${bestElf.second} calories")
}

fun topThreeElvesWithMostCalories(allCaloriesByElfId: List<Pair<Int, Int>>) {
    val bestElves = allCaloriesByElfId
        .sortedBy { it.second }
        .takeLast(3)

    val totalCalories = bestElves.sumOf { it.second }
    println("==============Top 3 Elves with Most Calories=================")
    println("Total calories are $totalCalories")
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_1/input")
    val allBlocks = Files.readString(inputFile).split(Regex("\\n\\n"))

    val allCaloriesByElfId = allBlocks.map { it.lines() }
        .map { lines -> lines.map { Integer.parseInt(it) } }
        .mapIndexed { elfId, calories -> Pair(elfId, calories.sum()) }

    // task 1
    elfWithMostCalories(allCaloriesByElfId)
    // task 2
    topThreeElvesWithMostCalories(allCaloriesByElfId)
}