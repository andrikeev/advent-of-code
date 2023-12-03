package Year2021.Day03

import java.io.File

/*
* Both the oxygen generator rating and the CO2 scrubber rating are values that can
* be found in your diagnostic report - finding them is the tricky part.
* Both values are located using a similar process that involves filtering out
* values until only one remains. Before searching for either rating value, start
* with the full list of binary numbers from your diagnostic report and consider
* just the first bit of those numbers. Then:
*    - Keep only numbers selected by the bit criteria for the type of rating value
*      for which you are searching. Discard numbers which do not match the bit
*      criteria.
*    - If you only have one number left, stop; this is the rating value for which
*      you are searching.
*    - Otherwise, repeat the process, considering the next bit to the right.
*
* The bit criteria depends on which type of rating value you want to find:
*    - To find oxygen generator rating, determine the most common value (0 or 1) in
*      the current bit position, and keep only numbers with that bit in that position.
*      If 0 and 1 are equally common, keep values with a 1 in the position being
*      considered.
*    - To find CO2 scrubber rating, determine the least common value (0 or 1) in the
*      current bit position, and keep only numbers with that bit in that position.
*      If 0 and 1 are equally common, keep values with a 0 in the position being
*      considered.
*
* Use the binary numbers in your diagnostic report to calculate the oxygen generator
* rating and CO2 scrubber rating, then multiply them together. What is the life
* support rating of the submarine?
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
    assert(calculateLifeSupportRating(testReport) == 230)

    val report = File("res/Year2021/Day03/input.txt").readLines()
    println(calculateLifeSupportRating(report))
}

private fun calculateLifeSupportRating(report: List<String>): Int {
    return calculateOxygenGeneratorRating(report) * calculateCO2ScrubberRating(report)
}

private fun calculateOxygenGeneratorRating(report: List<String>): Int {
    var filteredReport = report
    var position = 0
    val maxPosition = filteredReport.firstOrNull()?.length ?: 0
    while (filteredReport.size > 1 && position < maxPosition) {
        val mostCommonBit = mostCommonBit(filteredReport, position)
        filteredReport = filteredReport.filter { it[position].digitToInt() == mostCommonBit }
        position++
    }
    return filteredReport.first().toInt(2)
}

private fun calculateCO2ScrubberRating(report: List<String>): Int {
    var filteredReport = report
    var position = 0
    val maxPosition = filteredReport.firstOrNull()?.length ?: 0
    while (filteredReport.size > 1 && position < maxPosition) {
        val leastCommonBit = leastCommonBit(filteredReport, position)
        filteredReport = filteredReport.filter { it[position].digitToInt() == leastCommonBit }
        position++
    }
    return filteredReport.first().toInt(2)
}

private fun mostCommonBit(lines: List<String>, position: Int): Int {
    val bitsCount = lines.sumOf { it[position].digitToInt() }
    return when {
        bitsCount >= lines.size - bitsCount -> 1
        else -> 0
    }
}

private fun leastCommonBit(lines: List<String>, position: Int): Int {
    val bitsCount = lines.sumOf { it[position].digitToInt() }
    return when {
        bitsCount >= lines.size - bitsCount -> 0
        else -> 1
    }
}
