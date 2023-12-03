package Year2022

import readInput
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

    fun part1(input: List<String>): Long {
        val map = Map.parse(input.takeWhile(String::isNotBlank))
        val path = Path.parse(input.last())
        val position = PathPosition(map.start(), StepDirection.start)

        path.steps.forEach { step ->
            when (step) {
                is Step.LTurn -> position.turnLeft()
                is Step.RTurn -> position.turnRight()
                is Step.Move -> position.moveTo(map.step(position.position, position.direction, step.number))
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
    fun start(): Pair<Int, Int> = 0 to tiles.first().indexOfFirst(Tile::isNotSpace)

    fun step(
        start: Pair<Int, Int>,
        direction: StepDirection,
        moves: Int,
    ): Pair<Int, Int> {
        var (i, j) = start
        for (m in 0 until moves) {
            val nextPos = when (direction) {
                StepDirection.Right -> nextRight(i to j)
                StepDirection.Down -> nextDown(i to j)
                StepDirection.Left -> nextLeft(i to j)
                StepDirection.Up -> nextUp(i to j)
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

    private fun get(pos: Pair<Int, Int>) = tiles[pos.first][pos.second]

    private fun nextLeft(from: Pair<Int, Int>): Pair<Int, Int> {
        var (i, j) = from
        val row = tiles[i]
        val rowSize = row.size
        j = (rowSize + j - 1) % rowSize
        while (row[j].isSpace) {
            j = (rowSize + j - 1) % rowSize
        }
        return i to j
    }

    private fun nextUp(from: Pair<Int, Int>): Pair<Int, Int> {
        var (i, j) = from
        val columnSize = tiles.size
        i = (columnSize + i - 1) % columnSize
        while (tiles[i][j].isSpace) {
            i = (columnSize + i - 1) % columnSize
        }
        return i to j
    }

    private fun nextRight(from: Pair<Int, Int>): Pair<Int, Int> {
        var (i, j) = from
        val row = tiles[i]
        val rowSize = row.size
        j = (j + 1) % rowSize
        while (row[j].isSpace) {
            j = (j + 1) % rowSize
        }
        return i to j
    }

    private fun nextDown(from: Pair<Int, Int>): Pair<Int, Int> {
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
    object RTurn : Step
    object LTurn : Step
}

private enum class StepDirection {
    Right, Down, Left, Up;

    companion object {
        val start = Right
    }
}

private fun StepDirection.turnLeft() = when (this) {
    StepDirection.Left -> StepDirection.Down
    StepDirection.Up -> StepDirection.Left
    StepDirection.Right -> StepDirection.Up
    StepDirection.Down -> StepDirection.Right
}

private fun StepDirection.turnRight() = when (this) {
    StepDirection.Left -> StepDirection.Up
    StepDirection.Up -> StepDirection.Right
    StepDirection.Right -> StepDirection.Down
    StepDirection.Down -> StepDirection.Left
}

private class PathPosition(
    start: Pair<Int, Int>,
    direction: StepDirection,
) {
    var position: Pair<Int, Int> = start
        private set
    var direction: StepDirection = direction
        private set

    fun turnLeft() {
        direction = direction.turnLeft()
    }

    fun turnRight() {
        direction = direction.turnRight()
    }

    fun moveTo(position: Pair<Int, Int>) {
        this.position = position
    }

    fun calc(): Long {
        return 1000L * (position.first + 1) + 4 * (position.second + 1) + direction.ordinal
    }
}
