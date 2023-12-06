package Year2023

import readInput
import testInput

fun main() {

    fun readSeeds(input: String) = input
        .substringAfter("seeds: ")
        .split(" ")
        .map(String::toLong)

    fun readSeedsRanges(input: String) = readSeeds(input)
        .chunked(2)
        .map { (f, s) -> LongRange(f, f + s) }

    fun readConverters(input: List<String>) = buildList {
        val converters = mutableListOf<Converter>()
        var name = ""
        input.forEach { line ->
            if (line.isNotEmpty()) {
                if (line.contains("map")) {
                    name = line.substringBefore(" map")
                } else {
                    val (dst, src, len) = line.split(" ").map(String::toLong)
                    converters.add(Converter(src..<src + len, dst - src))
                }
            } else {
                add(Mapper(converters.sortedBy { it.range.first }, name))
                converters.clear()
            }
        }
        add(Mapper(converters.sortedBy { it.range.first }, name))
    }

    fun Long.convert(steps: List<Mapper>): Long {
        var value = this
        steps.forEach { (converters) ->
            converters.firstOrNull { value in it.range }?.let { value += it.shift }
        }
        return value
    }

    fun LongRange.convert(converters: List<Converter>): List<LongRange> {
        return buildList {
            val split = mutableListOf<LongRange>()
            for (converter in converters) {
                val start = maxOf(first, converter.range.first)
                val end = minOf(last, converter.range.last)
                if (start <= end) {
                    split.add(start..end)
                    add(start + converter.shift..end + converter.shift)
                }
            }
            split.sortBy(LongRange::first)
            var x = first
            split.forEach {
                if (it.first > x) {
                    add(x..<it.first)
                }
                x = it.last + 1
            }
            if (x <= last) {
                add(x..last)
            }
        }
    }

    fun List<LongRange>.convert(steps: List<Mapper>): List<LongRange> {
        var seeds = this
        steps.forEach { (converters) ->
            val converted = seeds.map { it.convert(converters) }
            seeds = converted.flatten()
        }
        return seeds
    }


    fun part1(input: List<String>): Long {
        val seeds = readSeeds(input.first())
        val steps = readConverters(input.drop(2))
        return seeds.minOf { seed -> seed.convert(steps) }
    }

    fun part2(input: List<String>): Long {
        val seeds = readSeedsRanges(input.first())
        val steps = readConverters(input.drop(2))
        return seeds.convert(steps).minOf { it.first }
    }

    val testInput = testInput("""
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent())
    check(part1(testInput).also { println("part1 test: $it") } == 35L)
    check(part2(testInput).also { println("part2 test: $it") } == 46L)

    val input = readInput("Year2023/Day05")
    println(part1(input))
    println(part2(input))
}

private data class Mapper(val converters: List<Converter>, val name: String)
private data class Converter(val range: LongRange, val shift: Long)