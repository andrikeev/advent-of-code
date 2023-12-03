package Year2021.Day05

import java.io.File
import kotlin.math.max

fun main() {
    val testInput = listOf(
        "0,9 -> 5,9",
        "8,0 -> 0,8",
        "9,4 -> 3,4",
        "2,2 -> 2,1",
        "7,0 -> 7,4",
        "6,4 -> 2,0",
        "0,9 -> 2,9",
        "3,4 -> 1,4",
        "0,0 -> 8,8",
        "5,5 -> 8,2",
    )
    assert(calcOverpasses(HorizontalAndVerticalField(), testInput) == 5)

    val input = File("res/Year2021/Day05/input.txt")
        .readLines()
    println(calcOverpasses(HorizontalAndVerticalField(), input))
}

private class HorizontalAndVerticalField : BaseField() {
    override fun addLine(xRange: IntProgression, yRange: IntProgression) {
        adjust(max(xRange.maxOrNull() ?: 0, yRange.maxOrNull() ?: 0))
        when {
            xRange.count() == 1 -> {
                val x = xRange.first
                yRange.forEach { y -> inc(x, y) }
            }

            yRange.count() == 1 -> {
                val y = yRange.first
                xRange.forEach { x -> inc(x, y) }
            }
        }
    }
}
