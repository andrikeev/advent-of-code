package Year2022

import readInput

fun main() {
    fun simulation(input: String, steps: Long): Long {
        return Chamber(steps, GasJets(input)).simulate()
    }

    fun part1(input: String): Long = simulation(input, 2022L)

    fun part2(input: String): Long = simulation(input, 1_000_000_000_000)

    val testInput = readInput("Year2022/Day17_test")
    check(part1(testInput.first()).also { println("part1 test: $it") } == 3068L)
//    check(part2(testInput.first()).also { println("part2 test: $it") } == 1514285714288)

    val input = readInput("Year2022/Day17")
    println(part1(input.first()))
    println(part2(input.first()))
}

private class Chamber(
    private val steps: Long,
    private val gasJets: GasJets,
) {
    private val blockGenerator = BlockGenerator()
    private var blocksArray = mutableListOf<Array<Boolean>>()
    private var currentBlock = blockGenerator.get(3)
    private var blocks = 0L
    private var step = 0L
    private var height = 0L

    fun simulate(): Long {
        while (blocks < steps) {
            gasPush(gasJets.getNext())
            detektCycle()
        }
        return height
    }

    private fun gasPush(direction: GasDirection) {
        if (currentBlock.canGoTo(direction)) {
            currentBlock.push(direction)
        }
        if (currentBlock.canFall()) {
            currentBlock.fall()
        } else {
            addBlock()
        }
    }

    private fun addBlock() {
        blocks++
        currentBlock.points().sortedBy(Pair<Long, Long>::second).forEach { (x, y) -> take(x, y) }
        currentBlock = blockGenerator.get(blocksArray.size + 3L)
    }

    private fun take(x: Long, y: Long) {
        if (y > blocksArray.lastIndex) {
            blocksArray.add(Array(7) { j -> j == x.toInt() })
            height++
        } else {
            blocksArray[y.toInt()][x.toInt()] = true
        }
    }

    private fun taken(x: Long, y: Long): Boolean {
        return y < blocksArray.size && blocksArray[(y).toInt()][x.toInt()]
    }

    private fun Block.intersects(): Boolean = points().any { (x, y) -> x < 0 || x > 6 || y < 0 || taken(x, y) }

    private fun Block.canFall(): Boolean = !copyBlock(y = y - 1).intersects()

    private fun Block.canGoTo(direction: GasDirection): Boolean = when (direction) {
        GasDirection.Left -> !copyBlock(x = x - 1).intersects()
        GasDirection.Right -> !copyBlock(x = x + 1).intersects()
    }

    private var preCycleHeight = 0L
    private var preCycleBlocks = 0L
    private fun detektCycle() {
        step++
        if (step % 10091L == 0L) {
            if (step / 10091L == 1L) {
                preCycleHeight = height
                preCycleBlocks = blocks
            }
            if (step / 10091L == 2L) {
                val cycleHeight = height - preCycleHeight
                val cycleBlocks = blocks - preCycleBlocks
                val cyclesCount = (steps - preCycleBlocks) / cycleBlocks
                height += cycleHeight * (cyclesCount - 1)
                blocks += cycleBlocks * (cyclesCount - 1)
            }
        }
    }
}


private class BlockGenerator {
    private var index = 0

    fun get(y: Long): Block {
        if (index > 4) {
            index %= 5
        }
        return when (index++) {
            0 -> Block.Minus(2, y)
            1 -> Block.Plus(2, y)
            2 -> Block.Angle(2, y)
            3 -> Block.Stick(2, y)
            4 -> Block.Box(2, y)
            else -> error("wrong index")
        }
    }
}

private sealed interface Block {
    var x: Long
    var y: Long

    fun push(direction: GasDirection) {
        when (direction) {
            GasDirection.Left -> x--
            GasDirection.Right -> x++
        }
    }

    fun fall() {
        y--
    }

    fun copyBlock(x: Long = this.x, y: Long = this.y): Block {
        return when (this) {
            is Angle -> copy(x = x, y = y)
            is Box -> copy(x = x, y = y)
            is Minus -> copy(x = x, y = y)
            is Plus -> copy(x = x, y = y)
            is Stick -> copy(x = x, y = y)
        }
    }

    fun points(): Set<Pair<Long, Long>>

    data class Minus(
        override var x: Long,
        override var y: Long,
    ) : Block {
        override fun points(): Set<Pair<Long, Long>> = buildSet {
            repeat(4) { i -> add(Pair(x + i, y)) }
        }
    }

    data class Plus(
        override var x: Long,
        override var y: Long,
    ) : Block {
        override fun points(): Set<Pair<Long, Long>> = buildSet {
            add(Pair(x, y + 1))
            add(Pair(x + 1, y))
            add(Pair(x + 1, y + 1))
            add(Pair(x + 1, y + 2))
            add(Pair(x + 2, y + 1))
        }
    }

    data class Angle(
        override var x: Long,
        override var y: Long,
    ) : Block {
        override fun points(): Set<Pair<Long, Long>> = buildSet {
            add(Pair(x, y))
            add(Pair(x + 1, y))
            add(Pair(x + 2, y))
            add(Pair(x + 2, y + 1))
            add(Pair(x + 2, y + 2))
        }
    }

    data class Stick(
        override var x: Long,
        override var y: Long,
    ) : Block {
        override fun points(): Set<Pair<Long, Long>> = buildSet {
            repeat(4) { i -> add(Pair(x, y + i)) }
        }
    }

    data class Box(
        override var x: Long,
        override var y: Long,
    ) : Block {
        override fun points(): Set<Pair<Long, Long>> = buildSet {
            repeat(2) { i -> repeat(2) { j -> add(Pair(x + i, y + j)) } }
        }
    }
}

private enum class GasDirection {
    Left, Right
}

private class GasJets(input: String) {
    private var index = 0
    private val gas = input.toDirections()
    private fun String.toDirections(): List<GasDirection> = map {
        when (it) {
            '<' -> GasDirection.Left
            '>' -> GasDirection.Right
            else -> error("wrong sign")
        }
    }

    fun getNext(): GasDirection {
        if (index > gas.lastIndex) {
            index %= gas.size
        }
        return gas[index++]
    }
}
