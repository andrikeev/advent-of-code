package Year2023

import charGrid
import expect
import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Any {
        val (grid, n, m) = input.charGrid()

        var res = 0


        for (i in 0 until n) {
            for (j in 0 until m) {
                if (grid[i][j] == 'O') {
                    var k = i
                    var s = 0
                    while (k > 0 && grid[k - 1][j] != '#') {
                        k--
                        if (grid[k][j] == 'O') {
                            s++
                        }
                    }
                    res += n - k - s
                }
            }
        }

        return res
    }

    fun part2(input: List<String>): Any {
        val (grid, n, m) = input.charGrid()

        fun cycle() {
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (grid[i][j] == 'O') {
                        var k = i
                        while (k > 0 && grid[k - 1][j] == '.') {
                            k--
                        }
                        grid[i][j] = '.'
                        grid[k][j] = 'O'
                    }
                }
            }
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (grid[i][j] == 'O') {
                        var k = j
                        while (k > 0 && grid[i][k - 1] == '.') {
                            k--
                        }
                        grid[i][j] = '.'
                        grid[i][k] = 'O'
                    }
                }
            }
            for (i in n - 1 downTo 0) {
                for (j in 0 until m) {
                    if (grid[i][j] == 'O') {
                        var k = i
                        while (k < n - 1 && grid[k + 1][j] == '.') {
                            k++
                        }
                        grid[i][j] = '.'
                        grid[k][j] = 'O'
                    }
                }
            }
            for (i in 0 until n) {
                for (j in m - 1 downTo 0) {
                    if (grid[i][j] == 'O') {
                        var k = j
                        while (k < m - 1 && grid[i][k + 1] == '.') {
                            k++
                        }
                        grid[i][j] = '.'
                        grid[i][k] = 'O'
                    }
                }
            }
        }

        fun count(): Int {
            var res = 0
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (grid[i][j] == 'O') {
                        res += n - i
                    }
                }
            }
            return res
        }

        var i = 0
        val map = mutableMapOf<String, Pair<Int, Int>>()
        while (true) {
            val key = grid.joinToString { it.concatToString() }

            if (map.containsKey(key)) {
                val (start) = map.getValue(key)
                val finish = start + (1_000_000_000 - start) % (i - start)
                return map.filterValues { (j) -> j == finish }.values.first().second
            }

            map[key] = i to count()

            i++

            cycle()
        }
    }

    val testInput = testInput("""
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """)
    val input = readInput("Year2023/Day14")

    // part 1
    expect(part1(testInput), 136)
    println(part1(input))

    // part 2
    expect(part2(testInput), 64)
    println(part2(input))
}
