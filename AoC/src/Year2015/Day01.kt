package Year2015

import expect
import readInput

fun main() {

    fun part1(input: String): Any {
        var res = 0
        input.forEach { c ->
            if (c == '(') res++ else res--
        }
        return res
    }

    fun part2(input: String): Any {
        var res = 0
        input.forEachIndexed { index, c ->
            if (c == '(') res++ else res--
            if (res == -1) return index + 1
        }
        return 0
    }

    val input = readInput("Year2015/Day01")

    // part 1
    expect(part1("(())"), 0)
    expect(part1("()()"), 0)
    expect(part1("((("), 3)
    expect(part1("(()(()("), 3)
    expect(part1("))((((("), 3)
    expect(part1("())"), -1)
    expect(part1("))("), -1)
    expect(part1(")))"), -3)
    expect(part1(")())())"), -3)
    println(part1(input.first()))

    // part 2
    expect(part2(")"), 1)
    expect(part2("()())"), 5)
    println(part2(input.first()))
}
