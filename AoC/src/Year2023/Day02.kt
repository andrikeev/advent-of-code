package Year2023

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
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

    fun part2(input: List<String>): Any {
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

    val testInput = testInput("""
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """)
    val input = readInput("Year2023/Day02")

    // part 1
    expect(part1(testInput), 8)
    println(part1(input))

    // part 2
    expect(part2(testInput), 2286)
    println(part2(input))
}
