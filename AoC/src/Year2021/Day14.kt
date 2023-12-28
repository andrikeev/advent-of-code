package Year2021

import expect
import readInput
import testInput

fun main() {

    fun rules(input: List<String>): Map<Pair<Char, Char>, Char> {
        return input.associate {
            val (pair, insert) = it.split(" -> ")
            (pair.first() to pair.last()) to insert.single()
        }
    }

    fun count(
        pair: Pair<Char, Char>,
        rules: Map<Pair<Char, Char>, Char>,
        cache: MutableMap<Pair<Pair<Char, Char>, Int>, Map<Char, Long>>,
        steps: Int,
    ): Map<Char, Long> {
        val key = pair to steps

        if (cache.containsKey(key)) {
            return cache.getValue(key)
        }

        if (steps == 0 || !rules.containsKey(pair)) {
            cache[key] = mapOf(pair.first to 1L)
            return cache.getValue(key)
        }

        return buildMap {
            rules.getValue(pair)
                .let { listOf(pair.first to it, it to pair.second) }
                .forEach {
                    count(it, rules, cache, steps - 1).forEach { (c, v) ->
                        this[c] = this.getOrDefault(c, 0L) + v
                    }
                }
            cache[key] = this
        }
    }

    fun count(
        string: String,
        rules: Map<Pair<Char, Char>, Char>,
        steps: Int,
    ): Long {
        val result = mutableMapOf<Char, Long>()
        val cache = mutableMapOf<Pair<Pair<Char, Char>, Int>, Map<Char, Long>>()
        for (i in string.indices) {
            val l = string[i]
            if (i == string.lastIndex) {
                result[l] = result.getOrDefault(l, 0L) + 1
                break
            }
            val r = string[i + 1]
            count(l to r, rules, cache, steps).forEach { (c, v) ->
                result[c] = result.getOrDefault(c, 0L) + v
            }
        }
        return result.values.max() - result.values.min()
    }

    fun part1(input: List<String>): Any {
        val start = input.first()
        val rules = rules(input.drop(2))

        return count(start, rules, 10)
    }

    fun part2(input: List<String>): Any {
        val start = input.first()
        val rules = rules(input.drop(2))

        return count(start, rules, 40)
    }

    val testInput = testInput("""
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """)
    val input = readInput("Year2021/Day14")

    // part 1
    expect(part1(testInput), 1588L)
    println(part1(input))

    // part 2
    expect(part2(testInput), 2188189693529L)
    println(part2(input))
}
