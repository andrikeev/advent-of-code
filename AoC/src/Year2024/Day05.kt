package Year2024

import Day

private object Day05 : Day {
    override fun part1(input: List<String>): Any {
        var result = 0

        val rules = buildMap<Int, MutableList<Int>> {
            input.takeWhile(String::isNotEmpty).forEach { rule ->
                val (a, b) = rule.split('|').map(String::toInt)
                getOrPut(a) { mutableListOf() }.add(b)
            }
        }

        val updates = buildList {
            input.dropWhile(String::isNotEmpty).drop(1).forEach { update ->
                add(update.split(',').map(String::toInt))
            }
        }

        for (update in updates) {
            var correct = true
            for (i in update.indices) {
                val prev = update.subList(0, i)
                val rule = rules[update[i]].orEmpty()
                if (prev.intersect(rule.toSet()).isNotEmpty()) {
                    correct = false
                    break
                }
            }
            if (correct) {
                result += update[update.size / 2]
            }
        }

        return result
    }

    override fun part2(input: List<String>): Any {
        var result = 0

        val rules = buildMap<Int, MutableList<Int>> {
            input.takeWhile(String::isNotEmpty).forEach { rule ->
                val (a, b) = rule.split('|').map(String::toInt)
                getOrPut(a) { mutableListOf() }.add(b)
            }
        }

        val updates = buildList {
            input.dropWhile(String::isNotEmpty).drop(1).forEach { update ->
                add(update.split(',').map(String::toInt))
            }
        }

        for (update in updates) {
            var correct = true
            for (i in update.indices) {
                val prev = update.subList(0, i)
                val rule = rules[update[i]].orEmpty()
                if (prev.intersect(rule.toSet()).isNotEmpty()) {
                    correct = false
                    break
                }
            }
            if (!correct) {
                val corrected = update.sortedWith { a, b ->
                    if (rules[a].orEmpty().contains(b)) {
                        -1
                    } else if (rules[b].orEmpty().contains(a)) {
                        1
                    } else {
                        0
                    }
                }
                result += corrected[corrected.size / 2]
            }
        }

        return result
    }
}

fun main() = with(Day05) {
    val testInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13
        
        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()

    test1(testInput, 143)
    result1()

    test2(testInput, 123)
    result2()
}
