package Year2025

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        var position = 50
        var result = 0
        input.forEach { line ->
            val direction = line.first()
            val num = line.drop(1).toInt()

            repeat(num) {
                if (direction == 'R') {
                    if (position < 99) {
                        position++
                    } else {
                        position = 0
                    }
                } else {
                    if (position > 0) {
                        position--
                    } else {
                        position = 99
                    }
                }
            }

            if (position == 0) {
                result++
            }
        }
        return result
    }

    fun part2(input: List<String>): Any {
        var position = 50
        var result = 0
        input.forEach { line ->
            val direction = line.first()
            val num = line.drop(1).toInt()

            repeat(num) {
                if (direction == 'R') {
                    if (position < 99) {
                        position++
                    } else {
                        position = 0
                    }
                } else {
                    if (position > 0) {
                        position--
                    } else {
                        position = 99
                    }
                }
                if (position == 0) {
                    result++
                }
            }

        }
        return result
    }

    val input = readInput("Year2025/Day01")
    val testInput = testInput(
        """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
        """.trimIndent()
    )

    // part 1
    expect(part1(testInput), 3)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput), 6)
    println("Part 2: ${part2(input)}")
}
