package Year2023

import expect
import lcm
import readInput
import testInput

fun main() {

    fun readMap(input: List<String>) = input.map {
        val (start, end) = it.split(" = ")
        val (left, right) = end.removeSurrounding("(", ")").split(", ")
        start to (left to right)
    }.associateBy({ it.first }, { it.second })

    fun part1(input: List<String>): Any {
        val rl = input.first()
        val map = readMap(input.drop(2))

        var i = 0
        var s = 0
        var current = "AAA"

        while (current != "ZZZ") {
            val t = rl[i]
            val next = map.getValue(current)
            current = if (t == 'L') next.first else next.second
            i = (i + 1) % rl.length
            s++
        }

        return s
    }

    fun part2(input: List<String>): Any {
        val rl = input.first()
        val map = readMap(input.drop(2))
        var i = 0
        var s = 0

        var current = map.filter { it.key.endsWith('A') }.keys.toList()
        val loops = mutableMapOf<Int, Int>()

        while (loops.size < current.size) {
            val t = rl[i]

            current = current.mapIndexed { j, st ->
                val next = map.getValue(st)
                if (t == 'L') next.first else next.second.also {
                    if (it.endsWith('Z') && !loops.contains(j)) {
                        loops[j] = s + 1
                    }
                }
            }
            i = (i + 1) % rl.length
            s++
        }

        return loops.values.fold(1L) { acc, l -> lcm(acc, l.toLong()) }
    }

    val testInput = testInput("""
        RL

        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    """)
    val input = readInput("Year2023/Day08")

    // part 1
    expect(part1(testInput), 2)
    println(part1(input))

    // part 2
    val testInput2 = testInput("""
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """)
    expect(part2(testInput2), 6L)
    println(part2(input))
}
