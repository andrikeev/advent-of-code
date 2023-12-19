package Year2023

import readInput
import testInput

fun main() {

    fun workflows(input: List<String>) = buildMap {
        input.takeWhile(String::isNotEmpty)
            .forEach {
                put(
                    it.substringBefore('{'),
                    it.substringAfter('{')
                        .substringBefore('}')
                        .split(',')
                        .toMutableList(),
                )
            }
    }

    fun parts(input: List<String>) = input.dropWhile(String::isNotEmpty)
        .drop(1)
        .map { line ->
            line.substringAfter('{')
                .substringBefore('}')
                .split(',')
                .associate {
                    val (name, value) = it.split('=')
                    name to value.toInt()
                }
        }

    fun evaluate(
        workflows: Map<String, List<String>>,
        parts: Map<String, IntRange>,
        name: String,
    ): Parts {
        val rules = workflows.getValue(name)
        return rules.runningFold(
            Parts(
                accepted = listOf(),
                pending = listOf(parts),
            ),
        ) { combinations, rule ->
            val accepted = combinations.accepted.toMutableList()
            val newPending = mutableListOf<Map<String, IntRange>>()
            combinations.pending.forEach { current ->
                if (rule.contains(':')) {
                    val (condition, next) = rule.split(':')
                    val (partName, value) = condition.split('<', '>')
                    val isConditionGreaterThen = condition.contains('>')
                    val range = current.getValue(partName)
                    val inRange = current.plus(
                        partName to
                                if (isConditionGreaterThen) {
                                    maxOf(value.toInt() + 1, range.first)..range.last
                                } else {
                                    range.first..minOf(value.toInt() - 1, range.last)
                                },
                    )
                    val outOfRange = current.plus(
                        partName to
                                if (isConditionGreaterThen) {
                                    range.first..minOf(value.toInt(), range.last)
                                } else {
                                    maxOf(value.toInt(), range.first)..range.last
                                },
                    )
                    when (next) {
                        "A" -> {
                            accepted.add(inRange)
                            newPending.add(outOfRange)
                        }

                        "R" -> {
                            newPending.add(outOfRange)
                        }

                        else -> {
                            accepted.addAll(
                                evaluate(
                                    workflows,
                                    inRange,
                                    next,
                                ).accepted
                            )
                            newPending.add(outOfRange)
                        }
                    }
                } else {
                    when (rule) {
                        "A" -> accepted.add(current)
                        "R" -> newPending.add(current)
                        else -> accepted.addAll(
                            evaluate(
                                workflows,
                                current,
                                rule,
                            ).accepted
                        )
                    }
                }
            }
            Parts(accepted, newPending)
        }.last()
    }

    fun part1(input: List<String>): Int {
        val workFlows = workflows(input)
        val parts = parts(input)

        val acc = mutableListOf<Map<String, Int>>()
        parts.forEach { group ->
            var solved = false
            var workflow = workFlows.getValue("in")
            while (!solved) {
                for (i in workflow.indices) {
                    val rule = workflow[i]

                    fun next(next: String) {
                        when (next) {
                            "A" -> {
                                acc.add(group)
                                solved = true
                            }

                            "R" -> solved = true
                            else -> workflow = workFlows.getValue(next)
                        }
                    }

                    if (rule.contains(':')) {
                        val (condition, next) = rule.split(':')
                        val (partName, value) = condition.split('<', '>')
                        val partValue = group.getValue(partName)
                        if (
                            (condition.contains('<') && partValue < value.toInt()) ||
                            (condition.contains('>') && partValue > value.toInt())
                        ) {
                            next(next)
                            break
                        }
                    } else {
                        next(rule)
                        break
                    }
                }
            }
        }

        return acc.sumOf { it.values.sum() }
    }

    fun part2(input: List<String>): Long {
        val workflows = workflows(input)
        val parts = "xmas".associate { it.toString() to 1..4000 }
        return evaluate(workflows, parts, "in")
            .accepted
            .sumOf {
                it.values
                    .map { range -> range.last - range.first + 1L }
                    .reduce(Long::times)
            }
    }

    val testInput = testInput("""
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """)
    check(part1(testInput).also { println("part1 test: $it") } == 19114)
    check(part2(testInput).also { println("part2 test: $it") } == 167409079868000)

    val input = readInput("Year2023/Day19")
    println(part1(input))
    println(part2(input))
}

private data class Parts(
    val accepted: List<Map<String, IntRange>>,
    val pending: List<Map<String, IntRange>>,
)
