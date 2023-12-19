package Year2023

import Direction
import Point
import i
import j
import moveTo
import readInput
import testInput
import kotlin.math.abs

fun main() {

    fun List<Point>.points(): Long {
        var square = 0L
        for (i in indices) {
            square += this[i].i * (this[(i + 1) % size].j - this[(size + i - 1) % size].j)
        }
        return abs(square) / 2 + size / 2 + 1
    }

    fun part1(input: List<String>): Int {
        val loop = mutableListOf<Point>()

        var p = Point(0, 0)
        input.forEach { line ->
            val (d, s) = line.split(" ")
            repeat(s.toInt()) {
                p = p moveTo when (d) {
                    "L" -> Direction.Left
                    "U" -> Direction.Up
                    "R" -> Direction.Right
                    "D" -> Direction.Down
                    else -> error("!!!")
                }
                loop.add(p)
            }
        }

        return loop.points().toInt()
    }

    fun part2(input: List<String>): Long {
        val loop = mutableListOf<Point>()

        var p = Point(0, 0)
        input.forEach { line ->
            val (_, _, command) = line.split(" ")
            val s = command.drop(2).take(5).toInt(16)
            val d = when (command[7].digitToInt()) {
                0 -> Direction.Right
                1 -> Direction.Down
                2 -> Direction.Left
                3 -> Direction.Up
                else -> error("!!!")
            }
            repeat(s) {
                p = p moveTo d
                loop.add(p)
            }
        }

        return loop.points()
    }

    val testInput = testInput("""
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """)
    check(part1(testInput).also { println("part1 test: $it") } == 62)
    check(part2(testInput).also { println("part2 test: $it") } == 952408144115)

    val input = readInput("Year2023/Day18")
    println(part1(input))
    println(part2(input))
}
