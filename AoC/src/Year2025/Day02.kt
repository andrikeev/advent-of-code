package Year2025

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        return input.first()
            .split(",")
            .sumOf { range ->
                range.split("-")
                    .map { it.toLong() }
                    .let { (s, e) -> LongRange(s, e) }
                    .sumOf { n ->
                        val s = n.toString()
                        if (s.take(s.length / 2) == s.drop(s.length / 2)) {
                            return@sumOf n
                        }
                        0
                    }
            }
    }

    fun part2(input: List<String>): Any {
        return input.first()
            .split(",")
            .sumOf { range ->
                range.split("-")
                    .map { it.toLong() }
                    .let { (s, e) -> LongRange(s, e) }
                    .sumOf { n ->
                        val s = n.toString()
                        for (i in 1..s.length) {
                            val s2 = s.chunked(i)
                            if (s2.size > 1 && s2.toSet().size == 1) {
                                return@sumOf n
                            }
                        }
                        0
                    }
            }
    }

    val input = readInput("Year2025/Day02")
    val testInput = testInput(
        "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528," +
            "446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"
    )

    // part 1
    expect(part1(testInput), 1227775554L)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput), 4174379265)
    println("Part 2: ${part2(input)}")
}
