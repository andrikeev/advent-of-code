package Year2022

import Point
import i
import j
import manhattanDistanceTo
import readInput
import kotlin.math.abs

fun main() {
    val regex = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")

    fun String.parse(): Pair<Point, Point> {
        val (sX, sY, bX, bY) = regex.find(this)!!.groupValues.drop(1).map(String::toInt)
        return Pair(sX, sY) to Pair(bX, bY)
    }

    fun part1(input: List<String>, level: Int): Int {
        val covered = mutableSetOf<Point>()
        input.forEach { line ->
            val (sensor, beacon) = line.parse()
            val distance = sensor.manhattanDistanceTo(beacon)
            for (x in (sensor.i - distance)..(sensor.j + distance)) {
                val point = Pair(x, level)
                if (point != beacon && sensor.manhattanDistanceTo(point) <= distance) {
                    covered.add(point)
                }
            }
        }
        return covered.size
    }

    fun part2(input: List<String>, maxLevel: Int): Long {
        val sensors = input.map { line ->
            val (sensor, beacon) = line.parse()
            sensor to (sensor.manhattanDistanceTo(beacon))
        }.sortedBy { (sensor, _) -> sensor.i }
        for (y in 0..maxLevel) {
            var x = 0
            sensors.forEach { (sensor, distance) ->
                if ((sensor.manhattanDistanceTo(Point(x, y))) <= distance) {
                    x = sensor.i + distance - abs(sensor.j - y) + 1
                }
            }
            if (x <= maxLevel) {
                return x * 4_000_000L + y
            }
        }
        error("Not found")
    }

    val testInput = readInput("Year2022/Day15_test")
    check(part1(testInput, 10).also { println("part1 test: $it") } == 26)
    check(part2(testInput, 20).also { println("part2 test: $it") } == 56_000_011L)

    val input = readInput("Year2022/Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}
