package Year2022

import Direction
import Point
import Position
import direction
import i
import j
import moveTo
import point
import readInput
import turnLeft
import turnRight
import kotlin.collections.List
import kotlin.collections.buildList
import kotlin.collections.count
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.collections.indexOfFirst
import kotlin.collections.last
import kotlin.collections.map
import kotlin.collections.maxOf
import kotlin.collections.takeWhile
import kotlin.collections.toTypedArray
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {

    fun parseTiles(input: List<String>): Array<Array<Tile>> {
        val rowSize = input.maxOf(String::length)
        return input.map { line ->
            Array(rowSize) { i ->
                if (i < line.length) {
                    when (line[i]) {
                        '.' -> Tile.Open
                        '#' -> Tile.Wall
                        else -> Tile.Space
                    }
                } else {
                    Tile.Space
                }
            }
        }.toTypedArray()
    }

    fun parsePath(input: String): List<Step> {
        return buildList {
            val currentMove = StringBuilder()
            input.forEach { ch ->
                if (ch.isDigit()) {
                    currentMove.append(ch)
                } else {
                    add(Step.Move(currentMove.toString().toInt()))
                    currentMove.clear()
                    when (ch) {
                        'L' -> add(Step.LTurn)
                        'R' -> add(Step.RTurn)
                    }
                }
            }
            if (currentMove.isNotEmpty()) {
                add(Step.Move(currentMove.toString().toInt()))
            }
        }
    }

    fun Position.turnLeft(): Position = copy(second = direction.turnLeft())
    fun Position.turnRight(): Position = copy(second = direction.turnRight())
    fun Position.moveTo(point: Point): Position = copy(first = point)
    fun Position.next(): Position = copy(first = point moveTo direction)
    fun Direction.value() = when (this) {
        Direction.Right -> 0
        Direction.Down -> 1
        Direction.Left -> 2
        Direction.Up -> 3
    }

    fun Position.calc(): Long {
        return 1000L * (point.i + 1) + 4 * (point.j + 1) + direction.value()
    }

    fun part1(input: List<String>): Long {
        val map = parseTiles(input.takeWhile(String::isNotBlank))
        val steps = parsePath(input.last())
        var position = Position(0 to map.first().indexOfFirst(Tile::isNotSpace), Direction.Right)

        steps.forEach { step ->
            position = when (step) {
                is Step.LTurn -> position.turnLeft()
                is Step.RTurn -> position.turnRight()
                is Step.Move -> {
                    var current = position
                    for (s in 0 until step.number) {
                        val (i, j) = current.point
                        val next = when (current.direction) {
                            Direction.Left -> {
                                val row = map[i]
                                val rowSize = row.size
                                var k = (rowSize + j - 1) % rowSize
                                while (row[k].isSpace) {
                                    k = (rowSize + k - 1) % rowSize
                                }
                                Point(i, k)
                            }
                            Direction.Up -> {
                                val columnSize = map.size
                                var k = (columnSize + i - 1) % columnSize
                                while (map[k][j].isSpace) {
                                    k = (columnSize + k - 1) % columnSize
                                }
                                Point(k, j)
                            }
                            Direction.Right -> {
                                val row = map[i]
                                val rowSize = row.size
                                var k = (j + 1) % rowSize
                                while (row[k].isSpace) {
                                    k = (k + 1) % rowSize
                                }
                                Point(i, k)
                            }
                            Direction.Down -> {
                                val columnSize = map.size
                                var k = (i + 1) % columnSize
                                while (map[k][j].isSpace) {
                                    k = (k + 1) % columnSize
                                }
                                Point(k, j)
                            }
                        }
                        if (map[next.i][next.j].isOpen) {
                            current = current.moveTo(next)
                        } else {
                            break
                        }
                    }
                    current
                }
            }
        }

        return position.calc()
    }

    fun part2(input: List<String>): Long {
        val map = parseTiles(input.takeWhile(String::isNotBlank))
        val steps = parsePath(input.last())
        var position = Position(0 to map.first().indexOfFirst(Tile::isNotSpace), Direction.Right)

        steps.forEach { step ->
            position = when (step) {
                is Step.LTurn -> position.turnLeft()
                is Step.RTurn -> position.turnRight()
                is Step.Move -> {
                    var current = position
                    for (k in 0 until step.number) {
                        val (i, j) = current.point
                        val next = when (current.direction) {
                            Direction.Left -> {
                                when {
                                    i in 0..50 && j == 50 -> {
                                        Position(149 - i to 0, Direction.Right)
                                    }

                                    i in 50..<100 && j == 50 -> {
                                        Position(100 to i - 50, Direction.Down)
                                    }

                                    i in 100..<150 && j == 0 -> {
                                        Position(149 - i to 50, Direction.Right)
                                    }

                                    i in 150..<200 && j == 0 -> {
                                        Position(0 to i - 100, Direction.Down)
                                    }

                                    else -> current.next()
                                }
                            }

                            Direction.Up -> {
                                when {
                                    i == 0 && j in 50..<100 -> {
                                        Position(j + 100 to 0, Direction.Right)
                                    }

                                    i == 0 && j in 100..<150 -> {
                                        Position(199 to j - 100, Direction.Up)
                                    }

                                    i == 100 && j in 0..<50 -> {
                                        Position(j + 50 to 50, Direction.Right)
                                    }

                                    else -> current.next()
                                }
                            }

                            Direction.Right -> {
                                when {
                                    i in 0..<50 && j == 149 -> {
                                        Position(149 - i to 99, Direction.Left)
                                    }

                                    i in 50..<100 && j == 99 -> {
                                        Position(49 to i + 50, Direction.Up)
                                    }

                                    i in 100..<150 && j == 99 -> {
                                        Position(149 - i to 149, Direction.Left)
                                    }

                                    i in 150..<200 && j == 49 -> {
                                        Position(149 to i - 100, Direction.Up)
                                    }

                                    else -> current.next()
                                }
                            }

                            Direction.Down -> {
                                when {
                                    i == 49 && j in 100..<150 -> {
                                        Position(j - 50 to 99, Direction.Left)
                                    }

                                    i == 149 && j in 50..<100 -> {
                                        Position(j + 100 to 49, Direction.Left)
                                    }

                                    i == 199 && j in 0..<50 -> {
                                        Position(0 to j + 100, Direction.Down)
                                    }

                                    else -> current.next()
                                }
                            }
                        }
                        if (map[next.point.i][next.point.j].isOpen) {
                            current = next
                        } else {
                            break
                        }
                    }
                    current
                }
            }
        }

        return position.calc()
    }

    val testInput = readInput("Year2022/Day22_test")
    check(part1(testInput).also { println("part1 test: $it") } == 6032L)

    val input = readInput("Year2022/Day22")
    println(part1(input))
    println(part2(input))
}

private enum class Tile {
    Space, Open, Wall;

    val isNotSpace get() = this != Space
    val isSpace get() = this == Space
    val isOpen get() = this == Open
}

private sealed interface Step {
    data class Move(val number: Int) : Step
    data object RTurn : Step
    data object LTurn : Step
}
