package day_5

import java.nio.file.Files
import java.nio.file.Path
import java.util.Stack

val MOVE_PATTERN_REGEX = Regex("move (\\d+) from (\\d+) to (\\d+)")

typealias Crate = String
typealias Crates = Stack<Crate>

fun createEachStack(stackStrings: List<String>, stackCount: Int): List<Crates> {
    val stacks = (0 until stackCount).map { Crates() }
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

fun simulate(
    lines: List<String>,
    moveAction: (stacks: List<Crates>, moves: List<Move>) -> List<Crates>
): String {
    // determine the number of stacks
    val dividerIndex = lines.indexOfFirst { it.isBlank() }
    val stackCount = lines[dividerIndex - 1].trim().split(Regex("\\s+")).last().toInt()
    // create each stack
    // parse moves
    val moves = parseMoves(lines.subList(dividerIndex + 1, lines.size))
    val stacks = createEachStack(lines.subList(0, stackCount), stackCount)
    val modifiedStacks = moveAction(stacks, moves)
    return modifiedStacks.joinToString(separator = "") {
        if (it.isEmpty()) "" else it.peek()
    }
}


data class Move(private val matches: List<Int>) {
    val from = matches[1] - 1 // adjust to the zero-based index
    val to = matches[2] - 1 // adjust to the zero-based index
    val count = matches[0]
}

fun parseMoves(subList: List<String>): List<Move> {
    return subList.map { moveString ->
        val matchResult = MOVE_PATTERN_REGEX.findAll(moveString).first()
        Move(matchResult.groupValues.drop(1).map { it.toInt() })
    }
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_5/input")
    val lines = Files.readAllLines(inputFile)

    // simulate
    val resultPart1 = simulate(lines) { stacks, moves ->
        for (move in moves) {
            for (i in 1..move.count) {
                stacks[move.to].push(stacks[move.from].pop())
            }
        }
        stacks
    }
    println("Result Part 1: $resultPart1")

    val resultPart2 = simulate(lines) { stacks, moves ->
        for (move in moves) {
            val tempStack = Crates()
            for (i in 1..move.count) {
                tempStack.push(stacks[move.from].pop())
            }

            while (!tempStack.isEmpty()) {
                stacks[move.to].push(tempStack.pop())
            }
        }
        stacks
    }
    println("Result Part 1: $resultPart2")
}


