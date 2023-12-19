package Year2021

import Point
import adjacent
import i
import intGrid
import j
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Int {
        val (grid, n, m) = input.intGrid()
        var res = 0

        fun Point.forEachAdjacent(block: (Point) -> Unit) {
            adjacent()
                .filter { (i, j) -> i in 0 until n && j in 0 until m }
                .forEach(block)
        }

        repeat(100) {
            val flashed = mutableSetOf<Point>()
            val toFlash = mutableListOf<Point>()

            fun energise(i: Int, j: Int) {
                grid[i][j]++
                if (grid[i][j] == 10) {
                    toFlash.add(i to j)
                }
            }

            for (i in 0 until n) {
                for (j in 0 until m) {
                    energise(i, j)
                }
            }

            while (toFlash.isNotEmpty()) {
                val p = toFlash.removeFirst()
                flashed.add(p)
                grid[p.i][p.j] = 0
                p.forEachAdjacent { (i, j) ->
                    if (i to j !in flashed) {
                        energise(i, j)
                    }
                }
            }
            res += flashed.size
        }

        return res
    }

    fun part2(input: List<String>): Int {
        val (grid, n, m) = input.intGrid()

        fun Point.forEachAdjacent(block: (Point) -> Unit) {
            adjacent()
                .filter { (i, j) -> i in 0 until n && j in 0 until m }
                .forEach(block)
        }

        var step = 0
        while(true) {
            step++
            val flashed = mutableSetOf<Point>()
            val toFlash = mutableListOf<Point>()

            fun energise(i: Int, j: Int) {
                grid[i][j]++
                if (grid[i][j] == 10) {
                    toFlash.add(i to j)
                }
            }

            for (i in 0 until n) {
                for (j in 0 until m) {
                    energise(i, j)
                }
            }

            while (toFlash.isNotEmpty()) {
                val p = toFlash.removeFirst()
                flashed.add(p)
                grid[p.i][p.j] = 0
                p.forEachAdjacent { (i, j) ->
                    if (i to j !in flashed) {
                        energise(i, j)
                    }
                }
            }
            if (flashed.size == n * m) {
                return step
            }
        }
    }

    val testInput = testInput("""
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """)
    check(part1(testInput).also { println("part1 test: $it") } == 1656)
    check(part2(testInput).also { println("part2 test: $it") } == 195)

    val input = readInput("Year2021/Day11")
    println(part1(input))
    println(part2(input))
}
