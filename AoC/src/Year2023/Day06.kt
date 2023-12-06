package Year2023

import readInput
import testInput
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {

    fun part1(input: List<String>): Int {
        val (times, distances) = input.map { line ->
            line.substringAfter(":").split(" ").filter(String::isNotBlank).map(String::toInt)
        }

        return List(times.size) { i ->
            val t = times[i]
            val d = distances[i]
            (0..t).count { it * (t - it) > d }
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val (time, distance) = input.map { line ->
            line.substringAfter(":").replace(" ", "").toLong()
        }
        return sqrt(time * time - 4f * distance).roundToInt() - 1
    }

    val testInput = testInput("""
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent())
    check(part1(testInput).also { println("part1 test: $it") } == 288)
    check(part2(testInput).also { println("part2 test: $it") } == 71503)

    val input = readInput("Year2023/Day06")
    println(part1(input))
    println(part2(input))
}
