package Year2024

import CharGrid
import Day
import Point
import i
import j

private object Day14 : Day {
    private const val W = 101
    private const val H = 103

    private data class Robot(val p: Point, val v: Point)

    private fun List<String>.robots(): List<Robot> {
        return map { line ->
            val (p, v) = line.split(' ')
            Robot(
                p.substringAfter('=').split(',').map(String::toInt).let { Point(it[0], it[1]) },
                v.substringAfter('=').split(',').map(String::toInt).let { Point(it[0], it[1]) },
            )
        }
    }

    private fun Robot.move(): Robot {
        val next = Point((p.i + v.i + W) % W, (p.j + v.j + H) % H)
        return Robot(next, v)
    }

    override fun part1(input: List<String>): Any {
        var robots = input.robots()
        repeat(100) {
            robots = robots.map { it.move() }
        }
        var a = 0
        var b = 0
        var c = 0
        var d = 0
        val midx = W / 2
        val midy = H / 2
        robots.forEach {
            when {
                it.p.i in 0..<midx && it.p.j in 0..<midy -> a++
                it.p.i in (midx + 1)..<W && it.p.j in 0..<midy -> b++
                it.p.i in 0..<midx && it.p.j in (midy + 1)..<H -> c++
                it.p.i in (midx + 1)..<W && it.p.j in (midy + 1)..<H -> d++
            }
        }
        return a * b * c * d
    }

    override fun part2(input: List<String>): Any {
        var robots = input.robots()
        var s = 0
        while (true) {
            s++
            robots = robots.map { it.move() }
            val grid = CharGrid(H) { CharArray(W) { '.' } }
            robots.forEach { r ->
                grid[r.p.j][r.p.i] = '#'
            }
            val lines = grid.map(CharArray::concatToString)
            if (lines.any { it.contains("##########") }) {
                lines.forEach(::println)
                return s
            }
        }
    }
}

fun main() = with(Day14) {
    result1()
    result2()
}
