package Year2021

import expect
import readInput
import testInput

fun main() {

    val start = "start"
    val end = "end"
    fun String.isBig() = all(Char::isUpperCase)

    fun parseGraph(input: List<String>): Map<String, Set<String>> {
        return buildMap<String, MutableSet<String>> {
            input.forEach { line ->
                val (src, dst) = line.split('-')
                getOrPut(src, ::mutableSetOf).add(dst)
                getOrPut(dst, ::mutableSetOf).add(src)
            }
        }
    }

    fun countPaths(graph: Map<String, Set<String>>, allowVisitTwice: Boolean = false): Int {
        val visited = mutableSetOf<List<String>>()
        var toVisit = listOf(listOf(start))
        var count = 0
        while (toVisit.isNotEmpty()) {
            val newToVisit = mutableListOf<List<String>>()
            toVisit.forEach { path ->
                val last = path.last()
                if (last == end) {
                    count++
                    visited.add(path)
                } else {
                    val visitedCaves = path.toSet()
                    val canVisitTwice = allowVisitTwice && path
                        .filterNot(String::isBig)
                        .none { cave -> path.count { visited -> visited == cave } > 1 }
                    graph
                        .getValue(last)
                        .filter { it != start }
                        .filter { it.isBig() || it !in visitedCaves || canVisitTwice }
                        .map { path + it }
                        .filter { it !in visited }
                        .forEach(newToVisit::add)
                }
            }
            toVisit = newToVisit
        }

        return count
    }

    fun part1(input: List<String>): Any {
        return countPaths(parseGraph(input))
    }

    fun part2(input: List<String>): Any {
        return countPaths(parseGraph(input), true)
    }

    val testInput = testInput("""
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """)
    val input = readInput("Year2021/Day12")

    // part 1
    expect(part1(testInput), 10)
    println(part1(input))

    // part 2
    expect(part2(testInput), 36)
    println(part2(input))
}
