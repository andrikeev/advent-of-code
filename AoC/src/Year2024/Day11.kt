package Year2024

import Day

private object Day11 : Day {
    private fun change(stone: Long): List<Long> {
        return if (stone == 0L) {
            listOf(1L)
        } else {
            val str = stone.toString()
            if (str.length % 2 == 0) {
                listOf(
                    str.take(str.length / 2).toLong(),
                    str.drop(str.length / 2).toLong(),
                )
            } else {
                listOf(stone * 2024)
            }
        }
    }

    override fun part1(input: List<String>): Any {
        var stones = input.first().split(' ').map(String::toLong)

        repeat(25) {
            stones = buildList {
                stones.forEach { stone ->
                    addAll(change(stone))
                }
            }
        }

        return stones.size
    }

    override fun part2(input: List<String>): Any {
        var stones = input.first()
            .split(' ')
            .map(String::toLong)
            .groupBy { it }
            .mapValues { it.value.size.toLong() }

        repeat(75) {
            stones = buildMap {
                stones.forEach { (stone, count) ->
                    change(stone).forEach {
                        put(it, (getOrDefault(it, 0) + count))
                    }
                }
            }
        }

        return stones.values.sum()
    }
}

fun main() = with(Day11) {
    test1(
        input = """
            125 17
        """.trimIndent(),
        expected = 55312,
    )
    result1()

    result2()
}
