package Year2023

import CharGrid
import Point
import adjacentSides
import charGrid
import expect
import gridSize
import readInput
import testInput

fun main() {

    fun CharGrid.start(): Point {
        return indexOfFirst { 'S' in it }.let { i ->
            val j = this[i].indexOf('S')
            Point(i, j)
        }
    }

    fun CharGrid.count(start: Point, steps: Int): Int {
        val (n, m) = gridSize()
        val toVisit = mutableListOf(start)
        repeat(steps) {
            val newToVisit = mutableSetOf<Point>()
            while (toVisit.isNotEmpty()) {
                val p = toVisit.removeFirst()
                p.adjacentSides()
                    .filter { (i, j) -> i in 0 until n && j in 0 until m }
                    .filter { (i, j) -> this[i][j] != '#' }
                    .filterNot { it in toVisit }
                    .forEach(newToVisit::add)
            }
            toVisit.addAll(newToVisit)
        }
        return toVisit.size
    }

    fun part1(input: List<String>, steps: Int = 64): Any {
        val (grid) = input.charGrid()
        val start = grid.start()
        return grid.count(start, steps)
    }

    fun part2(input: List<String>): Any {
        val (grid, size) = input.charGrid()
        val start = grid.start()
        val steps = 26501365
        val repeats = (steps / size) - 1

        val odds = (1..(repeats / 2)).fold(1L) { acc, i -> acc + i * 8 }
        val evens = (0..repeats / 2).fold(0L) { acc, i -> acc + i * 8 + 4 }

        val oddCount = grid.count(start, size * 2 + 1)
        val evenCount = grid.count(start, size * 2)

        val topCorner = grid.count(start.copy(first = 0), size - 1)
        val bottomCorner = grid.count(start.copy(first = size - 1), size - 1)
        val leftCorner = grid.count(start.copy(second = 0), size - 1)
        val rightCorner = grid.count(start.copy(second = size - 1), size - 1)

        val smallCorners = (((repeats / 2 + 1) * 8 + 4) - 4) / 4
        val smallTopRight = grid.count(Point(0, 0), size / 2 - 1)
        val smallTopLeft = grid.count(Point(0, size - 1), size / 2 - 1)
        val smallBottomRight = grid.count(Point(size - 1, 0), size / 2 - 1)
        val smallBottomLeft = grid.count(Point(size - 1, size - 1), size / 2 - 1)

        val largeCorners = (((repeats / 2 + 1) * 8 + 4) - 4) / 4 - 1
        val largeTopRight = grid.count(Point(0, 0), size * 3 / 2 - 1)
        val largeTopLeft = grid.count(Point(0, size - 1), size * 3 / 2 - 1)
        val largeBottomRight = grid.count(Point(size - 1, 0), size * 3 / 2 - 1)
        val largeBottomLeft = grid.count(Point(size - 1, size - 1), size * 3 / 2 - 1)

        return odds * oddCount +
                evens * evenCount +
                topCorner +
                bottomCorner +
                leftCorner +
                rightCorner +
                smallCorners * (smallTopRight + smallTopLeft + smallBottomRight + smallBottomLeft) +
                largeCorners * (largeTopRight + largeTopLeft + largeBottomRight + largeBottomLeft)
    }

    val testInput = testInput("""
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........
    """)
    val input = readInput("Year2023/Day21")

    // part 1
    expect(part1(testInput, 6), 16)
    println(part1(input))

    // part 2
    // Not working on test input.
    // expect(part2(testInput, 6), 16)
    println(part2(input))
}
