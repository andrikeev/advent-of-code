package Year2024

import Day
import Point
import charGrid
import forEach
import i
import inside
import j
import kotlin.math.abs

private object Day08 : Day {
    fun antinodes(p1: Point, p2: Point, di: Int, dj: Int): List<Point> {
        val a1i: Int
        val a1j: Int
        val a2i: Int
        val a2j: Int

        if (p1.i <= p2.i) {
            if (p1.j <= p2.j) {
                a1i = p1.i - di
                a2i = p2.i + di
                a1j = p1.j - dj
                a2j = p2.j + dj
            } else {
                a1i = p1.i - di
                a2i = p2.i + di
                a1j = p1.j + dj
                a2j = p2.j - dj
            }
        } else {
            if (p1.j <= p2.j) {
                a1i = p1.i + di
                a2i = p2.i - di
                a1j = p1.j - dj
                a2j = p2.j + dj
            } else {
                a1i = p1.i + di
                a2i = p2.i - di
                a1j = p1.j + dj
                a2j = p2.j - dj
            }
        }
        return listOf(Point(a1i, a1j), Point(a2i, a2j))
    }

    override fun part1(input: List<String>): Any {
        val (grid) = input.charGrid()
        val antennas = buildMap<Char, MutableList<Point>> {
            grid.forEach { c, i, j ->
                if (c != '.') {
                    getOrPut(c) { mutableListOf() }.add(Point(i, j))
                }
            }
        }

        fun antinodes(p1: Point, p2: Point): List<Point> {
            val di = abs(p1.i - p2.i)
            val dj = abs(p1.j - p2.j)
            return antinodes(p1, p2, di, dj)
        }

        return buildSet {
            antennas.values.forEach { points ->
                for (i in points.indices) {
                    for (j in (i + 1)..points.lastIndex) {
                        addAll(antinodes(points[i], points[j]).filter { it inside grid })
                    }
                }
            }
        }.size
    }

    override fun part2(input: List<String>): Any {
        val (grid) = input.charGrid()
        val antennas = buildMap<Char, MutableList<Point>> {
            grid.forEach { c, i, j ->
                if (c != '.') {
                    getOrPut(c) { mutableListOf() }.add(Point(i, j))
                }
            }
        }

        fun antinodes(p1: Point, p2: Point): List<Point> {
            val di = abs(p1.i - p2.i)
            val dj = abs(p1.j - p2.j)
            var k = 0
            return buildList {
                var antinodes: List<Point>
                do {
                    antinodes = antinodes(p1, p2, di * k, dj * k)
                    addAll(antinodes)
                    k++
                } while (antinodes.any { it inside grid })
            }
        }

        return buildSet {
            antennas.values.forEach { points ->
                for (i in points.indices) {
                    for (j in (i + 1)..points.lastIndex) {
                        addAll(antinodes(points[i], points[j]).filter { it inside grid })
                    }
                }
            }
        }.size
    }
}

fun main() = with(Day08) {
    test1(
        input = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
        """.trimIndent(),
        expected = 14,
    )
    result1()

    test2(
        input = """
            T.........
            ...T......
            .T........
            ..........
            ..........
            ..........
            ..........
            ..........
            ..........
            ..........
        """.trimIndent(),
        expected = 9,
    )
    test2(
        input = """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
        """.trimIndent(),
        expected = 34,
    )
    result2()
}
