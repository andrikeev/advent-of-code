package Year2022

import Direction
import Point
import Position
import adjacentSides
import direction
import expect
import i
import j
import point
import readInput
import testInput

typealias Blizzard = Position

fun main() {

    val directions = "<^>v"
    fun direction(c: Char) = Direction.entries[directions.indexOf(c)]
    fun blizzards(input: List<String>): List<Blizzard> {
        return buildList {
            input.flatMapIndexed { i, line ->
                line.mapIndexed { j, c ->
                    if (c in directions) {
                        add(Blizzard(Point(i - 1, j - 1), direction(c)))
                    }
                }
            }
        }
    }

    fun Blizzard.next(n: Int, m: Int): Blizzard {
        return when (direction) {
            Direction.Left -> Point(point.i, (point.j - 1).mod(m))
            Direction.Up -> Point((point.i - 1).mod(n), point.j)
            Direction.Right -> Point(point.i, (point.j + 1).mod(m))
            Direction.Down -> Point((point.i + 1).mod(n), point.j)
        } to direction
    }

    fun findPath(
        n: Int,
        m: Int,
        start: Point,
        finish: Point,
        initialBlizzards: List<Blizzard>,
        trips: Int = 1,
    ): Int {
        var blizzards = initialBlizzards
        var toVisit = setOf(start)
        var target = finish
        var tripCount = 0
        var minutes = 0
        while (true) {
            blizzards = blizzards.map { it.next(n, m) }
            val excludedPoints = blizzards.map(Blizzard::point).toSet()

            if (toVisit.any { it == target }) {
                tripCount++
                when {
                    tripCount == trips -> return minutes

                    tripCount % 2 == 1 -> {
                        target = start
                        toVisit = setOf(finish)
                    }

                    tripCount % 2 == 0 -> {
                        target = finish
                        toVisit = setOf(start)
                    }


                    else -> return minutes
                }
            }

            toVisit = toVisit
                .flatMap { point -> point.adjacentSides().plus(point) }
                .filterNot { it in excludedPoints }
                .filter { it == start || it == finish || it.i in 0 until n && it.j in 0 until m }
                .toSet()
            minutes++
        }
    }

    fun part1(input: List<String>): Int {
        val n = input.size - 2
        val m = input.first().length - 2
        return findPath(
            n = n,
            m = m,
            start = Point(-1, 0),
            finish = Point(n, m - 1),
            initialBlizzards = blizzards(input),
        )
    }

    fun part2(input: List<String>): Int {
        val n = input.size - 2
        val m = input.first().length - 2
        return findPath(
            n = n,
            m = m,
            start = Point(-1, 0),
            finish = Point(n, m - 1),
            initialBlizzards = blizzards(input),
            trips = 3,
        )
    }

    val testInput = testInput("""
        #.######
        #>>.<^<#
        #.<..<<#
        #>v.><>#
        #<^v^^>#
        ######.#
    """)
    val input = readInput("Year2022/Day24")

    expect(part1(testInput), 18)
    println(part1(input))

    expect(part2(testInput), 54)
    println(part2(input))
}
