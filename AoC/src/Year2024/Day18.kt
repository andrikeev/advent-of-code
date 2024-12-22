package Year2024

import Day
import Point
import adjacentSides
import bfs

private class Day18(
    private val count: Int,
    private val range: IntRange,
) : Day {
    private fun List<String>.bytes(): List<Point> {
        return map { line ->
            line.split(',')
                .map(String::toInt)
        }
            .map { (i, j) -> Point(j, i) }
            .toList()
    }

    override fun part1(input: List<String>): Any {
        val bytes = input
            .bytes()
            .take(count)
            .toSet()
        bfs(
            start = Point(range.first, range.first) to 0,
            next = { (point, score) ->
                point.adjacentSides()
                    .filter { (i, j) -> i in range && j in range }
                    .filter { it !in bytes }
                    .map { it to score + 1 }
            },
            key = Pair<Point, Int>::first,
            visit = { (point, score) ->
                if (point == Point(range.last, range.last)) {
                    return score
                }
            },
        )
        error("No path found")
    }

    override fun part2(input: List<String>): Any {
        val bytes = input.bytes()
        val finish = Point(range.last, range.last)
        for (i in (count + 1)..<bytes.size) {
            val currentBytes = bytes.take(i)
            var finished = false
            bfs(
                start = Point(range.first, range.first) to 0,
                next = { (point, score) ->
                    point.adjacentSides()
                        .filter { (i, j) -> i in range && j in range }
                        .filter { it !in currentBytes }
                        .map { it to score + 1 }
                },
                key = Pair<Point, Int>::first,
                visit = { (point) ->
                    if (point == finish) {
                        finished = true
                    }
                },
                finished = { (point) ->
                    point == finish
                },
            )
            if (!finished) {
                return currentBytes[i - 1].let { (i, j) -> "$j,$i" }
            }
        }
        error("No solution found")
    }
}

fun main() {
    with(Day18(count = 12, range = 0..6)) {
        test1(
            input = """
                5,4
                4,2
                4,5
                3,0
                2,1
                6,3
                2,4
                1,5
                0,6
                3,3
                2,6
                5,1
                1,2
                5,5
                2,5
                6,5
                1,4
                0,4
                6,4
                1,1
                6,1
                1,0
                0,5
                1,6
                2,0
            """.trimIndent(),
            expected = 22,
        )
    }
    with(Day18(count = 1024, range = 0..70)) {
        result1()
    }

    with(Day18(count = 12, range = 0..6)) {
        test2(
            input = """
                5,4
                4,2
                4,5
                3,0
                2,1
                6,3
                2,4
                1,5
                0,6
                3,3
                2,6
                5,1
                1,2
                5,5
                2,5
                6,5
                1,4
                0,4
                6,4
                1,1
                6,1
                1,0
                0,5
                1,6
                2,0
            """.trimIndent(),
            expected = "6,1",
        )
    }
    with(Day18(1024, 0..70)) {
        result2()
    }
}
