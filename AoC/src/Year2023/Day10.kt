package Year2023

import Direction
import Point
import above
import adjacent
import adjacentSides
import below
import i
import j
import leftTo
import moveTo
import readInput
import rightTo
import testInput

fun main() {

    fun map(input: List<String>): Map<Point, Char> = input
        .mapIndexed { i, s -> s.mapIndexed { j, c -> Point(i, j) to c } }
        .flatten()
        .associateBy(Pair<Point, Char>::first, Pair<Point, Char>::second)

    fun Map<Point, Char>.start(): Point = entries.first { it.value == 'S' }.key

    fun Map<Point, Char>.loop(): List<Point> {
        val visited = mutableListOf<Point>()
        val toVisit = ArrayDeque<Point>().apply { add(start()) }
        while (toVisit.isNotEmpty()) {
            val point = toVisit.removeFirst()
            visited.add(point)
            val next = point.adjacentSides()
                .filter { contains(it) }
                .filter { !visited.contains(it) }
                .firstOrNull {
                    val p = getValue(point)
                    val c = getValue(it)
                    when (c) {
                        '7' -> p == 'S' || it rightTo point && (p == '-' || p == 'L' || p == 'F') ||
                                it above point && (p == '|' || p == 'L' || p == 'J')

                        'F' -> p == 'S' || it leftTo point && (p == '-' || p == 'J' || p == '7') ||
                                it above point && (p == '|' || p == 'L' || p == 'J')

                        'J' -> p == 'S' || it rightTo point && (p == '-' || p == 'L' || p == 'F') ||
                                it below point && (p == '|' || p == '7' || p == 'F')

                        'L' -> p == 'S' || it leftTo point && (p == '-' || p == '7' || p == 'J') ||
                                it below point && (p == '|' || p == '7' || p == 'F')

                        '-' -> p == 'S' || it rightTo point && (p == '-' || p == 'L' || p == 'F') ||
                                it leftTo point && (p == '-' || p == 'J' || p == '7')

                        '|' -> p == 'S' || it above point && (p == '|' || p == 'L' || p == 'J') ||
                                it below point && (p == '|' || p == '7' || p == 'F')

                        else -> false
                    }
                }
            if (next != null) {
                toVisit.addLast(next)
            }
        }
        return visited
    }

    fun part1(input: List<String>) = map(input).loop().size / 2

    fun part2(input: List<String>): Int {
        val map = map(input)
        val loop = map.loop()

        val moves = mutableMapOf(
            'S' to Direction.entries,
            '|' to listOf(Direction.Up, Direction.Down),
            '-' to listOf(Direction.Left, Direction.Right),
            'L' to listOf(Direction.Up, Direction.Right),
            'J' to listOf(Direction.Left, Direction.Up),
            '7' to listOf(Direction.Left, Direction.Down),
            'F' to listOf(Direction.Right, Direction.Down),
        )

        val expMap = mutableMapOf<Point, Char>()
        map.forEach { (point, c) ->
            val expPoint = Point(point.i * 3, point.j * 3)
            expMap[expPoint] = if (c != '.' && loop.contains(point)) 'X' else '.'
            expPoint.adjacent().forEach { expMap[it] = '.' }
            if (loop.contains(point)) {
                moves.getValue(c).forEach { direction ->
                    expMap[expPoint moveTo direction] = 'X'
                }
            }
        }

        val toVisit = ArrayDeque<Point>().apply { add(Point(0, 0)) }
        while (toVisit.isNotEmpty()) {
            val point = toVisit.removeFirst()
            expMap[point] = '0'
            point.adjacentSides()
                .filter { expMap[it] == '.' && !toVisit.contains(it) }
                .forEach(toVisit::addLast)
        }

        return map.keys.count { expMap[Point(it.i * 3, it.j * 3)] == '.' }
    }

    val testInput = testInput("""
        ..F7.
        .FJ|.
        SJ.L7
        |F--J
        LJ...
    """.trimIndent())
    check(part1(testInput).also { println("part1 test: $it") } == 8)

    val testInput2 = testInput("""
        ...........
        .S-------7.
        .|F-----7|.
        .||OOOOO||.
        .||OOOOO||.
        .|L-7OF-J|.
        .|II|O|II|.
        .L--JOL--J.
        .....O.....
    """.trimIndent())
    check(part2(testInput2).also { println("part2 test: $it") } == 4)

    val testInput3 = testInput("""
        ..........
        .S------7.
        .|F----7|.
        .||OOOO||.
        .||OOOO||.
        .|L-7F-J|.
        .|II||II|.
        .L--JL--J.
        ..........
    """)
    check(part2(testInput3).also { println("part2 test: $it") } == 4)

    val input = readInput("Year2023/Day10")
    println(part1(input))
    println(part2(input))
}
