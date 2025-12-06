package Year2025

import charGrid
import expect
import readInput
import testInput
import transpose
import kotlin.collections.last

fun main() {

    fun part1(input: List<String>): Any {
        return input.map { it.split(' ').filter(String::isNotBlank).map(String::trim) }
            .asReversed()
            .fold(arrayOf<Pair<String, Long>>()) { acc, new ->
                if (acc.isEmpty()) {
                    new.map { op ->
                        op to if (op == "*") {
                            1L
                        } else {
                            0L
                        }
                    }.toTypedArray()
                } else {
                    new.forEachIndexed { i, value ->
                        val (op, res) = acc[i]
                        acc[i] = op to if (op == "*") {
                            res * value.toLong()
                        } else {
                            res + value.toLong()
                        }
                    }
                    acc
                }
            }
            .sumOf(Pair<String, Long>::second)
    }

    fun part2(input: List<String>): Any {
        class Problem(var op: Char, val args: MutableList<Long>)
        return buildList {
            input.charGrid()
                .first
                .transpose()
                .forEach { row ->
                    val str = row.concatToString()
                    if (str.isBlank()) {
                        add(Problem(' ', mutableListOf()))
                    } else {
                        val (op, arg) = str.let { it.last() to it.dropLast(1).trim().toLong() }
                        if (isEmpty()) {
                            add(Problem(op, mutableListOf()))
                        }
                        if (op != ' ') {
                            last().op = op
                        }
                        last().args.add(arg)
                    }
                }
        }.sumOf { problem ->
            if (problem.op == '*') {
                problem.args.fold(1L, Long::times)
            } else {
                problem.args.fold(0L, Long::plus)
            }
        }
    }

    val input = readInput("Year2025/Day06")
    val testInput = testInput(
        """
            123 328  51 64 
             45 64  387 23 
              6 98  215 314
            *   +   *   +  
        """.trimIndent()
    )

    // part 1
    expect(part1(testInput), 4277556L)
    println("Part 1: ${part1(input)}")

    // part 2
    expect(part2(testInput), 3263827L)
    println("Part 2: ${part2(input)}")
}
