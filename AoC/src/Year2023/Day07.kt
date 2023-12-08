package Year2023

import readInput
import testInput


fun main() {

    val str = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    fun List<Pair<Int, Int>>.comb() = when {
        size == 1 -> Comb.FvOK
        get(0).second == 4 -> Comb.FrOK
        get(0).second == 3 && get(1).second == 2 -> Comb.FH
        get(0).second == 3 -> Comb.TrP
        get(0).second == 2 && get(1).second == 2 -> Comb.TwP
        get(0).second == 2 -> Comb.OP
        else -> Comb.HC
    }

    fun comb1(input: String) = input.map(str::indexOf)
        .groupBy { it }
        .map { it.key to it.value.size }
        .sortedByDescending { it.second }
        .comb()

    fun comb2(input: String): Comb {
        val indexOfJ = str.indexOf('J')
        return (0 until indexOfJ - 1)
            .map { replace ->
                input.asSequence()
                    .map(str::indexOf)
                    .map { if (it == indexOfJ) replace else it }
                    .groupBy { it }
                    .map { it.key to it.value.size }
                    .sortedByDescending { it.second }
                    .comb()
            }
            .minBy(Comb::ordinal)
    }

    fun List<Hand>.rank() = sortedWith { f, s ->
        var c = f.comb.compareTo(s.comb)
        if (c == 0) {
            for (i in f.hand.indices) {
                c = f.hand[i].compareTo(s.hand[i])
                if (c != 0) {
                    break
                }
            }
        }
        c
    }
        .reversed()
        .mapIndexed { i, (_, _, bid) -> (i + 1) * bid }
        .sum()

    fun part1(input: List<String>): Int {
        return input
            .map {
                val (hand, bid) = it.split(" ")
                Hand(hand.map(str::indexOf), comb1(hand), bid.toInt())
            }
            .rank()
    }

    fun part2(input: List<String>): Int {
        return input
            .map {
                val (hand, bid) = it.split(" ")
                Hand(hand.map(str::indexOf), comb2(hand), bid.toInt())
            }
            .rank()
    }

    val testInput = testInput(
        """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()
    )
    check(part1(testInput).also { println("part1 test: $it") } == 6440)
    check(part2(testInput).also { println("part2 test: $it") } == 5905)

    val input = readInput("Year2023/Day07")
    println(part1(input))
    println(part2(input))
}

enum class Comb { FvOK, FrOK, FH, TrP, TwP, OP, HC }

data class Hand(val hand: List<Int>, val comb: Comb, val bid: Int)
