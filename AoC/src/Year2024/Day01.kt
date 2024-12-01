package Year2024

import expect
import readInput
import testInput
import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Any {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()
        for (line in input) {
            val (a, b) = line.split("   ").map(String::toInt)
            list1.add(a)
            list2.add(b)
        }

        list1.sort()
        list2.sort()

        var sum = 0
        for (i in 0 until list1.size) {
            sum += abs(list1[i] - list2[i])
        }

        return sum
    }

    fun part2(input: List<String>): Any {
        val list1 = mutableListOf<Int>()
        val list2 = mutableMapOf<Int, Int>()
        for (line in input) {
            val (a, b) = line.split("   ").map(String::toInt)
            list1.add(a)
            list2[b] = list2.getOrDefault(b, 0) + 1
        }

        var sum = 0
        list1.forEach {  num ->
            sum += num * list2.getOrDefault(num, 0)
        }

        return sum
    }

    val input = readInput("Year2024/Day01")

    // part 1
    expect(part1(testInput("""
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent())), 11)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput("""
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent())), 31)
    println("Part 2: ${part2(input)}")
}
