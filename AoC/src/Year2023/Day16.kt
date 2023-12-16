package Year2023

import CharGrid
import Direction
import Point
import Position
import charGrid
import gridSize
import i
import j
import moveTo
import point
import readInput
import testInput

fun main() {

    fun count(grid: CharGrid, point: Point, direction: Direction): Int {
        val (n, m) = grid.gridSize()
        val visited = mutableSetOf<Position>()
        val toVisit = mutableListOf(point to direction)

        while (toVisit.isNotEmpty()) {
            val (p, d) = toVisit.removeFirst()
            visited.add(p to d)
            val c = grid[p.i][p.j]

            fun toVisit(vararg d: Direction) {
                d.forEach {
                    val next = p moveTo it
                    if (
                        next.i in 0 until n &&
                        next.j in 0 until m &&
                        next to it !in visited
                    ) {
                        toVisit.add(next to it)
                    }
                }
            }

            when (c) {
                '-' -> when (d) {
                    Direction.Left,
                    Direction.Right -> toVisit(d)
                    Direction.Up,
                    Direction.Down -> toVisit(Direction.Left, Direction.Right)
                }

                '|' -> when (d) {
                    Direction.Left,
                    Direction.Right -> toVisit(Direction.Up, Direction.Down)
                    Direction.Up,
                    Direction.Down -> toVisit(d)
                }

                '\\' -> when (d) {
                    Direction.Left -> toVisit(Direction.Up)
                    Direction.Up -> toVisit(Direction.Left)
                    Direction.Right -> toVisit(Direction.Down)
                    Direction.Down -> toVisit(Direction.Right)
                }

                '/' -> when (d) {
                    Direction.Left -> toVisit(Direction.Down)
                    Direction.Up -> toVisit(Direction.Right)
                    Direction.Right -> toVisit(Direction.Up)
                    Direction.Down -> toVisit(Direction.Left)
                }

                else -> toVisit(d)
            }
        }

        return visited.distinctBy(Position::point).size
    }

    fun part1(input: List<String>): Int {
        return count(
            input.charGrid().first,
            Point(0, 0),
            Direction.Right,
        )
    }

    fun part2(input: List<String>): Int {
        val (grid, n, m) = input.charGrid()
        var res = 0

        for (i in 0 until n) {
            res = maxOf(res, count(grid, Point(i, 0), Direction.Right))
            res = maxOf(res, count(grid, Point(i, m - 1), Direction.Left))
        }

        for (j in 0 until m) {
            res = maxOf(res, count(grid, Point(0, j), Direction.Down))
            res = maxOf(res, count(grid, Point(n - 1, j), Direction.Up))
        }

        return res
    }

    val testInput = testInput("""
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """)
    check(part1(testInput).also { println("part1 test: $it") } == 46)
    check(part2(testInput).also { println("part2 test: $it") } == 51)

    val input = readInput("Year2023/Day16")
    println(part1(input))
    println(part2(input))
}
