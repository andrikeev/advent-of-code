package Year2023

import Point
import i
import j
import moveBy
import readInput
import testInput

fun main() {

    infix fun Point.moveTo(direction: Direction): Point {
        return when (direction) {
            Direction.Left -> moveBy(j = -1)
            Direction.Up -> moveBy(i = -1)
            Direction.Right -> moveBy(j = +1)
            Direction.Down -> moveBy(i = +1)
        }
    }

    fun count(array: Array<CharArray>, point: Point, direction: Direction): Int {
        val n = array.size
        val m = array.first().size
        val visited = mutableSetOf<Pair<Point, Direction>>()
        val toVisit = mutableListOf(point to direction)

        while (toVisit.isNotEmpty()) {
            val (p, d) = toVisit.removeFirst()
            visited.add(p to d)
            val c = array[p.i][p.j]

            fun toVisit(p: Point, d: Direction) {
                val next = p moveTo d
                if (
                    next.i in 0 until n &&
                    next.j in 0 until m &&
                    next to d !in visited
                ) {
                    toVisit.add(next to d)
                }
            }

            when (c) {
                '-' -> {
                    when (d) {
                        Direction.Left,
                        Direction.Right -> toVisit(p, d)

                        Direction.Up,
                        Direction.Down -> {
                            toVisit(p, Direction.Left)
                            toVisit(p, Direction.Right)
                        }
                    }
                }

                '|' -> {
                    when (d) {
                        Direction.Left,
                        Direction.Right -> {
                            toVisit(p, Direction.Up)
                            toVisit(p, Direction.Down)
                        }

                        Direction.Up,
                        Direction.Down -> toVisit(p, d)
                    }
                }

                '\\' -> {
                    when (d) {
                        Direction.Left -> toVisit(p, Direction.Up)
                        Direction.Up -> toVisit(p, Direction.Left)
                        Direction.Right -> toVisit(p, Direction.Down)
                        Direction.Down -> toVisit(p, Direction.Right)
                    }
                }

                '/' -> {
                    when (d) {
                        Direction.Left -> toVisit(p, Direction.Down)
                        Direction.Up -> toVisit(p, Direction.Right)
                        Direction.Right -> toVisit(p, Direction.Up)
                        Direction.Down -> toVisit(p, Direction.Left)
                    }
                }

                else -> {
                    toVisit(p, d)
                }
            }
        }

        return visited.distinctBy(Pair<Point, Direction>::first).size
    }

    fun part1(input: List<String>): Int {
        return count(
            Array(input.size) { input[it].toCharArray() },
            Point(0, 0),
            Direction.Right,
        )
    }

    fun part2(input: List<String>): Int {
        val array = Array(input.size) { input[it].toCharArray() }
        val n = array.size
        val m = array.first().size
        var res = 0

        for (i in 0 until n) {
            res = maxOf(res, count(array, Point(i, 0), Direction.Right))
            res = maxOf(res, count(array, Point(i, m - 1), Direction.Left))
        }

        for (j in 0 until m) {
            res = maxOf(res, count(array, Point(0, j), Direction.Down))
            res = maxOf(res, count(array, Point(n - 1, j), Direction.Up))
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

enum class Direction { Left, Up, Right, Down; }
