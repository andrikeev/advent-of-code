package Year2015

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        var res = 0
        input.forEach { line ->
            val (a, b, c) = line.split("x").map(String::toInt)
            val ab = a * b
            val ac = a * c
            val bc = b * c
            res += 2 * ab + 2 * ac + 2 * bc + minOf(ab, ac, bc)
        }
        return res
    }

    fun part2(input: List<String>): Any {
        var res = 0
        input.forEach { line ->
            val (a, b, c) = line.split("x").map(String::toInt)
            val ab = 2 * a + 2 * b
            val ac = 2 * a + 2 * c
            val bc = 2 * b + 2 * c
            res += minOf(ab, ac, bc) + a * b * c

        }
        return res
    }

    val input = readInput("Year2015/Day02")

    // part 1
    expect(part1(testInput("2x3x4")), 58)
    expect(part1(testInput("1x1x10")), 43)
    println(part1(input))

    // part 2
    expect(part2(testInput("2x3x4")), 34)
    expect(part2(testInput("1x1x10")), 14)
    println(part2(input))
}
