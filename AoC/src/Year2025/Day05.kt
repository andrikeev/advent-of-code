package Year2025

import expect
import readInput
import split
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        val (first, second) = input.split(String::isEmpty)
        val ranges = first.map { it.split('-').map(String::toLong).let { (s, e) -> s..e } }
        return second
            .map(String::toLong)
            .count { n ->
                ranges.any { n in it }
            }
    }

    fun merge(r1: LongRange, r2: LongRange): LongRange {
        return if (r1.first in r2 || r1.last in r2 || r2.first in r1 || r2.last in r1) {
            minOf(r1.first, r2.first)..maxOf(r1.last, r2.last)
        } else {
            LongRange.EMPTY
        }
    }

    fun part2(input: List<String>): Any {
        val (first) = input.split(String::isEmpty)
        return first.map { it.split('-').map(String::toLong).let { (s, e) -> s..e } }
            .sortedBy(LongRange::first)
            .fold(listOf<LongRange>()) { acc, new ->
                buildList {
                    var flag = false
                    for (range in acc) {
                        val merged = merge(range, new)
                        if (merged.isEmpty()) {
                            add(range)
                        } else {
                            remove(range)
                            add(merged)
                            flag = true
                        }
                    }
                    if (!flag) {
                        add(new)
                    }
                }
            }
            .sumOf { it.last - it.first + 1 }
    }

    val input = readInput("Year2025/Day05")
    val testInput = testInput(
        """
            3-5
            10-14
            16-20
            12-18

            1
            5
            8
            11
            17
            32
        """.trimIndent()
    )

    // part 1
    expect(part1(testInput), 3)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput), 14L)
    println("Part 2: ${part2(input)}")
}
