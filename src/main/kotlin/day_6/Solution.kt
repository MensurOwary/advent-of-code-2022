package day_6

import java.nio.file.Files
import java.nio.file.Path

fun findStartOfMarker(completeString: String, markerSize: Int): Int {
    val stopIndex = completeString.length - markerSize + 1;
    val result = (0 until stopIndex)
        .map { Pair(completeString.substring(it, it + markerSize), it + markerSize) }
        .first { it.first.toSet().size == it.first.length }
    return result.second;
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_6/input")
    val completeString = Files.readString(inputFile).trim()

    val startOfPacket = findStartOfMarker(completeString, 4)
    println("Start of the packet: $startOfPacket")

    val startOfMessage = findStartOfMarker(completeString, 14);
    println("Start of the message: $startOfMessage")
}