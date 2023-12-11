package Year2023

import Point
import i
import j
import readInput
import testInput
import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val expanded = mutableListOf<String>()
        for (line in input) {
            var line2 = ""
            line.forEachIndexed { i, ch ->
                line2 += ch
                if (input.all { it[i] == '.' }) {
                    line2 += ch
                }
            }
            expanded.add(line2)
            if (line.all { it == '.' }) {
                expanded.add(line2)
            }
        }
        val galaxies = expanded.mapIndexed { i, s ->
            s.mapIndexedNotNull { j, c ->
                c.takeIf { it == '#' }?.let { Point(i, j) }
            }
        }.flatten()

        var sum = 0
        galaxies.forEachIndexed { i, first ->
            galaxies.drop(i + 1).forEach { second ->
                sum += abs(first.j - second.j) + abs(first.i - second.i)
            }
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        val expanded = mutableMapOf<Point, Space>()
        input.forEachIndexed { i, s ->
            if (s.all { it == '.' }) {
                s.forEachIndexed { j, _ ->
                    expanded[Point(i, j)] = Empty(1_000_000L)
                }
            } else {
                s.forEachIndexed { j, c ->
                    expanded[Point(i, j)] = if (c == '.') {
                        if (input.all { it[j] == '.' }) {
                            Empty(1_000_000L)
                        } else {
                            Empty(1L)
                        }
                    } else {
                        Galaxy
                    }
                }
            }
        }

        val galaxies = expanded.filterValues { it is Galaxy }.keys

        var sum = 0L
        galaxies.forEachIndexed { i, first ->
            galaxies.drop(i + 1).forEach { second ->
                val di = if (first.i < second.i) (first.i until second.i) else (second.i until first.i)
                val dj = if (first.j < second.j) (first.j until second.j) else (second.j until first.j)
                val dx = dj.sumOf { x -> expanded.getValue(Point(first.i, x)).size }
                val dy = di.sumOf { y -> expanded.getValue(Point(y, second.j)).size }

                sum += dx + dy
            }
        }

        return sum
    }

    val testInput = testInput("""
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent())
    check(part1(testInput).also { println("part1 test: $it") } == 374)
    check(part2(testInput).also { println("part2 test: $it") } == 82000210L)

    val input = readInput("Year2023/Day11")
    println(part1(input))
    println(part2(input))
}

sealed interface Space {
    val size: Long
}

data class Empty(override val size: Long) : Space

data object Galaxy : Space {
    override val size: Long = 1
}
