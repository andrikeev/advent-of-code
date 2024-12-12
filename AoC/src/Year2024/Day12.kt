package Year2024

import CharGrid
import Day
import Direction
import Point
import Position
import adjacentSides
import charGrid
import forEach
import get
import inside
import moveBy
import moveTo

private object Day12 : Day {
    private fun CharGrid.regions(): List<Set<Point>> {
        val visited = mutableSetOf<Point>()

        fun region(point: Point, visited: MutableSet<Point>): Set<Point> {
            val next = point
                .adjacentSides()
                .filter { it !in visited }
                .filter { it inside this }
                .filter { this[it] == this[point] }
                .toSet()
                .also(visited::addAll)

            return setOf(point) + next.flatMap { region(it, visited) }
        }

        return buildList {
            forEach { _, i, j ->
                val p = Point(i, j)
                if (p !in visited) {
                    val region = region(p, mutableSetOf(p))
                    visited.addAll(region)
                    add(region)
                }
            }
        }
    }

    override fun part1(input: List<String>): Any {
        val (grid, _, _) = input.charGrid()
        val regions = grid.regions()
        return regions.sumOf { r ->
            val area = r.size
            val perimeter = r.sumOf { p ->
                p.adjacentSides().count { it !in r }
            }
            area * perimeter
        }
    }

    override fun part2(input: List<String>): Any {
        val (grid, _, _) = input.charGrid()
        val regions = grid.regions()
        return regions.sumOf { r ->
            val outerPoints = r.filter { p -> p.adjacentSides().any { it !in r } }
            val sides = mutableSetOf<List<Position>>()
            val visited = mutableSetOf<Position>()

            fun side(pos: Position, visited: MutableSet<Point>): Set<Point> {
                val (p, d) = pos
                val next = when (d) {
                    Direction.Left,
                    Direction.Right -> {
                        setOf(
                            p.moveBy(i = -1),
                            p.moveBy(i = +1),
                        )
                    }

                    Direction.Up,
                    Direction.Down -> {
                        setOf(
                            p.moveBy(j = -1),
                            p.moveBy(j = +1),
                        )
                    }
                }
                    .filter { it !in visited }
                    .filter { it in outerPoints }
                    .filter { it.moveTo(d) !in outerPoints }
                    .also(visited::addAll)

                return setOf(p) + next.flatMap { side(it to d, visited) }
            }

            outerPoints.forEach { p ->
                Direction.entries
                    .filter { p.moveTo(it) !in r }
                    .filter { (p to it) !in visited }
                    .forEach { d ->
                        val side = side(p to d, mutableSetOf(p)).map { it to d }
                        visited.addAll(side)
                        sides.add(side)
                    }
            }

            r.size * sides.size
        }
    }
}

fun main() = with(Day12) {
    test1(
        input = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
        """.trimIndent(),
        expected = 1930,
    )
    result1()

    test2(
        input = """
            AAAA
            BBCD
            BBCC
            EEEC
        """.trimIndent(),
        expected = 80,
    )
    test2(
        input = """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
        """.trimIndent(),
        expected = 236,
    )
    test2(
        input = """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
        """.trimIndent(),
        expected = 368,
    )
    result2()
}
