package Year2023

import readInput
import testInput
import kotlin.math.roundToLong

data class P(val x: Double, val y: Double, val z: Double)
data class V(val x: Int, val y: Int, val z: Int)
data class H(val s: P, val v: V)

fun main() {

    fun hailstones(input: List<String>): List<H> {
        return input.map { line ->
            val (p, v) = line.split("@")
            val (px, py, pz) = p
                .split(",")
                .map(String::trim)
                .map(String::toDouble)
            val (vx, vy, vz) = v
                .split(",")
                .map(String::trim)
                .map(String::toInt)
            H(P(px, py, pz), V(vx, vy, vz))
        }
    }

    fun intersect(s1: P, v1: V, s2: P, v2: V, range: LongRange): Boolean {
        val a1 = -v1.y / v1.x.toDouble()
        val c1 = a1 * s1.x + s1.y

        val a2 = -v2.y / v2.x.toDouble()
        val c2 = a2 * s2.x + s2.y

        val d = a1 - a2

        if (d == 0.0) {
            return false
        }

        val cx = (c1 - c2) / d
        val cy = (a1 * c2 - a2 * c1) / d

        return cx >= range.first.toDouble() &&
                cx <= range.last.toDouble() &&
                ((cx - s1.x) * v1.x) >= 0 &&
                ((cy - s1.y) * v1.y) >= 0 &&
                cy >= range.first.toDouble() &&
                cy <= range.last.toDouble() &&
                ((cx - s2.x) * v2.x) >= 0 &&
                ((cy - s2.y) * v2.y) >= 0
    }

    fun part1(
        input: List<String>,
        range: LongRange = 7L..27L,
    ): Int {
        val hails = hailstones(input)
        var res = 0
        for (i in 0..hails.lastIndex) {
            val l1 = hails[i]
            for (j in i + 1..hails.lastIndex) {
                val l2 = hails[j]
                if (intersect(l1.s, l1.v, l2.s, l2.v, range)) {
                    res++
                }
            }
        }
        return res
    }

    fun part2(input: List<String>): Long {
        val hailstones = hailstones(input)

        //
        // For every hailstone there is a system of equations.
        // Where t[i] is time of collision.
        //
        // x[i] + t[i] * vx[i] = x + t[i] * vx
        // y[i] + t[i] * vy[i] = y + t[i] * vy
        // z[i] + t[i] * vz[i] = z + t[i] * vz
        //
        // ---------------------------
        //
        // x - x[i] = (vx[i] - vx) * t[i]
        // y - y[i] = (vy[i] - vy) * t[i]
        // z - z[i] = (vz[i] - vz) * t[i]
        //
        // ---------------------------
        //
        // p - p[i] = (v[i] - v) * t[i] (vector = vector * scalar)
        //
        // So we can eliminate t[i]
        //
        // (p - p[i]) x (v[i] - v) = 0
        // (p.y - p[i].y) * (v[i].z - v.z) - (p.z - p[i].z) * (v[i].y - v.y) = 0
        // (p.y * v[i].z - p.y * v.z - p[i].y * v[i].z + p[i].y * v.z) - (p.z * v[i].y - p.z * v.y - p[i].z * v[i].y + p[i].z * v.y) = 0
        // v[i].z * p.y - v[i].y * p.z - p[i].z * v.y + p[i].y * v.z = p[i].y * v[i].z - p[i].z * v[i].y + p.y * v.z - p.z * v.y
        //

        val (a, b, c) = hailstones.takeLast(3)
        val matrix = arrayOf(
            doubleArrayOf(
                0.0,
                (a.v.z - b.v.z).toDouble(),
                (b.v.y - a.v.y).toDouble(),
                0.0,
                (b.s.z - a.s.z),
                (a.s.y - b.s.y),
                (a.s.y * a.v.z - a.s.z * a.v.y - b.s.y * b.v.z + b.s.z * b.v.y)
            ),
            doubleArrayOf(
                (b.v.z - a.v.z).toDouble(),
                0.0,
                (a.v.x - b.v.x).toDouble(),
                (a.s.z - b.s.z),
                0.0,
                (b.s.x - a.s.x),
                (a.s.z * a.v.x - a.s.x * a.v.z - b.s.z * b.v.x + b.s.x * b.v.z)
            ),
            doubleArrayOf(
                (a.v.y - b.v.y).toDouble(),
                (b.v.x - a.v.x).toDouble(),
                0.0,
                (b.s.y - a.s.y),
                (a.s.x - b.s.x),
                0.0,
                (a.s.x * a.v.y - a.s.y * a.v.x - b.s.x * b.v.y + b.s.y * b.v.x)
            ),
            doubleArrayOf(
                0.0,
                (a.v.z - c.v.z).toDouble(),
                (c.v.y - a.v.y).toDouble(),
                0.0,
                (c.s.z - a.s.z),
                (a.s.y - c.s.y),
                (a.s.y * a.v.z - a.s.z * a.v.y - c.s.y * c.v.z + c.s.z * c.v.y)
            ),
            doubleArrayOf(
                (c.v.z - a.v.z).toDouble(),
                0.0,
                (a.v.x - c.v.x).toDouble(),
                (a.s.z - c.s.z),
                0.0,
                (c.s.x - a.s.x),
                (a.s.z * a.v.x - a.s.x * a.v.z - c.s.z * c.v.x + c.s.x * c.v.z)
            ),
            doubleArrayOf(
                (a.v.y - c.v.y).toDouble(),
                (c.v.x - a.v.x).toDouble(),
                0.0,
                (c.s.y - a.s.y),
                (a.s.x - c.s.x),
                0.0,
                (a.s.x * a.v.y - a.s.y * a.v.x - c.s.x * c.v.y + c.s.y * c.v.x)
            )
        )

        // gaussian elimination
        for (i in matrix.indices) {
            for (j in i..<matrix.size) {
                if (matrix[j][i] != 0.0) {
                    matrix[i] = matrix[j].also { matrix[j] = matrix[i] }
                    break
                }
            }
            for (j in matrix[0].lastIndex downTo i) {
                matrix[i][j] /= matrix[i][i]
            }
            for (j in i + 1..<matrix.size) {
                for (k in matrix[0].lastIndex downTo i) {
                    matrix[j][k] -= matrix[j][i] * matrix[i][k]
                }
            }
        }

        // backwards substitution
        for (i in matrix.indices.reversed()) {
            for (j in 0..<i) {
                matrix[j][matrix[0].lastIndex] -= matrix[j][i] * matrix[i].last()
            }
        }

        return matrix.asSequence().take(3).sumOf { it.last().roundToLong() }
    }

    val testInput = testInput("""
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """)
    check(part1(testInput).also { println("part1 test: $it") } == 2)
    check(part2(testInput).also { println("part2 test: $it") } == 47L)

    val input = readInput("Year2023/Day24")
    println(part1(input, 200000000000000L..400000000000000L))
    println(part2(input))
}
