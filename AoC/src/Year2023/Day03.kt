package Year2023

import Point
import adjacent
import expect
import readInput
import testInput

fun main() {

    fun parse(input: List<String>, symbol: Char? = null): Pair<Set<Pair<Int, List<Point>>>, Set<Point>> {
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

    fun part1(input: List<String>): Any {
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

    fun part2(input: List<String>): Any {
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

    val testInput = testInput("""
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598..
    """)
    val input = readInput("Year2023/Day03")

    // part 1
    expect(part1(testInput), 4361)
    println(part1(input))

    // part 2
    expect(part2(testInput), 467835)
    println(part2(input))
}
