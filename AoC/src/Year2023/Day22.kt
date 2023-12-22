package Year2023

import readInput
import testInput

fun main() {

    fun bricks(input: List<String>): List<Brick> {
        return input.map { line ->
            val (s, e) = line.split('~').map {
                it.split(",").let { (x, y, z) -> Coords(x.toInt(), y.toInt(), z.toInt()) }
            }
            val points = (s.x..e.x).flatMap { x ->
                (s.y..e.y).flatMap { y ->
                    (s.z..e.z).map { z ->
                        Coords(x, y, z)
                    }
                }
            }
            Brick(points)
        }.sortedBy { it.points.minOf(Coords::z) }
    }

    fun Brick.fall() = Brick(points.map { p -> p.copy(z = p.z - 1) })

    fun stabilize(bricks: List<Brick>): List<Brick> {
        return buildList {
            val fallen = hashSetOf<Coords>()
            bricks.forEach { brick ->
                var current = brick
                while (true) {
                    val down = current.fall()
                    if (down.points.any { it.z <= 0 || it in fallen }) {
                        add(current)
                        fallen.addAll(current.points)
                        break
                    }
                    current = down
                }
            }
        }
    }

    fun countFalls(bricks: List<Brick>): Int {
        val fallen = hashSetOf<Coords>()
        var count = 0
        bricks.forEach { brick ->
            var current = brick
            while (true) {
                val down = current.fall()
                if (down.points.any { it.z <= 0 || it in fallen }) {
                    fallen.addAll(current.points)
                    if (current != brick) {
                        count++
                    }
                    break
                }
                current = down
            }
        }
        return count
    }

    fun part1(input: List<String>): Int {
        val bricks = bricks(input)
        val stable = stabilize(bricks)
        return stable.count { countFalls(stable.minusElement(it)) == 0 }
    }

    fun part2(input: List<String>): Int {
        val bricks = bricks(input)
        val stable = stabilize(bricks)
        return stable.sumOf { countFalls(stable.minusElement(it)) }
    }

    val testInput = testInput("""
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
    """)
    check(part1(testInput).also { println("part1 test: $it") } == 5)
    check(part2(testInput).also { println("part2 test: $it") } == 7)

    val input = readInput("Year2023/Day22")
    println(part1(input))
    println(part2(input))
}

private data class Coords(val x: Int, val y: Int, val z: Int)
private data class Brick(val points: List<Coords>)
