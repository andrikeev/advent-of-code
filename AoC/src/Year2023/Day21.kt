package Year2023

import Point
import adjacentSides
import charGrid
import readInput
import testInput

fun main() {

    fun part1(input: List<String>, steps: Int = 64): Int {
        val (grid, n, m) = input.charGrid()
        val i = grid.indexOfFirst { 'S' in it }
        val j = grid[i].indexOf('S')
        val start = Point(i, j)

        val toVisit = mutableListOf(start)
        repeat(steps) {
            val newToVisit = mutableSetOf<Point>()
            while (toVisit.isNotEmpty()) {
                val p = toVisit.removeFirst()
                p.adjacentSides()
                    .filter { (i, j) -> i in 0 until n && j in 0 until m }
                    .filter { (i, j) -> grid[i][j] != '#' }
                    .filterNot { it in toVisit }
                    .forEach(newToVisit::add)
            }
            toVisit.addAll(newToVisit)
        }
        return toVisit.size
    }

    fun part2(input: List<String>, steps: Int = 26501365): Int {
        TODO()
    }

    val testInput = testInput(
        """
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
    """.trimIndent()
    )
    check(part1(testInput, 6).also { println("part1 test: $it") } == 16)
//    check(part2(testInput, 6).also { println("part2 test: $it") } == 16)
//    check(part2(testInput, 10).also { println("part2 test: $it") } == 50)
//    check(part2(testInput, 50).also { println("part2 test: $it") } == 1594)

    val input = readInput("Year2023/Day21")
    println(part1(input))
//    println(part2(input))
}
