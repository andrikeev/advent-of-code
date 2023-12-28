package Year2021

import Point
import adjacentSides
import charGrid
import expect
import readInput
import testInput
import java.util.*

fun main() {

    fun findPath(
        start: Point,
        finish: Point,
        n: Int,
        m: Int,
        getRisk: (Point) -> Int,
    ): Int {
        val visited = mutableSetOf(start)
        val toVisit = PriorityQueue(compareBy(Pair<Point, Int>::second))
            .apply { add(start to 0) }
        while (true) {
            val (point, risk) = toVisit.poll()
            if (point == finish) {
                return risk
            }

            point
                .adjacentSides()
                .filterNot { it in visited }
                .filter { (i, j) -> i in 0 until n && j in 0 until m }
                .forEach {
                    visited.add(it)
                    toVisit.add(it to risk + getRisk(it))
                }
        }
    }

    fun part1(input: List<String>): Any {
        val (grid, n, m) = input.charGrid()
        return findPath(
            start = Point(0, 0),
            finish = Point(n - 1, m - 1),
            n = n,
            m = m,
            getRisk = { (i, j) ->grid[i][j].digitToInt() },
        )
    }

    fun part2(input: List<String>): Any {
        val (grid, n, m) = input.charGrid()
        return findPath(
            start = Point(0, 0),
            finish = Point(n * 5 - 1, m * 5 - 1),
            n = n * 5,
            m = m * 5,
            getRisk = { (i, j) ->
                var r = grid[i.mod(n)][j.mod(m)].digitToInt() + i.floorDiv(n) + j.floorDiv(m)
                while (r > 9) r -= 9
                r
            },
        )
    }

    val testInput = testInput("""
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """)
    val input = readInput("Year2021/Day15")

    // part 1
    expect(part1(testInput), 40)
    println(part1(input))

    // part 2
    expect(part2(testInput), 315)
    println(part2(input))
}
