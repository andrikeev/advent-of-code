package Year2023

import expect
import readInput
import testInput
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {

    fun part1(input: List<String>): Any {
        val (times, distances) = input.map { line ->
            line.substringAfter(":").split(" ").filter(String::isNotBlank).map(String::toInt)
        }

        return List(times.size) { i ->
            val t = times[i]
            val d = distances[i]
            (0..t).count { it * (t - it) > d }
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Any {
        val (time, distance) = input.map { line ->
            line.substringAfter(":").replace(" ", "").toLong()
        }
        return sqrt(time * time - 4f * distance).roundToInt() - 1
    }

    val testInput = testInput("""
        Time:      7  15   30
        Distance:  9  40  200
    """)
    val input = readInput("Year2023/Day06")

    // part 1
    expect(part1(testInput), 288)
    println(part1(input))

    // part 2
    expect(part2(testInput), 71503)
    println(part2(input))
}
