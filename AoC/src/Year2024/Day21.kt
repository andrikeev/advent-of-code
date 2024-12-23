package Year2024

import Day
import Point
import i
import j
import kotlin.math.abs

private object Day21 : Day {
    interface Key {
        val point: Point
    }

    private interface Pad {
        val keys: List<Key>
        val Enter: Key
        val Gap: Key
    }

    private object NPad : Pad {
        override val keys = NKey.entries
        override val Enter = NKey.Enter
        override val Gap = NKey.Gap
    }

    private object DPad : Pad {
        override val keys = DKey.entries
        override val Enter = DKey.Enter
        override val Gap = DKey.Gap
    }

    enum class NKey(override val point: Point) : Key {
        Seven(Point(0, 0)),
        Eight(Point(0, 1)),
        Nine(Point(0, 2)),
        Four(Point(1, 0)),
        Five(Point(1, 1)),
        Six(Point(1, 2)),
        One(Point(2, 0)),
        Two(Point(2, 1)),
        Three(Point(2, 2)),
        Gap(Point(3, 0)),
        Zero(Point(3, 1)),
        Enter(Point(3, 2)),
    }

    enum class DKey(override val point: Point) : Key {
        Gap(Point(0, 0)),
        Up(Point(0, 1)),
        Enter(Point(0, 2)),
        Left(Point(1, 0)),
        Down(Point(1, 1)),
        Right(Point(1, 2)),
    }

    data class Code(private val code: String) {
        val value = code.takeWhile(Char::isDigit).toIntOrNull() ?: 0
        val keys = code.map {
            when (it) {
                '1' -> NKey.One
                '2' -> NKey.Two
                '3' -> NKey.Three
                '4' -> NKey.Four
                '5' -> NKey.Five
                '6' -> NKey.Six
                '7' -> NKey.Seven
                '8' -> NKey.Eight
                '9' -> NKey.Nine
                '0' -> NKey.Zero
                'A' -> NKey.Enter
                else -> error("Unknown: $this")
            }
        }
    }

    private fun Pad.horizontalDirectionPanic(from: Key, to: Key): Boolean {
        val j1 = from.point.j
        val j2 = to.point.j
        return (minOf(j1, j2)..maxOf(j1, j2))
            .map { Point(from.point.i, it) }
            .any { it == Gap.point }
    }

    private fun Pad.verticalDirectionPanic(from: Key, to: Key): Boolean {
        val i1 = from.point.i
        val i2 = to.point.i
        return (minOf(i1, i2)..maxOf(i1, i2))
            .map { Point(it, from.point.j) }
            .any { it == Gap.point }
    }

    override fun part1(input: List<String>): Any {
        val codes = input.map(::Code)
        val pads = listOf(NPad, DPad, DPad)

        fun bestSequence(code: List<Key>, depth: Int = 0): List<Key> {
            return if (depth == pads.size) {
                code
            } else {
                val pad = pads[depth]
                val sequence = mutableListOf<Key>()
                var current = pad.Enter
                code.forEach { key ->
                    val di = key.point.i - current.point.i
                    val dj = key.point.j - current.point.j
                    val vertical = List(abs(di)) { if (di >= 0) DKey.Down else DKey.Up }
                    val horizontal = List(abs(dj)) { if (dj >= 0) DKey.Right else DKey.Left }
                    buildList {
                        if (!pad.horizontalDirectionPanic(current, key)) {
                            add(horizontal + vertical + DKey.Enter)
                        }
                        if (!pad.verticalDirectionPanic(current, key)) {
                            add(vertical + horizontal + DKey.Enter)
                        }
                    }
                        .map { bestSequence(it, depth + 1) }
                        .minBy(List<Key>::size)
                        .let(sequence::addAll)
                    current = key
                }
                sequence
            }
        }

        return codes.sumOf { code ->
            bestSequence(code.keys).size * code.value
        }
    }

    override fun part2(input: List<String>): Any {
        val codes = input.map(::Code)
        val pads = listOf(NPad) + List(25) { DPad }
        val cache = mutableMapOf<Pair<List<Key>, Int>, Long>()

        fun bestLength(code: List<Key>, depth: Int = 0): Long {
            return when {
                depth == pads.size -> code.size.toLong()
                cache.containsKey(code to depth) -> cache.getValue(code to depth)
                else -> {
                    val pad = pads[depth]
                    var length = 0L
                    var current = pad.Enter
                    code.forEach { key ->
                        val di = key.point.i - current.point.i
                        val dj = key.point.j - current.point.j
                        val vertical = List(abs(di)) { if (di >= 0) DKey.Down else DKey.Up }
                        val horizontal = List(abs(dj)) { if (dj >= 0) DKey.Right else DKey.Left }
                        length += buildList {
                            if (!pad.horizontalDirectionPanic(current, key)) {
                                add(horizontal + vertical + DKey.Enter)
                            }
                            if (!pad.verticalDirectionPanic(current, key)) {
                                add(vertical + horizontal + DKey.Enter)
                            }
                        }.minOf { bestLength(it, depth + 1) }
                        current = key
                    }
                    cache[code to depth] = length
                    length
                }
            }
        }

        return codes.sumOf { code ->
            bestLength(code.keys) * code.value
        }
    }
}

fun main() = with(Day21) {
    test1(
        input = """
            029A
            980A
            179A
            456A
            379A
        """.trimIndent(),
        expected = 126384,
    )
    result1()

    result2()
}
