package Year2022

import Point
import i
import j
import moveBy
import readInput
import testInput

fun main() {
    val mainDirections = listOf(Compass.N, Compass.S, Compass.W, Compass.E)

    fun parseMap(input: List<String>) = input
        .flatMapIndexed { i, s ->
            s.mapIndexedNotNull { j, c ->
                c.takeIf { it == '#' }?.let { Elf(i, j) }
            }
        }.toSet()

    fun considerMove(
        elves: Set<Elf>,
        startDirection: Compass,
    ): Map<Point, List<Elf>> {
        val directions = Compass.entries.toTypedArray()

        fun Point.to(direction: Compass) = when (direction) {
            Compass.N -> moveBy(i = -1)
            Compass.NE -> moveBy(i = -1, j = 1)
            Compass.NW -> moveBy(i = -1, j = -1)
            Compass.S -> moveBy(i = 1)
            Compass.SE -> moveBy(i = 1, j = 1)
            Compass.SW -> moveBy(i = 1, j = -1)
            Compass.E -> moveBy(j = +1)
            Compass.W -> moveBy(j = -1)
        }

        fun Compass.nextMoveDirection() = when (this) {
            Compass.N -> Compass.S
            Compass.S -> Compass.W
            Compass.E -> Compass.N
            Compass.W -> Compass.E
            else -> error("Not a main direction $this")
        }

        fun Compass.adjacent() = when (this) {
            Compass.N -> listOf(Compass.N, Compass.NE, Compass.NW)
            Compass.S -> listOf(Compass.S, Compass.SE, Compass.SW)
            Compass.E -> listOf(Compass.E, Compass.NE, Compass.SE)
            Compass.W -> listOf(Compass.W, Compass.NW, Compass.SW)
            else -> error("Not a main direction $this")
        }

        fun Compass.moveDirections() = buildList {
            var direction = this@moveDirections
            repeat(mainDirections.size) {
                add(direction)
                direction = direction.nextMoveDirection()
            }
        }

        return elves
            .mapNotNull { elf ->
                directions
                    .map(elf::to)
                    .takeIf { it.any(elves::contains) }
                    ?.let { startDirection.moveDirections() }
                    ?.firstOrNull { direction ->
                        direction.adjacent()
                            .map(elf::to)
                            .none(elves::contains)
                    }
                    ?.let { direction -> elf.to(direction) to elf }
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

private enum class Compass {
    N, E, S, W, NE, SE, SW, NW;
}
