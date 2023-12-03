package Year2023

import Point
import adjacent
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val (numbers, symbols) = parse(input)
        return numbers.filter { (_, positions) ->
            positions
                .asSequence()
                .map(Point::adjacent)
                .flatten()
                .distinct()
                .any(symbols::contains)
        }.sumOf(Pair<Int, List<Point>>::first)
    }

    fun part2(input: List<String>): Int {
        val (numbers, symbols) = parse(input, '*')
        return numbers
            .groupBy(
                { (_, positions) ->
                    positions
                        .asSequence()
                        .map(Point::adjacent)
                        .flatten()
                        .distinct()
                        .firstOrNull(symbols::contains)
                },
                Pair<Int, List<Point>>::first,
            )
            .filterKeys { it != null }
            .filterValues { it.size == 2 }
            .values
            .sumOf { (first, second) -> first * second }
    }

    val testInput = readInput("Year2023/Day03_test")
    check(part1(testInput).also { println("part1 test: $it") } == 4361)
    check(part2(testInput).also { println("part2 test: $it") } == 467835)

    val input = readInput("Year2023/Day03")
    println(part1(input))
    println(part2(input))
}

private fun parse(input: List<String>, symbol: Char? = null): Pair<Set<Pair<Int, List<Point>>>, Set<Point>> {
    val numbers = mutableSetOf<Pair<Int, List<Point>>>()
    val symbols = mutableSetOf<Point>()
    input.forEachIndexed { i, s ->
        var number = -1
        var position = IntRange.EMPTY
        s.forEachIndexed { j, c ->
            if (c.isDigit()) {
                if (number != -1) {
                    number = 10 * number + c.digitToInt()
                    position = position.first..j
                } else {
                    number = c.digitToInt()
                    position = j..j
                }
            } else {
                if (c != '.' && (symbol == null || c == symbol)) {
                    symbols.add(Point(i, j))
                }
                if (number != -1) {
                    numbers.add(number to position.map { Point(i, it) })
                    number = -1
                    position = IntRange.EMPTY
                }
            }
        }
        if (number != -1) {
            numbers.add(number to position.map { Point(i, it) })
            number = -1
            position = IntRange.EMPTY
        }
    }
    return numbers to symbols
}
