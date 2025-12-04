package Year2025

import CharGrid
import Direction8
import Point
import charGrid
import expect
import forEach
import get
import inside
import moveTo
import readInput
import set
import testInput

fun main() {

    fun check(grid: CharGrid, p: Point): Boolean {
        return Direction8.entries.count { dir ->
            val n = p.moveTo(dir)
            (n inside grid) && (grid[n] == '@')
        } < 4
    }

    fun part1(input: List<String>): Any {
        val (grid) = input.charGrid()
        var res = 0
        grid.forEach { c, p ->
            if (c == '@' && check(grid, p)) {
                res++
            }
        }
        return res
    }

    fun part2(input: List<String>): Any {
        val (grid) = input.charGrid()
        var res = 0
        var flag = true
        while (flag) {
            flag = false
            grid.forEach { c, p ->
                if (c == '@' && check(grid, p)) {
                    res++
                    grid[p] = '.'
                    flag = true
                }
            }
        }
        return res
    }

    val input = readInput("Year2025/Day04")
    val testInput = testInput(
        """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
        """.trimIndent()
    )

    // part 1
    expect(part1(testInput), 13)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput), 43)
    println("Part 2: ${part2(input)}")
}
