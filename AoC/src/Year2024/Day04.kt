package Year2024

import Direction8
import Point
import charGrid
import expect
import i
import j
import moveTo
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        var result = 0
        val (grid, n, m) = input.charGrid()

        fun isXmas(p: Point, d: Direction8): Boolean {
            return if (grid[p.i][p.j] != 'X') {
                false
            } else {
                buildString {
                    append(grid[p.i][p.j])
                    var k = 1
                    var cur = p.moveTo(d)
                    while (k < 4 && cur.i in 0..<n && cur.j in 0..<m) {
                        append(grid[cur.i][cur.j])
                        k++
                        cur = cur.moveTo(d)
                    }
                }.also { println(it) } == "XMAS"
            }
        }

        for (i in 0 until n) {
            for (j in 0 until m) {
                result += Direction8
                    .entries
                    .count { isXmas(Point(i, j), it) }
            }
        }

        return result
    }

    fun part2(input: List<String>): Any {
        var result = 0
        val (grid, n, m) = input.charGrid()

        fun isMAS(word: String) = word == "MAS" || word == "SAM"

        for (i in 1 until n - 1) {
            for (j in 1 until m - 1) {
                if (
                    isMAS("${grid[i - 1][j - 1]}${grid[i][j]}${grid[i + 1][j + 1]}") &&
                    isMAS("${grid[i - 1][j + 1]}${grid[i][j]}${grid[i + 1][j - 1]}")
                ) {
                    result++
                }
            }
        }


        return result
    }

    val input = readInput("Year2024/Day04")

    // part 1
    expect(part1(testInput("""
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """)), 18)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput("""
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """)), 9)
    println("Part 2: ${part2(input)}")
}
