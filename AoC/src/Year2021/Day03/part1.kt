package Year2021.Day03

import java.io.File

/*
* Each bit in the gamma rate can be determined by finding
* the most common bit in the corresponding position of
* all numbers in the diagnostic report.
*
* The epsilon rate is calculated in a similar way;
* rather than use the most common bit, the least common
* bit from each position is used.
*
* Use the binary numbers in your diagnostic report to
* calculate the gamma rate and epsilon rate, then multiply
* them together. What is the power consumption of the submarine?
* */
fun main() {
    val testReport = listOf(
        "00100",
        "11110",
        "10110",
        "10111",
        "10101",
        "01111",
        "00111",
        "11100",
        "10000",
        "11001",
        "00010",
        "01010",
    )
    assert(encodePowerConsumption(testReport) == 198)

    val report = File("res/Year2021/Day03/input.txt").readLines()
    println(encodePowerConsumption(report))
}

private fun encodePowerConsumption(report: List<String>): Int {
    return report
        .fold(MutableList(report.first().length) { 0 }) { acc, line ->
            acc.apply {
                line.forEachIndexed { index, bit ->
                    acc[index] += bit.digitToInt()
                }
            }
        }
        .fold(0 to 0) { acc, count ->
            val (mostCommonBit, leastCommonBit) = if (count > (report.size / 2)) {
                1 to 0
            } else {
                0 to 1
            }
            ((acc.first shl 1) + mostCommonBit) to ((acc.second shl 1) + leastCommonBit)
        }
        .run { first * second }
}
