package Year2024

import CharGrid
import Day
import Direction
import Point
import charGrid
import firstPosition
import forEach
import get
import gridSize
import moveTo
import set
import java.util.Stack

private object Day15 : Day {
    private fun List<String>.map(): CharGrid {
        return takeWhile(String::isNotEmpty).charGrid().first
    }

    private fun List<String>.moves(): List<Direction> {
        return dropWhile(String::isNotEmpty)
            .drop(1)
            .joinToString("")
            .map { c ->
                when (c) {
                    '<' -> Direction.Left
                    '^' -> Direction.Up
                    '>' -> Direction.Right
                    'v' -> Direction.Down
                    else -> error("Unknown char: $c")
                }
            }
    }

    override fun part1(input: List<String>): Any {
        val map = input.map()
        val moves = input.moves()
        var r = map.firstPosition { c -> c == '@' }

        infix fun Point.tryMoveTo(d: Direction): Point {
            val stack = Stack<Point>()
            var p = this
            while (true) {
                val next = p moveTo d
                when (map[next]) {
                    '.' -> {
                        stack.push(p)
                        break
                    }
                    'O' -> {
                        stack.push(p)
                        p = next
                        continue
                    }
                    else -> {
                        stack.clear()
                        break
                    }
                }
            }
            if (stack.isNotEmpty()) {
                while (stack.isNotEmpty()) {
                    p = stack.pop()
                    map[p moveTo d] = map[p]
                    map[p] = '.'
                }
                return this moveTo d
            }
            return this
        }

        for (m in moves) {
            r = r tryMoveTo m
        }

        var result = 0
        map.forEach { c, i, j ->
            if (c == 'O') {
                result += i * 100 + j
            }
        }
        return result
    }

    override fun part2(input: List<String>): Any {
        val initialMap = input.map()
        val moves = input.moves()
        val (h, w) = initialMap.gridSize()
        val map = CharGrid(h, w * 2) { i, j ->
            when (initialMap[i][j / 2]) {
                '.' -> '.'
                '#' -> '#'
                'O' -> if (j % 2 == 0) '[' else ']'
                '@' -> if (j % 2 == 0) '@' else '.'
                else -> error("Unknown char")

            }
        }
        var r = map.firstPosition { c -> c == '@' }

        infix fun Point.tryMoveTo(d: Direction): Point {
            val stack = Stack<List<Point>>()
            var p = listOf(this)
            while (true) {
                val next = p.map { it moveTo d }
                when {
                    next.all { map[it] == '.' } -> {
                        stack.push(p)
                        break
                    }
                    next.all { map[it] != '#' } -> {
                        stack.push(p)
                        p = when (d) {
                            Direction.Left, Direction.Right -> next
                            Direction.Up, Direction.Down -> {
                                buildList {
                                    if (map[next.first()] == ']') {
                                        add(next.first() moveTo Direction.Left)
                                    }
                                    addAll(next)
                                    if (map[next.last()] == '[') {
                                        add(next.last() moveTo Direction.Right)
                                    }
                                }
                            }
                        }.filter { map[it] != '.' }
                        continue
                    }
                    else -> {
                        stack.clear()
                        break
                    }
                }
            }
            if (stack.isNotEmpty()) {
                while (stack.isNotEmpty()) {
                    p = stack.pop()
                    p.forEach {
                        map[it moveTo d] = map[it]
                        map[it] = '.'
                    }
                }
                return this moveTo d
            }
            return this
        }

        for (m in moves) {
            r = r tryMoveTo m
            println(m)
        }

        var result = 0
        map.forEach { c, i, j ->
            if (c == '[') {
                result += i * 100 + j
            }
        }
        return result
    }
}

fun main() = with(Day15) {
    test1(
        input = """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            <^^>>>vv<v>>v<<
        """.trimIndent(),
        expected = 2028,
    )
    test1(
        input = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent(),
        expected = 10092
    )
    result1()

    test2(
        input = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent(),
        expected = 9021,
    )
    result2()
}
