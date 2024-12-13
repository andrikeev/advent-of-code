package Year2024

import Day

private typealias Offset = Pair<Long, Long>

private val Offset.x
    get() = first
private val Offset.y
    get() = second

private object Day13 : Day {
    data class Game(val a: Offset, val b: Offset, val p: Offset)

    override fun part1(input: List<String>): Any {
        val games = input.chunked(4).dropLast(1).map {
            val (a, b, p) = it
            Game(
                a.substringAfter(':').split(',').let { (x, y) ->
                    x.substringAfter('+').toLong() to y.substringAfter('+').toLong()
                },
                b.substringAfter(':').split(',').let { (x, y) ->
                    x.substringAfter('+').toLong() to y.substringAfter('+').toLong()
                },
                p.substringAfter(':').split(',').let { (x, y) ->
                    x.substringAfter('=').toLong() to y.substringAfter('=').toLong()
                },
            )
        }

        fun Game.tokens(): Int {
            for (i in 0..<100) {
                for (j in 0..<100) {
                    val c = Offset(a.x * i + b.x * j, a.y * i + b.y * j)
                    if (c == p) {
                        return i * 3 + j
                    }
                }
            }
            return 0
        }

        return games.map(Game::tokens).sum()
    }

    override fun part2(input: List<String>): Any {
        val games = input.chunked(4).dropLast(1).map {
            val (a, b, p) = it
            Game(
                a.substringAfter(':').split(',').let { (x, y) ->
                    x.substringAfter('+').toLong() to y.substringAfter('+').toLong()
                },
                b.substringAfter(':').split(',').let { (x, y) ->
                    x.substringAfter('+').toLong() to y.substringAfter('+').toLong()
                },
                p.substringAfter(':').split(',').let { (x, y) ->
                    x.substringAfter('=').toLong() + 10000000000000L to
                        y.substringAfter('=').toLong() + 10000000000000L
                },
            )
        }

        fun Game.tokens(): Long {
            val ax = a.x.toDouble()
            val ay = a.y.toDouble()
            val bx = b.x.toDouble()
            val by = b.y.toDouble()
            val px = (p.x).toDouble()
            val py = (p.y).toDouble()

            val i = ((py * bx - by * px) / (ay * bx - by * ax)).toLong()
            val j = ((px - i * ax) / bx).toLong()

            return if (a.x * i + b.x * j == p.x && a.y * i + b.y * j == p.y) {
                i * 3 + j
            } else {
                0
            }
        }

        return games.map(Game::tokens).sum()
    }
}

fun main() = with(Day13) {
    test1(
        input = """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
        """.trimIndent(),
        expected = 480,
    )
    result1()

    result2()
}
