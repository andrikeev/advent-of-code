package Year2024

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        val reg = Regex("""mul\((?<a>\d+),(?<b>\d+)\)""")
        var sum = 0
        input.forEach { line ->
            reg.findAll(line).forEach { match ->
                val a = match.groups["a"]!!.value.toInt()
                val b = match.groups["b"]!!.value.toInt()
                sum += a * b
            }
        }
        return sum
    }

    fun part2(input: List<String>): Any {
        val reg = Regex("""mul\((?<a>\d+),(?<b>\d+)\)|don't\(\)|do\(\)""")
        var sum = 0
        var enabled = true
        input.forEach { line ->
            reg.findAll(line).forEach { match ->
                when (match.value) {
                    "don't()" -> enabled = false
                    "do()" -> enabled = true
                    else -> if (enabled) {
                        val a = match.groups["a"]!!.value.toInt()
                        val b = match.groups["b"]!!.value.toInt()
                        sum += a * b
                    }
                }
            }
        }
        return sum
    }

    val input = readInput("Year2024/Day03")

    // part 1
    expect(part1(testInput("""
        xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
    """)), 161)
    println(part1(input))

    // part 2
    expect(part2(testInput("""
        xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
    """)), 48)
    println(part2(input))
}
