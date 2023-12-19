package Year2023

import Direction
import Point
import i
import intGrid
import j
import moveTo
import readInput
import testInput
import turnLeft
import turnRight
import java.util.*

fun main() {
    data class Position(val p: Point, val d: Direction, val s: Int)

    fun findPath(
        input: List<String>,
        minSteps: Int = 1,
        forwardSteps: Int = 3,
        turnSteps: Int = 1
    ): Int {
        val (grid, n, m) = input.intGrid()

        val start = Position(Point(0, 0), Direction.Right, 0)
        val finish = Point(n - 1, m - 1)

        val visited = mutableSetOf(start)
        val toVisit = PriorityQueue(compareBy(Pair<Position, Int>::second))
            .apply { add(start to 0) }

        while (toVisit.isNotEmpty()) {
            val (position, loss) = toVisit.poll()
            val (p, d, s) = position

            if (p == finish && s >= minSteps) {
                return loss
            }

            buildList {
                if (s < forwardSteps) {
                    add(d)
                }
                if (s >= turnSteps) {
                    add(d.turnLeft())
                    add(d.turnRight())
                }
            }
                .map { d1 ->
                    val p1 = p moveTo d1
                    val s1 = if (d == d1) s + 1 else 1
                    Position(p1, d1, s1)
                }
                .filter { (p1) -> p1.i in 0 until n && p1.j in 0 until m }
                .filterNot { it in visited }
                .forEach { pos ->
                    val l1 = loss + grid[pos.p.i][pos.p.j]
                    visited.add(pos)
                    toVisit.add(pos to l1)
                }
        }
        error("!!!")
    }

    fun part1(input: List<String>) = findPath(input)

    fun part2(input: List<String>) = findPath(input, 4, 10, 4)

    val testInput = testInput("""
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533
    """)
    check(part1(testInput).also { println("part1 test: $it") } == 102)
    check(part2(testInput).also { println("part2 test: $it") } == 94)

    val input = readInput("Year2023/Day17")
    println(part1(input))
    println(part2(input))
}
