package Year2025

import LongGrid
import charGrid
import expect
import get
import readInput
import set
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        val (grid, n, m) = input.charGrid()
        var result = 0
        for (i in 0..<n - 1) {
            for (j in 0..<m) {
                when (grid[i][j]) {
                    'S' -> grid[i + 1][j] = '|'
                    '|' -> {
                        when (grid[i + 1][j]) {
                            '.' -> grid[i + 1][j] = '|'
                            '^' -> {
                                result++
                                listOf(
                                    (i + 1 to j - 1),
                                    (i + 1 to j + 1)
                                ).forEach { p ->
                                    grid[p] = '|'
                                }
                            }
                        }
                    }
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Any {
        val (grid, n, m) = input.charGrid()
        val paths = LongGrid(n, m)
        for (i in 0..<n - 1) {
            for (j in 0..<m) {
                when (grid[i][j]) {
                    'S' -> {
                        paths[i + 1][j] = 1L
                    }

                    '.' -> {
                        paths[i + 1][j] += paths[i][j]
                    }

                    '^' -> {
                        listOf(
                            (i + 1 to j - 1),
                            (i + 1 to j + 1)
                        ).forEach { p ->
                            paths[p] += paths[i][j]
                        }
                    }
                }
            }
        }
        return paths.last().sum()
    }

    val input = readInput("Year2025/Day07")
    val testInput = testInput(
        """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
        """.trimIndent()
    )

    // part 1
    expect(part1(testInput), 21)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput), 40L)
    println("Part 2: ${part2(input)}")
}
