package Year2023

import readInput
import testInput

fun main() {

    fun count(
        input: String,
        nums: List<Int>,
        cache: MutableMap<Pair<String, List<Int>>, Long> = mutableMapOf(),
    ): Long {
        when {
            input.isEmpty() && nums.isEmpty() -> return 1
            input.isEmpty() && nums.isNotEmpty() -> return 0
            nums.isEmpty() && input.contains('#') -> return 0
            nums.isEmpty() && !input.contains('#') -> return 1
        }

        val key = input to nums
        if (cache.containsKey(key)) {
            return cache.getValue(key)
        }

        var result = 0L

        val c = input.first()

        if (c == '.' || c == '?') {
            result += count(input.drop(1), nums, cache)
        }

        if (c == '#' || c == '?') {
            val n = nums.first()
            if (n <= input.length && !input.take(n).contains('.') && (input.length == n || input[n] != '#')) {
                result += count(input.drop(n + 1), nums.drop(1), cache)
            }
        }

        cache[key] = result
        return result
    }

    fun part1(input: List<String>): Long {
        return input.sumOf {
            val (f, s) = it.split(' ')
            count(f, s.split(',').map(String::toInt))
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf {
            val (f, s) = it.split(' ')
            count(
                (0..4).joinToString("?") { f },
                (0..4).joinToString(",") { s }.split(',').map(String::toInt),
            )
        }
    }

    val testInput = testInput("""
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent())
    check(part1(testInput).also { println("part1 test: $it") } == 21L)
    check(part2(testInput).also { println("part2 test: $it") } == 525152L)

    val input = readInput("Year2023/Day12")
    println(part1(input))
    println(part2(input))
}
