package Year2023

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.first(Char::isDigit).digitToInt() * 10 + line.last(Char::isDigit).digitToInt()
        }
    }

    fun part2(input: List<String>): Int {
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

    val testInput1 = readInput("Year2023/Day01_test1")
    check(part1(testInput1).also { println("part1 test: $it") } == 142)
    val testInput2 = readInput("Year2023/Day01_test2")
    check(part2(testInput2).also { println("part2 test: $it") } == 281)

    val input = readInput("Year2023/Day01")
    println(part1(input))
    println(part2(input))
}
