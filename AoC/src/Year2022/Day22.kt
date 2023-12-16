package Year2022

import Direction
import Point
import Position
import direction
import i
import j
import point
import readInput
import turnLeft
import turnRight
import kotlin.collections.List
import kotlin.collections.buildList
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.collections.indexOfFirst
import kotlin.collections.last
import kotlin.collections.map
import kotlin.collections.maxOf
import kotlin.collections.takeWhile
import kotlin.collections.toTypedArray

fun main() {

    fun Position.turnLeft(): Position = copy(second = direction.turnLeft())
    fun Position.turnRight(): Position = copy(second = direction.turnRight())
    fun Position.moveTo(point: Point): Position = copy(first = point)
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
        val map = Map.parse(input.takeWhile(String::isNotBlank))
        val path = Path.parse(input.last())
        var position = Position(map.start(), Direction.Right)

        path.steps.forEach { step ->
            position = when (step) {
                is Step.LTurn -> position.turnLeft()
                is Step.RTurn -> position.turnRight()
                is Step.Move -> position.moveTo(map.step(position.point, position.direction, step.number))
            }
        }

        return position.calc()
    }

    fun part2(input: List<String>): Long {
        TODO()
    }

    val testInput = readInput("Year2022/Day22_test")
    check(part1(testInput).also { println("part1 test: $it") } == 6032L)
    //check(part2(testInput).also { println("part2 test: $it") } == 5031L)

    val input = readInput("Year2022/Day22")
    println(part1(input))
    //println(part2(input))
}

private class Map(private val tiles: Array<Array<Tile>>) {
    fun start(): Point = 0 to tiles.first().indexOfFirst(Tile::isNotSpace)

    fun step(
        start: Point,
        direction: Direction,
        moves: Int,
    ): Point {
        var (i, j) = start
        for (m in 0 until moves) {
            val nextPos = when (direction) {
                Direction.Right -> nextRight(i to j)
                Direction.Down -> nextDown(i to j)
                Direction.Left -> nextLeft(i to j)
                Direction.Up -> nextUp(i to j)
            }
            val nextTile = get(nextPos)
            if (nextTile.isOpen) {
                i = nextPos.first
                j = nextPos.second
            } else {
                break
            }
        }
        return i to j
    }

    private fun get(pos: Point) = tiles[pos.first][pos.second]

    private fun nextLeft(from: Point): Point {
        var (i, j) = from
        val row = tiles[i]
        val rowSize = row.size
        j = (rowSize + j - 1) % rowSize
        while (row[j].isSpace) {
            j = (rowSize + j - 1) % rowSize
        }
        return i to j
    }

    private fun nextUp(from: Point): Point {
        var (i, j) = from
        val columnSize = tiles.size
        i = (columnSize + i - 1) % columnSize
        while (tiles[i][j].isSpace) {
            i = (columnSize + i - 1) % columnSize
        }
        return i to j
    }

    private fun nextRight(from: Point): Point {
        var (i, j) = from
        val row = tiles[i]
        val rowSize = row.size
        j = (j + 1) % rowSize
        while (row[j].isSpace) {
            j = (j + 1) % rowSize
        }
        return i to j
    }

    private fun nextDown(from: Point): Point {
        var (i, j) = from
        val columnSize = tiles.size
        i = (i + 1) % columnSize
        while (tiles[i][j].isSpace) {
            i = (i + 1) % columnSize
        }
        return i to j
    }

    companion object {
        fun parse(input: List<String>): Map {
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
            }.toTypedArray().let(::Map)
        }
    }
}

private enum class Tile {
    Space, Open, Wall;

    val isNotSpace get() = this != Space
    val isSpace get() = this == Space
    val isOpen get() = this == Open
}

private data class Path(val steps: List<Step>) {
    companion object {
        fun parse(input: String): Path {
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
            }.let(::Path)
        }
    }
}

private sealed interface Step {
    data class Move(val number: Int) : Step
    data object RTurn : Step
    data object LTurn : Step
}
