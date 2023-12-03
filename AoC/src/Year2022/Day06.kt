package Year2022

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            var marker = 0
            for (i in 0..line.lastIndex - 3) {
                if (line.subSequence(i, i + 4).toSet().size == 4) {
                    marker = i + 4
                    break
                }
            }
            marker
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            var marker = 0
            for (i in 0..line.lastIndex - 13) {
                if (line.subSequence(i, i + 14).toSet().size == 14) {
                    marker = i + 14
                    break
                }
            }
            marker
        }
    }

    val testInput = readInput("Year2022/Day06_test")
    check(part1(testInput) == 39)
    check(part2(testInput) == 120)

    val input = readInput("Year2022/Day06")
    println(part1(input))
    println(part2(input))
}
