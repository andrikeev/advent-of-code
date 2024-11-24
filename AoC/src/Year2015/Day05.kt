package Year2015

import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        val vowels = "aeiou"
        val disallowed = listOf("ab", "cd", "pq", "xy")
        return input.count { str ->
            var vowelsCount = 0
            var hasPair = false
            var hasDisallowed = false
            var prev = str[0]
            if (prev in vowels) {
                vowelsCount++
            }
            for (i in 1..str.lastIndex) {
                val curr = str[i]
                if (curr in vowels) {
                    vowelsCount++
                }
                if (prev == curr) {
                    hasPair = true
                }
                if ("$prev$curr" in disallowed) {
                    hasDisallowed = true
                }
                prev = curr
            }
            vowelsCount >= 3 && hasPair && !hasDisallowed
        }
    }

    fun part2(input: List<String>): Any {
        return input.count { str ->
            val pairs = mutableMapOf<String, Int>()
            var hasPairs = false
            var hasMiddle = false
            var i = 0

            while (i < str.length - 2) {
                if (!hasPairs) {
                    for (j in i..i + 1) {
                        val cur = str.substring(j, j + 2)
                        val prev = pairs[cur]
                        if (prev != null && prev < j - 1) {
                            hasPairs = true
                        } else {
                            pairs[cur] = j
                        }
                    }
                }
                if (!hasMiddle) {
                    if (str[i] == str[i + 2]) {
                        hasMiddle = true
                    }
                }
                if (hasPairs && hasMiddle) {
                    return@count true
                }
                i++
            }

            false
        }
    }

    val input = readInput("Year2015/Day05")

    // part 1
    expect(
        part1(
            testInput(
                """
                ugknbfddgicrmopn
                aaa
                jchzalrnumimnmhp
                haegwjzuvuyypxyu
                dvszwmarrgswjxmb
                """.trimIndent()
            )
        ), 2
    )
    println(part1(input))

    // part 2
    expect(
        part2(
            testInput(
                """
                qjhvhtzxzqqjkmpb
                xxyxx
                uurcxstgmygtbstg
                ieodomkazucvgmuy
                """.trimIndent()
            )
        ), 2
    )
    println(part2(input))
}
