package day_9

import java.nio.file.Files
import java.nio.file.Path

enum class Direction {
    R, L, D, U
}

data class Order(val direction: Direction, val amount: Int)

class Knot(val x: Int, val y: Int) {

    override fun hashCode() = 31 * 31 * 7 + x + y

    override fun equals(other: Any?): Boolean {
        if (other !is Knot) return false
        return x == other.x && y == other.y
    }

    fun advance(direction: Direction): Knot {
        val x = this.x
        val y = this.y

        return when (direction) {
            Direction.D -> Knot(x, y - 1)
            Direction.U -> Knot(x, y + 1)
            Direction.L -> Knot(x - 1, y)
            Direction.R -> Knot(x + 1, y)
        }
    }

    fun save() = Knot(x, y)
}

enum class Distance {
    FAR, CLOSE
}

fun String.toOrder(): Order {
    val pieces = this.trim().split(" ")
    return Order(
        direction = Direction.valueOf(pieces.first()),
        amount = pieces.last().toInt()
    )
}

fun Knot.distanceTo(other: Knot): Distance {
    val x1 = this.x
    val y1 = this.y

    val x2 = other.x
    val y2 = other.y

    val squared = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)
    return if (squared <= 2) Distance.CLOSE else Distance.FAR
}

fun main() {
    val inputFile = Path.of("src/main/kotlin/day_9/input")
    val lines = Files.readAllLines(inputFile)

    val orders = lines.map { it.toOrder() }

    var currentHead = Knot(0, 0)
    var tail = Knot(0, 0)

    val set = hashSetOf(tail);

    for (order in orders) {
        for (i in 0 until order.amount) {
            val old = currentHead.save()
            currentHead = currentHead.advance(order.direction)
            if (currentHead.distanceTo(tail) == Distance.FAR) {
                tail = old
            }
            set.add(tail)
        }
    }
    println(set.size)
}


