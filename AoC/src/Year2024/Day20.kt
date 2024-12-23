package Year2024

import CharGrid
import Day
import adjacentSides
import bfs
import charGrid
import firstPosition
import get
import inside
import manhattanDistanceTo

private object Day20 : Day {
    private fun CharGrid.path() = buildList {
        val start = firstPosition { it == 'S' }
        val end = firstPosition { it == 'E' }
        bfs(
            start = listOf(start),
            next = { current ->
                current.last()
                    .adjacentSides()
                    .filter { it inside this@path }
                    .filter { this@path[it] != '#' }
                    .map { current + it }
            },
            key = { it.last() },
            visit = { current ->
                if (current.last() == end) {
                    addAll(current)
                }
            },
            finished = { current ->
                current.last() == end
            },
        )
    }

    override fun part1(input: List<String>): Any {
        val (map) = input.charGrid()
        val path = map.path()

        var result = 0
        for (i in path.indices) {
            val current = path[i]
            for (j in i + 1..path.lastIndex) {
                val next = path[j]
                val cheatDistance = current manhattanDistanceTo next
                if (cheatDistance <= 2 && j - i >= 100 + cheatDistance) {
                    result++
                }
            }
        }

        return result
    }

    override fun part2(input: List<String>): Any {
        val (map) = input.charGrid()
        val path = map.path()

        var result = 0

        for (i in path.indices) {
            val current = path[i]
            for (j in i + 1..path.lastIndex) {
                val next = path[j]
                val cheatDistance = current manhattanDistanceTo next
                if (cheatDistance <= 20 && j - i >= 100 + cheatDistance) {
                    result++
                }
            }
        }

        return result
    }
}

fun main() = with(Day20) {
    result1()
    result2()
}
