package day_8

import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

sealed interface Range

data class RowRange(val range: IntRange, val col: Int) : Range
data class ColRange(val range: IntRange, val row: Int) : Range

typealias Matrix = List<List<Int>>

data class IndexPair(val row: Int, val col: Int, val len: Int) {
    private val up = RowRange(0 until row, col)
    private val down = RowRange(row + 1 until len, col)
    private val left = ColRange(0 until col, row)
    private val right = ColRange(col + 1 until len, row)

    fun getNeighbors(): List<Range> {
        return listOf(up, down, left, right)
    }
}

fun Int.isVisible(range: Range, matrix: List<List<Int>>): Boolean {
    val values = when (range) {
        is RowRange -> {
            sequence {
                range.range.map { yield(matrix[it][range.col]) }
            }
        }

        is ColRange -> {
            sequence {
                range.range.map { yield(matrix[range.row][it]) }
            }
        }
    }

    val max = values.maxOrNull() ?: Int.MIN_VALUE
    return max < this
}

fun Matrix.crossProductIndices(): List<IndexPair> {
    return indices.flatMap { row -> indices.map { col -> IndexPair(row, col, size) } }
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_8/input")
    val lines = Files.readAllLines(inputFile)

    val matrix = lines.map { it.chars().map { s -> s - 48 }.toList() } as Matrix

    val result = matrix.crossProductIndices()
        .count { pair ->
            val number = matrix[pair.row][pair.col]
            pair.getNeighbors().any { number.isVisible(it, matrix) }
        }

    println("Result: $result")
}