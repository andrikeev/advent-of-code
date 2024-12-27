package Year2021

import Day
import java.util.Stack
import java.util.TreeMap

private object Day24 : Day {
    private fun solve(input: List<String>): Pair<String, String> {
        val stack = Stack<Pair<Int, Int>>()
        val digits = TreeMap<Int, Pair<Int, Int>>()
        var push = false
        var sub = 0
        var dig = 0
        input.forEachIndexed { i, args ->
            val operand = args.split(' ').last()
            if (i % 18 == 4) {
                push = operand == "1"
            }
            if (i % 18 == 5) {
                sub = operand.toInt()
            }
            if (i % 18 == 15) {
                if (push) {
                    stack.push(dig to operand.toInt())
                } else {
                    val (prev, add) = stack.pop()
                    val diff = add + sub
                    if (diff < 0) {
                        digits[prev] = -diff + 1 to 9
                        digits[dig] = 1 to 9 + diff
                    } else {
                        digits[prev] = 1 to 9 - diff
                        digits[dig] = 1 + diff to 9
                    }
                }
                dig++
            }
        }
        return digits
            .values
            .unzip()
            .let { (a, b) -> b.joinToString("") to a.joinToString("") }
    }

    override fun part1(input: List<String>) = solve(input).first

    override fun part2(input: List<String>) = solve(input).second
}

fun main() = with(Day24) {
    result1()
    result2()
}
