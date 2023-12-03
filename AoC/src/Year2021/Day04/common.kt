package Year2021.Day04

const val boardSize = 5

fun parseInput(lines: List<String>): Pair<List<Int>, List<Board>> {
    return parseNumbers(lines.first()) to
            lines.drop(1)
                .chunked(boardSize + 1)
                .map { parseBoard(it.drop(1)) }
}

private fun parseNumbers(line: String): List<Int> {
    return line.split(",").map(String::toInt)
}

private fun parseBoard(lines: List<String>): Board {
    val cells = lines.map { line ->
        line.trim()
            .replace("  ", " ")
            .split(" ")
            .map(String::toInt)
    }
    return GameBoard(cells)
}

class GameBoard(private val cells: List<List<Int>>) : Board {
    private val marked = MutableList(boardSize) { MutableList(boardSize) { false } }

    override fun mark(value: Int) {
        cells.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                if (value == cell) {
                    marked[i][j] = true
                }
            }
        }
    }

    override fun isBingo(): Boolean {
        return (0 until boardSize).any { index ->
            marked.getRow(index).all { it } or marked.getColumn(index).all { it }
        }
    }

    override fun calcBingoSum(): Int {
        var sum = 0
        cells.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                if (!marked[i][j]) {
                    sum += cell
                }
            }
        }
        return sum
    }

    private inline fun <reified T> List<List<T>>.getRow(index: Int): List<T> {
        return this[index]
    }

    private inline fun <reified T> List<List<T>>.getColumn(index: Int): List<T> {
        return this.map { it[index] }
    }
}

interface Board {
    fun mark(value: Int)
    fun isBingo(): Boolean
    fun calcBingoSum(): Int
}
