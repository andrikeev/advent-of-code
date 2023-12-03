package Year2021.Day05

data class Point(val x: Int, val y: Int)

internal interface Field {
    fun addLine(from: Point, to: Point)
    fun numberOfOverpasses(): Int
}

internal abstract class BaseField : Field {
    private var grid: MutableList<MutableList<Int>> = mutableListOf()

    override fun addLine(from: Point, to: Point) {
        val (x1, y1) = from
        val (x2, y2) = to
        addLine(
            xRange = if (x1 <= x2) x1..x2 else x1 downTo x2,
            yRange = if (y1 <= y2) y1..y2 else y1 downTo y2,
        )
    }

    override fun numberOfOverpasses(): Int {
        grid.forEach { println(it) }
        return grid.sumOf { row -> row.count { it > 1 } }
    }

    protected abstract fun addLine(xRange: IntProgression, yRange: IntProgression)

    protected fun adjust(size: Int) {
        grid.forEach { row -> repeat(size - row.lastIndex) { row.add(0) } }
        repeat(size - grid.lastIndex) { grid.add(MutableList(size + 1) { 0 }) }
    }

    protected fun inc(x: Int, y: Int) {
        grid[y][x] += 1
    }
}

internal fun calcOverpasses(field: Field, input: List<String>): Int {
    return parseField(field, input).numberOfOverpasses()
}

private fun parseField(field: Field, input: List<String>): Field {
    input.forEach { line ->
        val (from, to) = line.split(" -> ")
        field.addLine(
            from = from.split(",").map(String::toInt).let { (x1, y1) -> Point(x1, y1) },
            to = to.split(",").map(String::toInt).let { (x1, y1) -> Point(x1, y1) },
        )
    }
    return field
}
