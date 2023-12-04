package Year2023

import readInput
import kotlin.math.pow

fun main() {

    fun parseCards(input: String): Pair<List<String>, List<String>> {
        return input.substringAfter(": ")
            .split(" | ")
            .let { (first, second) ->
                first.split(" ").filter(String::isNotBlank) to
                        second.split(" ").filter(String::isNotBlank)
            }
    }

    fun cardPoints(input: String): Int {
        val (win, my) = parseCards(input)
        return win.intersect(my.toSet()).size
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line -> 2.0.pow(cardPoints(line) - 1.0).toInt() }
    }

    fun part2(input: List<String>): Int {
        val copies = mutableMapOf<Int, Int>()
        input.forEachIndexed { i, line ->
            copies[i] = copies.getOrDefault(i, 0) + 1
            val points = cardPoints(line)
            repeat(points) {
                val j = i + it + 1
                copies[j] = copies.getOrDefault(j, 0) + copies.getValue(i)
            }
        }
        return copies.values.sum()
    }

    val testInput = readInput("Year2023/Day04_test")
    check(part1(testInput).also { println("part1 test:$it") } == 13)
    check(part2(testInput).also { println("part2 test: $it") } == 30)

    val input = readInput("Year2023/Day04")
    println(part1(input))
    println(part2(input))
}
