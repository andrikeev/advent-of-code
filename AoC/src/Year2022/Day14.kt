package Year2022

import Point
import i
import j
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val grid = RocksGrid.parse(input)
        var counter = 0
        while (grid.addSandUnit()) {
            counter++
        }
        return counter
    }

    fun part2(input: List<String>): Int {
        val grid = RocksGrid.parse(input)
        var counter = 0
        while (grid.addSandUnit2()) {
            counter++
        }
        return counter
    }

    val testInput = readInput("Year2022/Day14_test")
    check(part1(testInput).also { println("part1 test: $it") } == 24)
    check(part2(testInput).also { println("part2 test: $it") } == 93)

    val input = readInput("Year2022/Day14")
    println(part1(input))
    println(part2(input))
}

private class RocksGrid(
    val blocked: MutableSet<Point>,
    private val floorLevel: Int,
) {
    fun addSandUnit(): Boolean {
        var pos = Pair(500, 0)
        fun hasBottom(): Boolean = blocked.any { it.i == pos.i && it.j > pos.j }
        fun canFall(): Boolean {
            return !blocked.contains(Point(pos.i, pos.j + 1)) ||
                    !blocked.contains(Point(pos.i - 1, pos.j + 1)) ||
                    !blocked.contains(Point(pos.i + 1, pos.j + 1))
        }

        while (canFall()) {
            if (!hasBottom()) return false
            val newPos = when {
                !blocked.contains(Point(pos.i, pos.j + 1)) -> Point(pos.i, pos.j + 1)
                !blocked.contains(Point(pos.i - 1, pos.j + 1)) -> Point(pos.i - 1, pos.j + 1)
                !blocked.contains(Point(pos.i + 1, pos.j + 1)) -> Point(pos.i + 1, pos.j + 1)
                else -> error("not possible")
            }
            pos = newPos
        }
        blocked.add(pos)
        return true
    }

    fun addSandUnit2(): Boolean {
        var pos = Point(500, 0)
        fun canFall(): Boolean {
            return (pos.j < floorLevel - 1) &&
                    (!blocked.contains(Point(pos.i, pos.j + 1)) ||
                            !blocked.contains(Point(pos.i - 1, pos.j + 1)) ||
                            !blocked.contains(Point(pos.i + 1, pos.j + 1)))
        }
        if (blocked.contains(pos)) {
            return false
        }
        while (canFall()) {
            val newPos = when {
                !blocked.contains(Point(pos.i, pos.j + 1)) -> Point(pos.i, pos.j + 1)
                !blocked.contains(Point(pos.i - 1, pos.j + 1)) -> Point(pos.i - 1, pos.j + 1)
                !blocked.contains(Point(pos.i + 1, pos.j + 1)) -> Point(pos.i + 1, pos.j + 1)
                else -> error("not possible")
            }
            pos = newPos
        }
        blocked.add(pos)
        return true
    }

    companion object {
        fun parse(input: List<String>): RocksGrid {
            val rocks = mutableSetOf<Point>()
            var floorLevel = 0
            input.forEach { line ->
                val points = line
                    .split(" -> ")
                    .map { it.split(",").map(String::toInt).let { (x, y) -> Point(x, y) } }
                for (i in 1..points.lastIndex) {
                    val start = points[i - 1]
                    val end = points[i]
                    for (x in minOf(start.i, end.i)..maxOf(start.i, end.i)) {
                        for (y in minOf(start.j, end.j)..maxOf(start.j, end.j)) {
                            rocks.add(Point(x, y))
                        }
                    }
                    val maxLevel = maxOf(start.j, end.j) + 2
                    if (maxLevel > floorLevel) {
                        floorLevel = maxLevel
                    }
                }
            }
            return RocksGrid(rocks, floorLevel)
        }
    }
}
