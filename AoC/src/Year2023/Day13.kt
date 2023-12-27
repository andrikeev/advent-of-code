package Year2023

import expect
import readInput
import testInput

fun main() {

    fun patterns(input: List<String>): List<List<String>> {
        return buildList<MutableList<String>> {
            add(mutableListOf())
            input.forEach {
                if (it.isNotEmpty()) {
                    last().add(it)
                } else {
                    add(mutableListOf())
                }
            }
        }
    }

    fun rows(input: List<String>): Set<Int> {
        return buildSet {
            val n = input.size
            for (k in 0 until n) {
                var b = true
                for (i in 0 until minOf(n - k, k)) {
                    if (input[k - i - 1] != input[k + i]) {
                        b = false
                    }
                }
                if (b) {
                    add(k)
                }
            }
        }
    }

    fun cols(input: List<String>) = buildSet {
        val n = input.size
        val m = input.first().length
        for (k in 0 until m) {
            var b = true
            for (i in 0 until n) {
                for (j in 0 until minOf(m - k, k)) {
                    if (input[i][k - j - 1] != input[i][j + k]) {
                        b = false
                    }
                }
            }
            if (b) {
                add(k)
            }
        }
    }

    fun part1(input: List<String>): Any {
        return patterns(input).sumOf { pattern ->
            rows(pattern).sumOf { it * 100 } + cols(pattern).sum()
        }
    }

    fun part2(input: List<String>): Any {
        fun List<String>.swap(i: Int, j: Int): List<String> {
            val list = Array(size) { get(it).toCharArray() }
            list[i][j] = if (list[i][j] == '.') '#' else '.'
            return list.map { it.concatToString() }
        }

        return patterns(input).sumOf { pattern ->
            val n = pattern.size
            val m = pattern.first().length

            var res = 0
            val rows = rows(pattern)
            val cols = cols(pattern)

            for (i in 0 until n) {
                if (res != 0) {
                    break
                }
                for (j in 0 until m) {
                    val swapped = pattern.swap(i, j)
                    val rowsDiff = rows(swapped).subtract(rows)
                    val colsDiff = cols(swapped).subtract(cols)
                    if (rowsDiff.isNotEmpty() || colsDiff.isNotEmpty()) {
                        res = rowsDiff.sumOf { it * 100 } + colsDiff.sum()
                        break
                    }
                }
            }

            res
        }
    }

    val testInput = testInput("""
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.

        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    """)
    val input = readInput("Year2023/Day13")

    // part 1
    expect(part1(testInput), 405)
    println(part1(input))

    // part 2
    expect(part2(testInput), 400)
    println(part2(input))
}
