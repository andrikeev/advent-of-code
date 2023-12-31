package Year2021

import Point
import expect
import i
import j
import moveBy
import readInput
import testInput
import kotlin.math.abs

fun main() {

    fun targetArea(input: String): Pair<IntRange, IntRange> {
        val (x, y) = input
            .substringAfter(':')
            .split(',')
            .map(String::trim)
        val xRange = x
            .substringAfter('=')
            .split("..")
            .map(String::toInt)
            .let { (s, e) -> s..e }
        val yRange = y
            .substringAfter('=')
            .split("..")
            .map(String::toInt)
            .let { (s, e) -> s..e }
        return xRange to yRange
    }

    fun part1(input: List<String>): Any {
        val (x, y) = targetArea(input.single())
        val start = Point(0, 0)

        var res = 0
        for (i in 0..x.last) {
            for (j in y.first..abs(y.first)) {
                var vx = i
                var vy = j
                var current = start
                var maxHeight = 0
                while (current.j > y.first) {
                    current = current.moveBy(vx, vy)
                    maxHeight = maxOf(maxHeight, current.j)
                    vx += 0 compareTo vx
                    vy--
                    if (current.i in x && current.j in y) {
                        res = maxOf(res, maxHeight)
                        break
                    }
                }
            }
        }

        return res
    }

    fun part2(input: List<String>): Any {
        val (x, y) = targetArea(input.single())
        val start = Point(0, 0)

        var res = 0
        for (i in 0..x.last) {
            for (j in y.first..abs(y.first)) {
                var vx = i
                var vy = j
                var current = start
                while (current.j > y.first) {
                    current = current.moveBy(vx, vy)
                    vx += 0 compareTo vx
                    vy--
                    if (current.i in x && current.j in y) {
                        res++
                        break
                    }
                }
            }
        }

        return res
    }

    val testInput = testInput("target area: x=20..30, y=-10..-5")
    val input = readInput("Year2021/Day17")

    // part 1
    expect(part1(testInput), 45)
    println(part1(input))

    // part 2
    expect(part2(testInput), 112)
    println(part2(input))
}
