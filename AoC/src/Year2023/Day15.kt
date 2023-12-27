package Year2023

import expect
import readInput

fun main() {

    fun String.hash() = fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

    fun part1(input: String) = input.split(",").sumOf(String::hash)

    fun part2(input: String): Any {
        val boxes = Array<MutableList<Pair<String, Int>>>(256) { mutableListOf() }
        input.split(",").forEach { command ->
            if (command.contains('-')) {
                val l = command.substringBefore('-')
                boxes[l.hash()].removeIf { (label) -> label == l }
            }
            if (command.contains('=')) {
                val (l, p) = command.split("=")
                val box = boxes[l.hash()]

                val i = box.indexOfFirst { (label) -> label == l }
                if (i >= 0) {
                    box[i] = l to p.toInt()
                } else {
                    box.add(l to p.toInt())
                }
            }
        }
        return boxes.withIndex().sumOf { (i, box) ->
            box.withIndex().sumOf { (j, lens) ->
                (i + 1) * (j + 1) * lens.second
            }
        }
    }

    val testInput = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
    val input = readInput("Year2023/Day15")

    // part 1
    expect(part1(testInput), 1320)
    println(part1(input.first()))

    // part 2
    expect(part2(testInput), 145)
    println(part2(input.first()))
}
