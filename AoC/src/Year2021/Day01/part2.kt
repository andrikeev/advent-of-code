package Year2021.Day01

import java.io.File

/*
* Consider sums of a three-measurement sliding window.
* How many sums are larger than the previous sum?
*/
fun main() {
    val testMeasures = listOf(
        199,
        200,
        208,
        210,
        200,
        207,
        240,
        269,
        260,
        263,
    )
    assert(countWindowIncreases(testMeasures, 3) == 5)

    val measures = File("res/Year2021/Day01/input.txt")
        .readLines()
        .map(String::toInt)
    println(countWindowIncreases(measures, 3))
}

private fun countWindowIncreases(measures: List<Int>, windowSize: Int = 3): Int {
    var numberOfIncreases = 0
    var previousSum = measures.take(windowSize).sum()
    (0..(measures.size - windowSize)).forEach { i ->
        val currentSum = measures.subList(i, i + windowSize).sum()
        if (currentSum > previousSum) {
            numberOfIncreases++
        }
        previousSum = currentSum
    }
    return numberOfIncreases
}
