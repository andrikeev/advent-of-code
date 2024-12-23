package Year2024

import Day
import split

private object Day19 : Day {
    override fun part1(input: List<String>): Any {
        val (a, designs) = input.split(String::isEmpty)
        val patterns = a.flatMap { it.split(", ") }

        fun isPossible(design: String): Boolean {
            return if (design.isEmpty()) {
                true
            } else {
                patterns
                    .filter(design::startsWith)
                    .any { pattern ->
                        isPossible(design.drop(pattern.length))
                    }
            }
        }

        return designs.count(::isPossible)
    }

    override fun part2(input: List<String>): Any {
        val (a, designs) = input.split(String::isEmpty)
        val patterns = a.flatMap { it.split(", ") }
        val cache = mutableMapOf<String, Long>()

        fun variants(design: String): Long {
            return when {
                design.isEmpty() -> 1L
                cache.containsKey(design) -> cache.getValue(design)
                else -> {
                    patterns.sumOf { pattern ->
                        if (design.startsWith(pattern)) {
                            variants(design.drop(pattern.length))
                        } else {
                            0L
                        }
                    }.also { cache[design] = it }
                }
            }
        }

        return designs.sumOf(::variants)
    }
}

fun main() = with(Day19) {
    test1(
        input = """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """.trimIndent(),
        expected = 6,
    )
    result1()

    test2(
        input = """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """.trimIndent(),
        expected = 16L,
    )
    result2()
}
