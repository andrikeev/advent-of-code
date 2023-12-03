package Year2021.Day07

import java.io.File
import kotlin.math.abs

fun main() {
    val testInput = "16,1,2,0,4,2,7,1,2,14"
        .split(",")
        .map(String::toInt)
    assert(calculateFuel(testInput) == 37)

    val input = File("res/Year2021/Day07/input.txt")
        .readLines()
        .first()
        .split(",")
        .map(String::toInt)
    println(calculateFuel(input))
}

private fun calculateFuel(input: List<Int>): Int {
    return (0..(input.maxOrNull() ?: 0))
        .map { to ->
            input.groupBy { it }
                .map { entry -> abs(entry.key - to) * entry.value.size }
                .sum()
        }
        .minOrNull() ?: 0
}
