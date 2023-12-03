package Year2021.Day06

import java.io.File

fun main() {
    val testInput = "3,4,3,1,2".split(",")
    assert(calculateFish(256, testInput) == 26984457539L)

    val input = File("res/Year2021/Day06/input.txt")
        .readLines()
        .first()
        .split(",")
    println(calculateFish(256, input))
}

private fun calculateFish(day: Int, input: List<String>): Long {
    val fish = input.map(String::toInt)
    var map = mutableMapOf<Int, Long>().apply {
        fish.forEach { i ->
            this[i] = this.getOrDefault(i, 0) + 1
        }
    }
    repeat(day) {
        val newMap = mutableMapOf<Int, Long>()
        map.forEach { (k, v) ->
            if (k == 0) {
                newMap[6] = newMap.getOrDefault(6, 0L) + v
                newMap[8] = newMap.getOrDefault(8, 0L) + v
            } else {
                val newK = k - 1
                newMap[newK] = newMap.getOrDefault(newK, 0L) + v
            }
        }
        map = newMap
    }
    return map.values.sum()
}
