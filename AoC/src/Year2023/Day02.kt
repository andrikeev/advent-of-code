package Year2023

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val r = 12
        val g = 13
        val b = 14
        return input.sumOf { line ->
            val id = line
                .substringAfter("Game ")
                .substringBefore(":").toInt()

            val canPlay = line.substringAfter(": ")
                .split("; ")
                .map { round ->
                    var red = 0
                    var green = 0
                    var blue = 0
                    round.split(", ").forEach {
                        val (num, color) = it.split(" ")
                        when (color) {
                            "red" -> red = num.toInt()
                            "green" -> green = num.toInt()
                            "blue" -> blue = num.toInt()
                        }
                    }
                    Triple(red, green, blue)
                }
                .all { (red, green, blue) -> red <= r && green <= g && blue <= b }

            if (canPlay) id else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            var r = 0
            var g = 0
            var b = 0

            line.substringAfter(": ")
                .split("; ")
                .forEach { round ->
                    round.split(", ").forEach {
                        val (num, color) = it.split(" ")
                        when (color) {
                            "red" -> r = maxOf(r, num.toInt())
                            "green" -> g = maxOf(g, num.toInt())
                            "blue" -> b = maxOf(b, num.toInt())
                        }
                    }
                }

            r * g * b
        }
    }

    val testInput = readInput("Year2023/Day02_test")
    check(part1(testInput).also { println("part1 test: $it") } == 8)
    check(part2(testInput).also { println("part2 test: $it") } == 2286)

    val input = readInput("Year2023/Day02")
    println(part1(input))
    println(part2(input))
}
