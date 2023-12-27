package Year2021

import CharGrid
import Point
import expect
import gridSize
import i
import j
import readInput
import testInput

private enum class Axis { Hor, Ver }

fun main() {

    fun parseGrid(input: List<String>): CharGrid {
        val dots = input.map { line ->
            val (i, j) = line.split(',').map(String::toInt)
            Point(i, j)
        }.toSet()
        val n = dots.maxOf { it.j } + 1
        val m = dots.maxOf { it.i } + 1
        return CharGrid(n) { i -> CharArray(m) { j -> if (j to i in dots) '#' else ' ' } }
    }

    fun parseFold(input: String): Pair<Axis, Int> {
        val (axis, num) = input.split('=')
        return (if (axis.contains('y')) Axis.Hor else Axis.Ver) to num.toInt()
    }

    fun CharGrid.fold(axis: Axis, l: Int): CharGrid {
        val (n, m) = gridSize()
        return when (axis) {
            Axis.Hor -> {
                val k = 2 * l - n
                CharGrid(l) { i ->
                    CharArray(m) { j ->
                        if (this[i][j] == '#' || ((i > k) && this[n + k - i][j] == '#')) '#' else ' '
                    }
                }
            }

            Axis.Ver -> {
                val k = 2 * l - m
                CharGrid(n) { i ->
                    CharArray(l) { j ->
                        if (this[i][j] == '#' || ((j > k) && this[i][m + k - j] == '#')) '#' else ' '
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Any {
        var grid = parseGrid(input.takeWhile(String::isNotEmpty))
        val firstFold = input.dropWhile(String::isNotEmpty).drop(1).first()
        val (axis, num) = parseFold(firstFold)
        grid = grid.fold(axis, num)
        return grid.sumOf { row -> row.count { c -> c == '#' } }
    }

    fun part2(input: List<String>) {
        var grid = parseGrid(input.takeWhile(String::isNotEmpty))
        input.dropWhile(String::isNotEmpty).drop(1).forEach { line ->
            val (axis, num) = parseFold(line)
            grid = grid.fold(axis, num)
        }
        grid.forEach { row -> println(row.joinToString(separator = "")) }
    }

    val testInput = testInput("""
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0

        fold along y=7
        fold along x=5
    """)
    val input = readInput("Year2021/Day13")

    // part 1
    expect(part1(testInput), 17)
    println(part1(input))

    // part 2
    part2(input)
}
