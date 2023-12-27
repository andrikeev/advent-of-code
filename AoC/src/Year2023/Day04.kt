package Year2023

import expect
import readInput
import testInput
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

    fun part1(input: List<String>): Any {
        return input.sumOf { line -> 2.0.pow(cardPoints(line) - 1.0).toInt() }
    }

    fun part2(input: List<String>): Any {
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

    val testInput = testInput("""
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """)
    val input = readInput("Year2023/Day04")

    // part 1
    expect(part1(testInput), 13)
    println(part1(input))

    // part 2
    expect(part2(testInput), 30)
    println(part2(input))
}
