package Year2022

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>) = buildString {
        var n = input.sumOf { line ->
            line.fold(0L) { k, c ->
                5 * k + when (c) {
                    '=' -> -2
                    '-' -> -1
                    else -> c.digitToInt()
                }
            }
        }
        while (n != 0L) {
            append("012=-"[n.mod(5)])
            n = (n + 2).floorDiv(5)
        }
        reverse()
    }

    val testInput = testInput(
        """
        1=-0-2
        12111
        2=0=
        21
        2=01
        111
        20012
        112
        1=-1=
        1-12
        12
        1=
        122
    """.trimIndent()
    )
    val input by lazy { readInput("Year2022/Day25") }
    expect(part1(testInput), "2=-1=0")
    println(part1(input))
}
