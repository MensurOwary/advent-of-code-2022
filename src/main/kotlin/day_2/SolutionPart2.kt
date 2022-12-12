package day_2

import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Path

private enum class Outcome {
    WIN, DRAW, LOSE
}

private data class Turn(val opponent: Move, val expectedOutcome: Outcome) {

    fun score() =
        when(expectedOutcome) {
            Outcome.WIN -> opponent.kryptonite().score + 6
            Outcome.DRAW -> opponent.score + 3
            Outcome.LOSE -> opponent.defeats().score
        }
}

private fun createMove(str: String) =
    when (str) {
        "A" -> Move.Rock
        "B" -> Move.Paper
        "C" -> Move.Scissors
        else -> {
            throw RuntimeException("Unsupported move $str")
        }
    }

private fun createOutcome(str: String) =
    when (str) {
        "X" -> Outcome.LOSE
        "Y" -> Outcome.DRAW
        "Z" -> Outcome.WIN
        else -> {
            throw RuntimeException("Unsupported outcome $str")
        }
    }

private fun String.toTurn(): Turn {
    val pieces = this.split(" ")
    val opponent = createMove(pieces[0])
    val expectedOutcome = createOutcome(pieces[1])
    return Turn(opponent, expectedOutcome)
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_2/input")
    val lines = Files.readAllLines(inputFile)

    val score = lines.map { it.toTurn() }.sumOf { it.score() }

    println("Score: $score")
}