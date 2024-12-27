package Year2021

import Day
import charGrid

private object Day25 : Day {
    override fun part1(input: List<String>): Any {
        val (map, n, m) = input.charGrid()
        var step = 1
        while (true) {
            var moved: Boolean
            buildList {
                for (i in 0..<n) {
                    for (j in 0..<m) {
                        if (map[i][j] == '>' && map[i][(j + 1) % m] == '.') {
                            add(i to j)
                        }
                    }
                }
            }
                .also { moved = it.isNotEmpty() }
                .forEach { (i, j) ->
                    map[i][j] = '.'
                    map[i][(j + 1) % m] = '>'
                }
            buildList {
                for (j in 0..<m) {
                    for (i in 0..<n) {
                        if (map[i][j] == 'v' && map[(i + 1) % n][j] == '.') {
                            add(i to j)
                        }
                    }
                }
            }
                .also { moved = moved || it.isNotEmpty() }
                .forEach { (i, j) ->
                    map[i][j] = '.'
                    map[(i + 1) % n][j] = 'v'
                }
            if (moved) {
                step++
            } else {
                return step
            }
        }
    }
}

fun main() = with(Day25) {
    test1(
        input = """
            v...>>.vv>
            .vv>>.vv..
            >>.>v>...v
            >>v>>.>.v.
            v>v.vv.v..
            >.>>..v...
            .vv..>.>v.
            v.v..>>v.v
            ....v..v.>
        """.trimIndent(),
        expected = 58,
    )
    result1()
}
