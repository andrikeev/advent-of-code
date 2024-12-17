package Year2024

import Day
import Direction
import Point
import Position
import charGrid
import firstPosition
import get
import moveTo
import point
import turnLeft
import turnRight
import java.util.PriorityQueue

private object Day16 : Day {
    private fun Position.next(): List<Pair<Position, Int>> {
        val (point, direction) = this
        return listOf(
            (point to direction.turnLeft()) to 1000,
            ((point moveTo direction) to direction) to 1,
            (point to direction.turnRight()) to 1000,
        )
    }

    override fun part1(input: List<String>): Any {
        val (map) = input.charGrid()
        val start = map.firstPosition { it == 'S' }
        val end = map.firstPosition { it == 'E' }
        val toVisit = PriorityQueue(compareBy(Pair<Position, Int>::second))
            .apply { add((start to Direction.Right) to 0) }
        val visited = mutableSetOf<Position>()
        val scores = mutableMapOf<Position, Int>()

        while (toVisit.isNotEmpty()) {
            val (position, score) = toVisit.poll()
            if (position.first == end) {
                return score
            } else if (visited.contains(position)) {
                continue
            } else {
                visited.add(position)
                position.next()
                    .filter { (p) -> p !in visited }
                    .filter { (p) -> map[p.point] != '#' }
                    .forEach { (next, cost) ->
                        val nextScore = score + cost
                        if (nextScore < scores.getOrDefault(next, Int.MAX_VALUE)) {
                            scores[next] = nextScore
                            toVisit.add(next to nextScore)
                        }
                    }
            }
        }
        error("No path found")
    }

    override fun part2(input: List<String>): Any {
        val (map) = input.charGrid()
        val start = map.firstPosition { it == 'S' }
        val end = map.firstPosition { it == 'E' }
        val toVisit = PriorityQueue(compareBy(Pair<List<Position>, Int>::second))
            .apply { add(listOf(start to Direction.Right) to 0) }
        val visited = mutableSetOf<Position>()
        val scores = mutableMapOf<Position, Int>()
        val result = mutableSetOf<Point>()
        var min = Int.MAX_VALUE

        while (toVisit.isNotEmpty()) {
            val (path, score) = toVisit.poll()
            val position = path.last()
            if (position.point == end) {
                if (score <= min) {
                    min = score
                    result.addAll(path.map(Position::point))
                }
            } else {
                position.next()
                    .filter { (p) -> p !in visited }
                    .filter { (p) -> map[p.point] != '#' }
                    .forEach { (next, cost) ->
                        val nextScore = score + cost
                        if (nextScore <= scores.getOrDefault(next, Int.MAX_VALUE)) {
                            scores[next] = nextScore
                            toVisit.add(path + next to nextScore)
                        }
                    }
            }
        }

        return result.size
    }
}

fun main() = with(Day16) {
    test1(
        input = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
        """.trimIndent(),
        expected = 7036,
    )
    result1()

    test2(
        input = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
        """.trimIndent(),
        expected = 45,
    )
    result2()
}
