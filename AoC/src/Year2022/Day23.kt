package Year2022

import Direction8
import Point
import i
import j
import readInput
import testInput
import moveTo

fun main() {
    val mainDirections = listOf(Direction8.N, Direction8.S, Direction8.W, Direction8.E)

    fun parseMap(input: List<String>) = input
        .flatMapIndexed { i, s ->
            s.mapIndexedNotNull { j, c ->
                c.takeIf { it == '#' }?.let { Elf(i, j) }
            }
        }.toSet()

    fun considerMove(
        elves: Set<Elf>,
        startDirection: Direction8,
    ): Map<Point, List<Elf>> {
        val directions = Direction8.entries.toTypedArray()

        fun Direction8.nextMoveDirection() = when (this) {
            Direction8.N -> Direction8.S
            Direction8.S -> Direction8.W
            Direction8.E -> Direction8.N
            Direction8.W -> Direction8.E
            else -> error("Not a main direction $this")
        }

        fun Direction8.adjacent() = when (this) {
            Direction8.N -> listOf(Direction8.N, Direction8.NE, Direction8.NW)
            Direction8.S -> listOf(Direction8.S, Direction8.SE, Direction8.SW)
            Direction8.E -> listOf(Direction8.E, Direction8.NE, Direction8.SE)
            Direction8.W -> listOf(Direction8.W, Direction8.NW, Direction8.SW)
            else -> error("Not a main direction $this")
        }

        fun Direction8.moveDirections() = buildList {
            var direction = this@moveDirections
            repeat(mainDirections.size) {
                add(direction)
                direction = direction.nextMoveDirection()
            }
        }

        return elves
            .mapNotNull { elf ->
                directions
                    .map(elf::moveTo)
                    .takeIf { it.any(elves::contains) }
                    ?.let { startDirection.moveDirections() }
                    ?.firstOrNull { direction ->
                        direction.adjacent()
                            .map(elf::moveTo)
                            .none(elves::contains)
                    }
                    ?.let { direction -> (elf moveTo direction) to elf }
            }
            .groupBy(
                Pair<Point, Elf>::first,
                Pair<Point, Elf>::second,
            )
    }

    fun MutableSet<Elf>.move(consideration: Map<Point, List<Elf>>) {
        consideration
            .filterValues { it.size == 1 }
            .mapValues { it.value.first() }
            .forEach { (to, from) ->
                remove(from)
                add(to)
            }
    }

    fun part1(input: List<String>): Int {
        val elves = mutableSetOf<Elf>().apply { addAll(parseMap(input)) }

        repeat(10) { i ->
            val direction = mainDirections[i % mainDirections.size]
            val consideration = considerMove(elves, direction)
            elves.move(consideration)
        }

        return (elves.maxOf(Elf::i) + 1 - elves.minOf(Elf::i)) *
                (elves.maxOf(Elf::j) + 1 - elves.minOf(Elf::j)) - elves.size
    }

    fun part2(input: List<String>): Int {
        val elves = mutableSetOf<Elf>().apply { addAll(parseMap(input)) }
        var step = 0

        while (true) {
            val direction = mainDirections[step % mainDirections.size]
            val consideration = considerMove(elves, direction)
            if (consideration.isNotEmpty()) {
                elves.move(consideration)
                step++
            } else {
                break
            }
        }

        return step + 1
    }

    val testInput = testInput(
        """
        ....#..
        ..###.#
        #...#.#
        .#...##
        #.###..
        ##.#.##
        .#..#..
        """
    )
    check(part1(testInput).also { println("part1 test: $it") } == 110)
    check(part2(testInput).also { println("part2 test: $it") } == 20)

    val input = readInput("Year2022/Day23")
    println(part1(input))
    println(part2(input))
}

private typealias Elf = Point
