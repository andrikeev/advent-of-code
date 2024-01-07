package Year2021

import expect
import readInput
import testInput

fun main() {

    data class Position(
        val p1: Int,
        val p2: Int,
    )

    data class Scores(
        val s1: Int,
        val s2: Int,
    )

    data class State(
        val position: Position,
        val scores: Scores = Scores(0, 0),
    )

    data class Wins(
        val w1: Long,
        val w2: Long,
    )

    fun part1(input: List<String>): Any {
        val players = input
            .map { it.substringAfter(": ").toInt() }
            .toIntArray()
        val scores = IntArray(players.size) { 0 }

        var rolls = 0
        var player = 0
        while (scores.all { it < 1000 }) {
            val points = (rolls..rolls + 2).sumOf { it.mod(100).plus(1) }
            players[player] = (players[player] + points).minus(1).mod(10).plus(1)
            scores[player] += players[player]
            player = player.plus(1).mod(players.size)
            rolls += 3
        }

        return scores.min() * rolls
    }

    fun part2(input: List<String>): Any {
        val (pos1, pos2) = input.map { it.substringAfter(": ").toInt() }
        val state = State(Position(pos1, pos2))
        val cache = mutableMapOf<State, Wins>()

        fun countWins(state: State): Wins {
            if (state in cache) {
                return cache.getValue(state)
            }

            val (position, scores) = state
            val (p1, p2) = position
            val (s1, s2) = scores
            return (1..3).flatMap { i ->
                (1..3).flatMap { j ->
                    (1..3).map { k -> i + j + k }
                }
            }
                .map { p ->
                    val nextP1 = (p1 + p).minus(1).mod(10).plus(1)
                    val nextS1 = s1 + nextP1
                    if (nextS1 >= 21) {
                        Wins(1, 0)
                    } else {
                        val (w2, w1) = countWins(State(Position(p2, nextP1), Scores(s2, nextS1)))
                        Wins(w1, w2)
                    }
                }
                .reduce { acc, wins -> Wins(acc.w1 + wins.w1, acc.w2 + wins.w2) }
                .also { cache[state] = it }
        }

        return countWins(state).let { (w1, w2) -> maxOf(w1, w2) }
    }

    val testInput = testInput("""
        Player 1 starting position: 4
        Player 2 starting position: 8
    """)
    val input = readInput("Year2021/Day21")

    // part 1
    expect(part1(testInput), 739785)
    println(part1(input))

    // part 2
    expect(part2(testInput), 444356092776315)
    println(part2(input))
}
