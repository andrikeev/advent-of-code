package Year2022

import readInput

fun main() {

    fun contains(first: String, second: String): Boolean {
        val (aStart, aEnd) = first.split("-").map(String::toInt)
        val (bStart, bEnd) = second.split("-").map(String::toInt)

        return aStart <= bStart && aEnd >= bEnd || bStart <= aStart && bEnd >= aEnd
    }

    fun intersect(first: String, second: String): Boolean {
        val (aStart, aEnd) = first.split("-").map(String::toInt)
        val (bStart, bEnd) = second.split("-").map(String::toInt)

        return aStart in bStart..bEnd || bStart in aStart..aEnd
    }

    fun part1(input: List<String>): Int {
        return input.count { line ->
            val (first, second) = line.split(",")
            contains(first, second)
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { line ->
            val (first, second) = line.split(",")
            intersect(first, second)
        }
    }

    val testInput = readInput("Year2022/Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Year2022/Day04")
    println(part1(input))
    println(part2(input))
}
