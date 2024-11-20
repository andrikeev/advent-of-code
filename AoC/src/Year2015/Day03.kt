package Year2015

import Direction
import Point
import expect
import moveTo
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        var point = Point(0, 0)
        val visited = mutableSetOf(point)
        input.first().forEach { c ->
            point = point.moveTo(
                when (c) {
                    '<' -> Direction.Left
                    '^' -> Direction.Up
                    '>' -> Direction.Right
                    'v' -> Direction.Down
                    else -> error("")
                }
            )
            visited.add(point)
        }

        return visited.size
    }

    fun part2(input: List<String>): Any {
        var point1 = Point(0, 0)
        var point2 = Point(0, 0)
        val visited = mutableSetOf(point1, point2)
        input.first().forEachIndexed { index, c ->
            val direction = when (c) {
                '<' -> Direction.Left
                '^' -> Direction.Up
                '>' -> Direction.Right
                'v' -> Direction.Down
                else -> error("")
            }

            if (index % 2 == 0) {
                point1 = point1.moveTo(direction)
                visited.add(point1)
            } else {
                point2 = point2.moveTo(direction)
                visited.add(point2)
            }
        }

        return visited.size
    }

    val input = readInput("Year2015/Day03")

    // part 1
    expect(part1(testInput(">")), 2)
    expect(part1(testInput("^>v<")), 4)
    expect(part1(testInput("^v^v^v^v^v")), 2)
    println(part1(input))

    // part 2
    expect(part2(testInput("^v")), 3)
    expect(part2(testInput("^>v<")), 3)
    expect(part2(testInput("^v^v^v^v^v")), 11)
    println(part2(input))
}
