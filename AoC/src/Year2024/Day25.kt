package Year2024

import Day
import split

private object Day25 : Day {
    override fun part1(input: List<String>): Any {
        val blocks = input.split(String::isEmpty)

        val keys = mutableListOf<Array<Int>>()
        val locks = mutableListOf<Array<Int>>()

        blocks.forEach { block ->
            val heights = Array(5) { 0 }
            block.forEach { line ->
                line.forEachIndexed { i, c ->
                    if (c == '#') {
                        heights[i]++
                    }
                }
            }
            if (block.first().contains('#')) {
                keys.add(heights)
            } else {
                locks.add(heights)
            }
        }

        var result = 0
        keys.forEach { key ->
            locks.forEach { lock ->
                var fit = true
                for (i in key.indices) {
                    if (key[i] + lock[i] > 7) {
                        fit = false
                        break
                    }
                }
                if (fit) {
                    result++
                }
            }
        }
        return result
    }

    override fun part2(input: List<String>): Any {
        return 0
    }
}

fun main() = with(Day25) {
    test1(
        input = """
            #####
            .####
            .####
            .####
            .#.#.
            .#...
            .....

            #####
            ##.##
            .#.##
            ...##
            ...#.
            ...#.
            .....

            .....
            #....
            #....
            #...#
            #.#.#
            #.###
            #####

            .....
            .....
            #.#..
            ###..
            ###.#
            ###.#
            #####

            .....
            .....
            .....
            #....
            #.#..
            #.#.#
            #####
        """.trimIndent(),
        expected = 3,
    )
    result1()
}
