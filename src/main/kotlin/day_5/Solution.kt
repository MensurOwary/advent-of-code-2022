package day_5

import java.nio.file.Files
import java.nio.file.Path
import java.util.Stack

val MOVE_PATTERN_REGEX = Regex("move (\\d+) from (\\d+) to (\\d+)")

fun createEachStack(stackStrings: List<String>, stackCount: Int): List<Stack<String>> {
    val stacks = IntRange(0, stackCount - 1).map { Stack<String>() }
    for (line in stackStrings.reversed()) {
        for (i in 0 until stackCount) {
            try {
                val start = i * 4
                val end = start + 3
                val value = line.substring(start, end)
                if (value.isNotBlank()) {
                    stacks[i].push(value.replace("[", "").replace("]", ""))
                }
            } catch (ex: StringIndexOutOfBoundsException) {
                continue
            }
        }
    }
    return stacks
}

fun simulatePartOne(stacks: List<Stack<String>>, moves: List<Move>) {
    for (move in moves) {
        for (i in 1..move.count) {
            stacks[move.to].push(stacks[move.from].pop())
        }
    }

    val result = stacks.joinToString(separator = ""){
        if (it.isEmpty()) "" else it.peek()
    }

    println("Part 1 Result: $result")
}

fun simulatePartTwo(stacks: List<Stack<String>>, moves: List<Move>) {
    for (move in moves) {
        val tempStack = Stack<String>()
        for (i in 1..move.count) {
            tempStack.push(stacks[move.from].pop())
        }

        while (!tempStack.isEmpty()) {
            stacks[move.to].push(tempStack.pop())
        }
    }

    val result = stacks.joinToString(separator = ""){
        if (it.isEmpty()) "" else it.peek()
    }

    println("Part 2 Result: $result")
}

data class Move(private val matches: List<Int>) {
    val from = matches[1] - 1 // adjust to the zero-based index
    val to = matches[2] - 1 // adjust to the zero-based index
    val count = matches[0]
}

fun parseMoves(subList: List<String>): List<Move> {
    return subList.map {moveString ->
        val matchResult = MOVE_PATTERN_REGEX.findAll(moveString).first()
        Move(matchResult.groupValues.drop(1).map { it.toInt() })
    }
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_5/input")
    val lines = Files.readAllLines(inputFile)

    // determine the number of stacks
    val dividerIndex = lines.indexOfFirst { it.isBlank() }
    val stackCount = lines[dividerIndex - 1].trim().split(Regex("\\s+")).last().toInt()
    // create each stack
    // parse moves
    val moves = parseMoves(lines.subList(dividerIndex + 1, lines.size))
    // simulate
    simulatePartOne(createEachStack(lines.subList(0, stackCount), stackCount), moves)
    simulatePartTwo(createEachStack(lines.subList(0, stackCount), stackCount), moves)
}


