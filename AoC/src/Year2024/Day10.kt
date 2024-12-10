package Year2024

import Day
import Point
import adjacentSides
import forEach
import get
import inside
import intGrid

private object Day10 : Day {
    override fun part1(input: List<String>): Any {
        val (grid) = input.intGrid()

        fun trailheads(start: Point): Int {
            val toVisit = mutableListOf(start)
            val visited = mutableSetOf(start)
            val finishes = mutableSetOf<Point>()

            while (toVisit.isNotEmpty()) {
                val point = toVisit.removeFirst()
                visited.add(point)
                if (grid[point] == 9) {
                    finishes.add(point)
                } else {
                    point.adjacentSides().forEach { next ->
                        if (next inside grid && grid[next] - grid[point] == 1 && next !in visited) {
                            toVisit.add(next)
                        }
                    }
                }
            }

            return finishes.size
        }

        var result = 0
        grid.forEach { v, i, j ->
            if (v == 0) {
                result += trailheads(Point(i, j))
            }
        }
        return result
    }

    override fun part2(input: List<String>): Any {
        val (grid) = input.intGrid()

        fun trailheads(start: Point): Int {
            val toVisit = mutableListOf(listOf(start))
            val visited = mutableSetOf(listOf(start))
            val finishes = mutableSetOf<List<Point>>()

            while (toVisit.isNotEmpty()) {
                val path = toVisit.removeFirst()
                visited.add(path)
                if (grid[path.last()] == 9) {
                    finishes.add(path)
                } else {
                    val point = path.last()
                    point.adjacentSides().forEach { next ->
                        if (next inside grid && grid[next] - grid[point] == 1) {
                            val newPath = path + next
                            if (newPath !in visited) {
                                toVisit.add(newPath)
                            }
                        }
                    }
                }
            }

            return finishes.size
        }

        var result = 0
        grid.forEach { v, i, j ->
            if (v == 0) {
                result += trailheads(Point(i, j))
            }
        }
        return result
    }
}

fun main() = with(Day10) {
    test1(
        input = """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
        """.trimIndent(),
        expected = 36,
    )
    result1()

    test2(
        input = """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
        """.trimIndent(),
        expected = 81,
    )
    result2()
}
