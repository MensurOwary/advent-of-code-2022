package day_2

import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Path

enum class Move(val score: Int) {
    Rock(1), Paper(2), Scissors(3);

    fun defeats(other: Move): Boolean {
        if (this == Paper && other == Rock) return true
        if (this == Rock && other == Scissors) return true
        return this == Scissors && other == Paper
    }
}

data class Event(val opponent: Move, val you: Move) {

    fun score(): Int {
        if (opponent.defeats(you)) {
            return you.score
        } else if (you.defeats(opponent)) {
            return you.score + 6;
        }
        return you.score + 3
    }
}

fun createMove(str: String) =
    when (str) {
        "A", "X" -> Move.Rock
        "B", "Y" -> Move.Paper
        "C", "Z" -> Move.Scissors
        else -> {
            throw RuntimeException("Unsupported move $str")
        }
    }

fun String.toEvent(): Event {
    val pieces = this.split(" ")
    val opponent = createMove(pieces[0])
    val you = createMove(pieces[1])
    return Event(opponent, you)
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_2/input")
    val lines = Files.readAllLines(inputFile)

    val score = lines.map { it.toEvent() }.sumOf { it.score() }

    println("Score: $score")
}