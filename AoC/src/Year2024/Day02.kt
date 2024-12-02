package Year2024

import expect
import readInput
import testInput
import kotlin.math.abs
import kotlin.math.sign

fun main() {

    fun List<Int>.isSafe(): Boolean {
        var sign = 0
        var prev = first()
        drop(1).forEach { curr ->
            val diff = curr - prev
            if (abs(diff) < 1 || abs(diff) > 3) {
                return false
            } else {
                when {
                    sign == 0 -> {
                        sign = diff.sign
                        prev = curr
                    }

                    sign != diff.sign -> {
                        return false
                    }

                    else -> {
                        prev = curr
                    }
                }
            }
        }
        return true
    }

    fun part1(input: List<String>): Any {
        return input.count { line ->
            line.split(" ").map(String::toInt).isSafe()
        }
    }

    fun part2(input: List<String>): Any {
        return input.count { line ->
            val nums = line.split(" ").map(String::toInt)
            nums.indices.any { i ->
                (nums.subList(0, i) + nums.subList(i + 1, nums.size)).isSafe()
            }
        }
    }

    val input = readInput("Year2024/Day02")

    // part 1
    expect(part1(testInput("""
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """)), 2)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput("""
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """)), 4)
    println("Part 2: ${part2(input)}")
}
