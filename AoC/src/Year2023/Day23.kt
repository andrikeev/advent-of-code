package Year2023

import CharGrid
import Direction
import Point
import charGrid
import expect
import gridSize
import i
import j
import moveTo
import readInput
import testInput

fun main() {

    fun findPath(
        grid: CharGrid,
        start: Point,
        finish: Point,
        visited: MutableSet<Point>,
        steps: Int,
        slippery: Boolean = true,
    ): Int {
        val (n, m) = grid.gridSize()

        if (start == finish) {
            return steps
        }

        val nextPoints = buildList {
            if (slippery) {
                when (grid[start.i][start.j]) {
                    '>' -> add(Direction.Right)
                    '<' -> add(Direction.Left)
                    'v' -> add(Direction.Down)
                    '^' -> add(Direction.Up)
                    else -> addAll(Direction.entries)
                }
            } else {
                addAll(Direction.entries)
            }
        }
            .asSequence()
            .map { start moveTo it }
            .filter { (i, j) -> i in 0 until n && j in 0 until m }
            .filter { (i, j) -> grid[i][j] != '#' }
            .filterNot { it in visited }
            .toList()

        visited.add(start)
        val distances = nextPoints.map { p -> findPath(grid, p, finish, visited, steps + 1, slippery) }
        visited.remove(start)

        return distances.maxOrNull() ?: -1
    }

    fun part1(input: List<String>): Any {
        val (grid, n, m) = input.charGrid()
        val start = Point(0, 1)
        val finish = Point(n - 1, m - 2)

        return findPath(grid, start, finish, mutableSetOf(start), 0)
    }

    fun part2(input: List<String>): Any {
        val (grid, n, m) = input.charGrid()
        val start = Point(0, 1)
        val finish = Point(n - 1, m - 2)

        return findPath(grid, start, finish, mutableSetOf(start), 0, false)
    }

    val testInput = testInput("""
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """)
    val input = readInput("Year2023/Day23")

    // part 1
    expect(part1(testInput), 94)
    println(part1(input))

    // part 2
    expect(part2(testInput), 154)
    println(part2(input))
}
