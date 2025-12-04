package Year2025

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        return input.sumOf { line ->
            buildString {
                val (i, c1) = line.dropLast(1).withIndex().maxBy(IndexedValue<Char>::value)
                append(c1)
                val c2 = line.drop(i + 1).max()
                append(c2)
            }.toInt()
        }
    }

    fun part2(input: List<String>): Any {
        return input.sumOf { line ->
            buildString {
                var i = -1
                while (length < 12) {
                    val (j, c) = line
                        .drop(i + 1)
                        .dropLast(12 - length - 1)
                        .withIndex()
                        .maxBy(IndexedValue<Char>::value)
                    append(c)
                    i += j + 1
                }
            }.toLong()
        }
    }

    val input = readInput("Year2025/Day03")
    val testInput = testInput(
        """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
        """.trimIndent()
    )

    // part 1
    expect(part1(testInput), 357)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput), 3121910778619L)
    println("Part 2: ${part2(input)}")
}
