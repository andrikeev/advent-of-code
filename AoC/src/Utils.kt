import java.io.File

typealias Point = Pair<Int, Int>

fun readInput(name: String) =
    File("res", "$name.txt").readLines()

fun testInput(input: String) =
    input.trimIndent().lines()

val Point.i
    get() = first

val Point.j
    get() = second

fun Point.moveBy(i: Int = 0, j: Int = 0): Point {
    return copy(first + i, second + j)
}

fun Point.adjacent(): List<Point> {
    return listOf(
        moveBy(j = -1),
        moveBy(i = -1, j = -1),
        moveBy(i = -1),
        moveBy(i = -1, j = +1),
        moveBy(i = +1, j = -1),
        moveBy(i = +1),
        moveBy(i = +1, j = +1),
        moveBy(j = +1),
    )
}

fun Point.adj(): List<Point> {
    return listOf(
        moveBy(j = -1),
        moveBy(i = -1),
        moveBy(i = +1),
        moveBy(j = +1),
    )
}

infix fun Point.leftTo(that: Point): Boolean {
    return this.i == that.i && this.j == that.j - 1
}

infix fun Point.above(that: Point): Boolean {
    return this.i == that.i - 1 && this.j == that.j
}

infix fun Point.rightTo(that: Point): Boolean {
    return this.i == that.i && this.j == that.j + 1
}

infix fun Point.below(that: Point): Boolean {
    return this.i == that.i + 1 && this.j == that.j
}
