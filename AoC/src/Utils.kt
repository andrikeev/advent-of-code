import java.io.File

typealias Point = Pair<Int, Int>

fun readInput(name: String) =
    File("res", "$name.txt").readLines()

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
