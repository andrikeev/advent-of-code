package Year2024

import Day
import Direction
import Point
import Position
import charGrid
import direction
import firstOrThrow
import forEach
import get
import inside
import moveTo
import point
import turnRight

private object Day06 : Day {
    override fun part1(input: List<String>): Any {
        val (grid) = input.charGrid()
        val start = grid.firstOrThrow { c, _, _ -> c == '^' }
            .let { (_, i, j) -> Position(Point(i, j), Direction.Up) }

        var p = start
        val visited = mutableSetOf(p.point)
        while (true) {
            val (point, direction) = p
            val next = point moveTo direction
            if (next inside grid) {
                if (grid[next] == '#') {
                    val newDirection = p.direction.turnRight()
                    p = Position(point, newDirection)
                } else {
                    visited.add(next)
                    p = Position(next, direction)
                }
            } else {
                break
            }
        }

        return visited.size
    }

    override fun part2(input: List<String>): Any {
        val (grid) = input.charGrid()
        val start = grid.firstOrThrow { c, _, _ -> c == '^' }
            .let { (_, i, j) -> Position(Point(i, j), Direction.Up) }

        fun isLoop(obstacle: Point): Boolean {
            var p = start
            val visited = mutableSetOf(p)
            while (true) {
                val (point, direction) = p
                val next = point moveTo direction
                if (next inside grid) {
                    if (next == obstacle || grid[next] == '#') {
                        val newDirection = p.direction.turnRight()
                        p = Position(point, newDirection)
                    } else {
                        p = Position(next, direction)
                        if (!visited.add(p)) {
                            return true
                        }
                    }
                } else {
                    return false
                }
            }
        }

        var result = 0
        grid.forEach { c, i, j ->
            if (c != '#' && isLoop(Point(i, j))) {
                result++
            }
        }
        return result
    }
}

fun main() = with(Day06) {
    test1(
        input = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
        """.trimIndent(),
        expected = 41,
    )
    result1()

    test2(
        input = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
        """.trimIndent(),
        expected = 6,
    )
    result2()
}
