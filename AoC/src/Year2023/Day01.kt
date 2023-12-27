package Year2023

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        return input.sumOf { line ->
            line.first(Char::isDigit).digitToInt() * 10 + line.last(Char::isDigit).digitToInt()
        }
    }

    fun part2(input: List<String>): Any {
        val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        return input.sumOf { line ->
            var first = 0
            var second = 0
            var i = 0
            while (i < line.length) {
                if (line[i].isDigit()) {
                    val digit = line[i].digitToInt()
                    if (first == 0) {
                        first = digit
                    } else {
                        second = digit
                    }
                    i++
                } else {
                    val num = numbers.firstOrNull { num ->
                        (num.length <= line.length - i) && (line.substring(i, i + num.length) == num)
                    }
                    if (num != null) {
                        val digit = numbers.indexOf(num) + 1
                        if (first == 0) {
                            first = digit
                        } else {
                            second = digit
                        }
                        i += num.length
                    } else {
                        i++
                    }
                }
            }
            first * 10 + second
        }
    }

    val testInput1 = testInput("""
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """)
    val input = readInput("Year2023/Day01")

    // part 1
    expect(part1(testInput1), 142)
    println(part1(input))

    // part 2
    val testInput2 = testInput("""
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """)
    expect(part2(testInput2), 281)
    println(part2(input))
}
