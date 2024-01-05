package Year2021

import CharGrid
import charGrid
import expect
import gridSize
import readInput

fun main() {

    fun CharGrid.enhance(alg: String, times: Int): Int {
        var result = this
        var infiniteValue = '.'
        repeat(times) {
            val (n, m) = result.gridSize()
            result = Array(n + 2) { i ->
                CharArray(m + 2) { j ->
                    val r = (i-2..i).flatMap { a ->
                        (j-2..j).map { b ->
                            val c = if (a in 0..<n && b in 0..<m) result[a][b] else infiniteValue
                            if (c == '#') '1' else '0'
                        }
                    }
                        .joinToString(separator = "")
                        .toInt(2)
                    alg[r]
                }
            }
            infiniteValue = CharArray(9) { infiniteValue }
                .map { if (it == '#') '1' else '0' }
                .joinToString(separator = "")
                .toInt(2)
                .let { alg[it] }
        }

        return result.sumOf { it.count { c -> c == '#' } }
    }

    fun part1(input: List<String>): Any {
        val alg = input.first()
        val (grid) = input.drop(2).charGrid()
        return grid.enhance(alg, 2)
    }

    fun part2(input: List<String>): Any {
        val alg = input.first()
        val (grid) = input.drop(2).charGrid()
        return grid.enhance(alg, 50)
    }

    val testInput = readInput("Year2021/Day20_test")
    val input = readInput("Year2021/Day20")

    // part 1
    expect(part1(testInput), 35)
    println(part1(input))

    // part 2
    expect(part2(testInput), 3351)
    println(part2(input))
}
