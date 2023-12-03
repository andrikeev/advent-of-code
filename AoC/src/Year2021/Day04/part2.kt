package Year2021.Day04

import java.io.File

/*
* https://adventofcode.com/2021/day/4
*/
fun main() {
    val testInput = listOf(
        "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1",
        "",
        "22 13 17 11  0",
        "8  2 23  4 24",
        "21  9 14 16  7",
        "6 10  3 18  5",
        "1 12 20 15 19",
        "",
        "3 15  0  2 22",
        "9 18 13 17  5",
        "19  8  7 25 23",
        "20 11 10 24  4",
        "14 21 16 12  6",
        "",
        "14 21 17 24  4",
        "10 16 15  9 19",
        "18  8 23 26 20",
        "22 11 13  6  5",
        "2  0 12  3  7",
    )
    assert(calculateValue(testInput) == 1924)

    val input = File("res/Year2021/Day04/input.txt")
        .readLines()
    println(calculateValue(input))
}

private fun calculateValue(lines: List<String>): Int {
    val (numbers, boards) = parseInput(lines)
    var bingoCount = 0
    numbers.forEach { number ->
        boards.filter { !it.isBingo() }
            .forEach { board ->
                board.mark(number)
                if (board.isBingo()) {
                    bingoCount++
                    if (bingoCount == boards.size) {
                        val bingoSum = board.calcBingoSum()
                        return bingoSum * number
                    }
                }
            }
    }
    return 0
}
