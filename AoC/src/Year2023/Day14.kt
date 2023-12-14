package Year2023

import readInput
import testInput

fun main() {

    fun part1(input: List<String>): Int {
        val list = Array(input.size) { input[it].toCharArray() }
        val n = input.size
        val m = input.first().length

        var res = 0


        for (i in 0 until n) {
            for (j in 0 until m) {
                if (list[i][j] == 'O') {
                    var k = i
                    var s = 0
                    while (k > 0 && list[k - 1][j] != '#') {
                        k--
                        if (list[k][j] == 'O') {
                            s++
                        }
                    }
                    res += n - k - s
                }
            }
        }

        return res
    }

    fun part2(input: List<String>): Int {
        val list = Array(input.size) { input[it].toCharArray() }
        val n = input.size
        val m = input.first().length

        fun cycle() {
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (list[i][j] == 'O') {
                        var k = i
                        while (k > 0 && list[k - 1][j] == '.') {
                            k--
                        }
                        list[i][j] = '.'
                        list[k][j] = 'O'
                    }
                }
            }
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (list[i][j] == 'O') {
                        var k = j
                        while (k > 0 && list[i][k - 1] == '.') {
                            k--
                        }
                        list[i][j] = '.'
                        list[i][k] = 'O'
                    }
                }
            }
            for (i in n - 1 downTo 0) {
                for (j in 0 until m) {
                    if (list[i][j] == 'O') {
                        var k = i
                        while (k < n - 1 && list[k + 1][j] == '.') {
                            k++
                        }
                        list[i][j] = '.'
                        list[k][j] = 'O'
                    }
                }
            }
            for (i in 0 until n) {
                for (j in m - 1 downTo 0) {
                    if (list[i][j] == 'O') {
                        var k = j
                        while (k < m - 1 && list[i][k + 1] == '.') {
                            k++
                        }
                        list[i][j] = '.'
                        list[i][k] = 'O'
                    }
                }
            }
        }

        fun count(): Int {
            var res = 0
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (list[i][j] == 'O') {
                        res += n - i
                    }
                }
            }
            return res
        }

        var i = 0
        val map = mutableMapOf<String, Pair<Int, Int>>()
        while (true) {
            val key = list.joinToString { it.concatToString() }

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
    """.trimIndent())
    check(part1(testInput).also { println("part1 test: $it") } == 136)
    check(part2(testInput).also { println("part2 test: $it") } == 64)

    val input = readInput("Year2023/Day14")
    println(part1(input))
    println(part2(input))
}
