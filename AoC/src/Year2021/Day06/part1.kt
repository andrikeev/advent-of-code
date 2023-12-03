package Year2021.Day06

import java.io.File

fun main() {
    val testInput = "3,4,3,1,2".split(",")
    assert(calculateFish(18, testInput) == 26)

    val input = File("res/Year2021/Day06/input.txt")
        .readLines()
        .first()
        .split(",")
    println(calculateFish(80, input))
}

private fun calculateFish(day: Int, input: List<String>): Int {
    val fish = input.map(String::toInt).toMutableList()
    repeat(day) {
        var newFish = 0
        repeat(fish.size) { i ->
            if (fish[i] == 0) {
                fish[i] = 6
                newFish++
            } else {
                fish[i] -= 1
            }
        }
        repeat(newFish) { fish.add(8) }
    }
    return fish.size
}
