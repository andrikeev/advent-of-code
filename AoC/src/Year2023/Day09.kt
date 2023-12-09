package Year2023

import readInput
import testInput
import java.util.Currency

fun main() {

    fun extrapolate(input: String): Int {
        val initial = input.split(" ").map { it.toInt() }
        val history = mutableListOf<MutableList<Int>>()
        history.add(initial.toMutableList())

        var current = mutableListOf<Int>()
        current.addAll(initial)

        while (current.any { it != 0 }) {
            val new = mutableListOf<Int>()
            current.forEachIndexed { i, x ->
                if (i < current.lastIndex) {
                    new.add(current[i + 1] - current[i])
                }
            }
            history.add(new)
            current = new
        }

        history.last().add(0)

        for (i in (history.lastIndex - 1)downTo 0) {
            val row = history[i]
            row.add(row.last() + history[i + 1].last())
        }

        return history.first().last()
    }

    fun extrapolate2(input: String): Int {
        val initial = input.split(" ").map { it.toInt() }
        val history = mutableListOf<MutableList<Int>>()
        history.add(initial.toMutableList())

        var current = mutableListOf<Int>()
        current.addAll(initial)

        while (current.any { it != 0 }) {
            val new = mutableListOf<Int>()
            current.forEachIndexed { i, x ->
                if (i < current.lastIndex) {
                    new.add(current[i + 1] - current[i])
                }
            }
            history.add(new)
            current = new
        }

        history.last().add(0, 0)

        for (i in (history.lastIndex - 1)downTo 0) {
            val row = history[i]
            row.add(0, row.first() - history[i + 1].first())
        }

        return history.first().first()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { extrapolate(it) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { extrapolate2(it) }
    }

    val testInput = testInput("""
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent()
    )
    check(part1(testInput).also { println("part1 test: $it") } == 114)
    check(part2(testInput).also { println("part2 test: $it") } == 2)

    val input = readInput("Year2023/Day09")
    println(part1(input))
    println(part2(input))
}
