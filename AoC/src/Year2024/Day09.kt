package Year2024

import Day

private object Day09 : Day {
    override fun part1(input: List<String>): Any {
        val memory = input.first().map { it.digitToInt() }
        val result = mutableListOf<Int>()
        repeat(memory.first()) {
            result.add(0)
        }

        var i = 1
        var j = memory.lastIndex - memory.lastIndex % 2
        var spaces = memory[i]
        var blocksToMove = memory[j]
        while (true) {
            while (spaces == 0) {
                i++
                if (i == j) {
                    repeat(blocksToMove) {
                        result.add(i / 2)
                    }
                    break
                } else {
                    repeat(memory[i]) {
                        result.add(i / 2)
                    }
                }
                i++
                spaces = memory[i]
            }
            if (i >= j) {
                break
            }
            while (spaces > 0 && blocksToMove > 0) {
                result.add(j / 2)
                blocksToMove--
                spaces--
            }
            if (blocksToMove == 0) {
                j -= 2
                if (j <= i) {
                    break
                }
                blocksToMove = memory[j]
            }
        }

        return result.mapIndexed { index, value -> index * value.toLong() }.sum()
    }

    override fun part2(input: List<String>): Any {
        val memory = input.first().map { it.digitToInt() }
        var result = buildList {
            memory.forEachIndexed { index, value ->
                if (index % 2 == 0) {
                    add(Block(true, value, index / 2))
                } else {
                    add(Block(false, value, 0))
                }
            }
        }

        while (true) {
            var i = result.lastIndex
            while (i > 0) {
                val block = result[i]
                if (block.isFile) {
                    val j = result.indexOfFirst { it.isSpace && it.size >= block.size }
                    if (j in 0..<i) {
                        val space = result[j]
                        result = result.subList(0, j)
                            .asSequence()
                            .plus(Triple(true, block.size, block.id))
                            .plus(Triple(false, space.size - block.size, 0))
                            .plus(result.subList(j + 1, i))
                            .plus(Triple(false, block.size, 0))
                            .plus(result.subList(i + 1, result.size))
                            .toList()
                        break
                    }
                }
                i--
            }
            if (i == 0) {
                break
            }
        }

        var i = 0
        var sum = 0L
        result.forEach { block ->
            if (block.isFile) {
                repeat(block.size) {
                    sum += block.id * i++
                }
            } else {
                i += block.size
            }
        }
        return sum
    }
}

private typealias Block = Triple<Boolean, Int, Int>

private val Block.isFile: Boolean get() = first
private val Block.isSpace: Boolean get() = !first
private val Block.size: Int get() = second
private val Block.id: Int get() = third

fun main() = with(Day09) {
    test1(
        input = """
        1010101
        """.trimIndent(),
        expected = 14L,
    )
    test1(
        input = """
        2333133121414131402
        """.trimIndent(),
        expected = 1928L,
    )
    result1()

    test2(
        input = """
        2333133121414131402
        """.trimIndent(),
        expected = 2858L,
    )
    result2()
}
