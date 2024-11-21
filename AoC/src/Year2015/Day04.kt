package Year2015

import expect
import md5
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        val key = input.first()
        var num = 1
        while (true) {
            if ("$key$num".md5().startsWith("00000")) {
                return num
            }
            num++
        }
    }

    fun part2(input: List<String>): Any {
        val key = input.first()
        var num = 1
        while (true) {
            if ("$key$num".md5().startsWith("000000")) {
                return num
            }
            num++
        }
    }

    val input = readInput("Year2015/Day04")

    // part 1
    expect(part1(testInput("abcdef")), 609043)
    expect(part1(testInput("pqrstuv")), 1048970)
    println(part1(input))

    // part 2
    println(part2(input))
}
