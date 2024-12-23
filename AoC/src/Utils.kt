import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.abs
import kotlin.system.exitProcess

typealias Point = Pair<Int, Int>
typealias Position = Pair<Point, Direction>
typealias Position8 = Pair<Point, Direction8>
typealias CharGrid = Array<CharArray>
typealias IntGrid = Array<IntArray>
typealias Grid<T> = Array<Array<T>>

fun readInput(name: String) =
    File("res", "$name.txt").readLines()

fun testInput(input: String) =
    input.trimIndent().lines()

fun expect(actual: Any, expected: Any) {
    if (actual != expected) {
        System.err.println(
            """
                Test failed
                Expected: $expected
                But was:  $actual
            """.trimIndent(),
        )
        exitProcess(1)
    }
}

interface Day {
    private val input: List<String>
        get() = readInput(this::class.qualifiedName.orEmpty().replace('.', '/'))

    fun part1(input: List<String>): Any
    fun part2(input: List<String>): Any
    fun test1(input: String, expected: Any) = expect(part1(testInput(input)), expected)
    fun test2(input: String, expected: Any) = expect(part2(testInput(input)), expected)
    fun result1() = println("Part 1: ${part1(input)}")
    fun result2() = println("Part 2: ${part2(input)}")
}

fun List<String>.split(predicate: (String) -> Boolean): List<List<String>> {
    return buildList {
        var current = mutableListOf<String>()
        this@split.forEach { line ->
            if (predicate(line)) {
                add(current.toList())
                current = mutableListOf()
            } else {
                current.add(line)
            }
        }
        if (current.isNotEmpty()) {
            add(current.toList())
        }
    }
}

fun List<String>.charGrid(): Triple<CharGrid, Int, Int> {
    val grid = Array(size) { this[it].toCharArray() }
    val (n, m) = grid.gridSize()
    return Triple(grid, n, m)
}

inline fun CharGrid.firstOrThrow(predicate: (Char, Int, Int) -> Boolean): Triple<Char, Int, Int> {
    forEach { c, i, j ->
        if (predicate(c, i, j)) {
            return Triple(c, i, j)
        }
    }
    error("No element matches the predicate")
}

inline fun CharGrid.firstPosition(predicate: (Char) -> Boolean): Point {
    forEach { c, i, j ->
        if (predicate(c)) {
            return Point(i, j)
        }
    }
    error("No element matches the predicate")
}

inline fun CharGrid.forEach(action: (Char, Int, Int) -> Unit) {
    val (n, m) = gridSize()
    for (i in 0..<n) {
        for (j in 0..<m) {
            action(this[i][j], i, j)
        }
    }
}

inline fun IntGrid.forEach(action: (Int, Int, Int) -> Unit) {
    val (n, m) = gridSize()
    for (i in 0..<n) {
        for (j in 0..<m) {
            action(this[i][j], i, j)
        }
    }
}

fun CharGrid(n: Int, m: Int, init: (Int, Int) -> Char): CharGrid {
    return Array(n) { i ->
        CharArray(m) { j ->
            init(i, j)
        }
    }
}

operator fun CharGrid.get(point: Point): Char {
    require(point inside this) { "Point $point is out of grid bounds ${gridSize()}" }
    return this[point.i][point.j]
}

operator fun CharGrid.set(point: Point, c: Char) {
    require(point inside this)
    this[point.i][point.j] = c
}

operator fun IntGrid.get(point: Point): Int {
    require(point inside this)
    return this[point.i][point.j]
}

fun List<String>.intGrid(): Triple<IntGrid, Int, Int> {
    val grid = Array(size) { this[it].map(Char::digitToInt).toIntArray() }
    val (n, m) = grid.gridSize()
    return Triple(grid, n, m)
}

fun CharGrid.gridSize(): Pair<Int, Int> {
    return size to first().size
}

fun IntGrid.gridSize(): Pair<Int, Int> {
    return size to first().size
}

fun <T> Grid<T>.gridSize(): Pair<Int, Int> {
    return size to first().size
}

val Point.i
    get() = first

val Point.j
    get() = second

infix fun Point.inside(grid: CharGrid): Boolean {
    val (n, m) = grid.gridSize()
    return i in 0..<n && j in 0..<m
}

infix fun Point.inside(grid: IntGrid): Boolean {
    val (n, m) = grid.gridSize()
    return i in 0..<n && j in 0..<m
}

fun Point.moveBy(i: Int = 0, j: Int = 0): Point {
    return copy(first + i, second + j)
}

infix fun Point.moveTo(direction: Direction) = when (direction) {
    Direction.Left -> moveBy(j = -1)
    Direction.Up -> moveBy(i = -1)
    Direction.Right -> moveBy(j = +1)
    Direction.Down -> moveBy(i = +1)
}

infix fun Point.moveTo(direction: Direction8) = when (direction) {
    Direction8.W -> moveBy(j = -1)
    Direction8.NW -> moveBy(i = -1, j = -1)
    Direction8.N -> moveBy(i = -1)
    Direction8.NE -> moveBy(i = -1, j = 1)
    Direction8.E -> moveBy(j = +1)
    Direction8.SE -> moveBy(i = 1, j = 1)
    Direction8.S -> moveBy(i = 1)
    Direction8.SW -> moveBy(i = 1, j = -1)
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

fun Point.adjacentSides(): List<Point> {
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

infix fun Point.manhattanDistanceTo(that: Point): Int {
    return abs(this.i - that.i) + abs(this.j - that.j)
}

val Position.point
    get() = first

val Position.direction
    get() = second

val Position8.point8
    get() = first

val Position8.direction8
    get() = second

enum class Direction { Left, Up, Right, Down; }

fun Direction.turnLeft() = when (this) {
    Direction.Left -> Direction.Down
    Direction.Up -> Direction.Left
    Direction.Right -> Direction.Up
    Direction.Down -> Direction.Right
}

fun Direction.turnRight() = when (this) {
    Direction.Left -> Direction.Up
    Direction.Up -> Direction.Right
    Direction.Right -> Direction.Down
    Direction.Down -> Direction.Left
}

enum class Direction8 { W, NW, N, NE, E, SE, S, SW; }

fun gcd(x: Long, y: Long): Long {
    return if (y == 0L) x else gcd(y, x % y)
}

fun lcm(x: Long, y: Long): Long {
    return x * y / gcd(x, y)
}

fun lcm(values: List<Long>): Long {
    return values.reduce { lcm, value -> lcm(lcm, value) }
}

inline fun <T, R> bfs(
    start: T,
    next: (T) -> List<T>,
    key: (T) -> R,
    visit: (T) -> Unit,
    finished: (T) -> Boolean = { false },
) {
    val toVisit = ArrayDeque<T>().apply { add(start) }
    val visited = mutableSetOf<R>()
    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst()
        if (key(current) !in visited) {
            visited.add(key(current))
            visit(current)
            if (finished(current)) {
                return
            }
            toVisit.addAll(next(current))
        }
    }
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}
