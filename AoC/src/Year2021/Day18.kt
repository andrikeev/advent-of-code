package Year2021

import expect
import readInput
import testInput

fun main() {

    fun String.explode(): String {
        var brackets = 0
        for (i in indices) {
            when (this[i]) {
                '[' -> brackets++
                ']' -> brackets--
            }
            if (brackets == 0) {
                break
            }
            if (brackets > 4) {
                val j = indexOf(']', startIndex = i)
                val (left, right) = substring(i + 1, j).split(',').map(String::toInt)

                val current = this
                return buildString {
                    val prefix = current.substring(0, i)
                    var e = prefix.indexOfLast(Char::isDigit)
                    if (e >= 0) {
                        var s = e
                        while (true) {
                            if (s > 0 && prefix[s - 1].isDigit()) {
                                s--
                            } else {
                                break
                            }
                        }
                        val number = prefix.substring(s, e + 1).toInt()
                        append(prefix.substring(0, s))
                        append(number + left)
                        append(prefix.substring(e + 1))
                    } else {
                        append(prefix)
                    }
                    append(0)
                    val suffix = current.substring(j + 1)
                    val s = suffix.indexOfFirst(Char::isDigit)
                    if (s >= 0) {
                        e = s
                        while (true) {
                            if (e < suffix.lastIndex && suffix[e + 1].isDigit()) {
                                e++
                            } else {
                                break
                            }
                        }
                        val number = suffix.substring(s, e + 1).toInt()
                        append(suffix.substring(0, s))
                        append(number + right)
                        append(suffix.substring(e + 1))
                    } else {
                        append(suffix)
                    }
                }
            }
        }
        return this
    }

    fun String.split(): String {
        var s = 0
        for (i in indices) {
            if (this[i].isDigit()) {
                s++
            } else {
                s = 0
            }
            if (s > 1) {
                val j = i - 1
                val current = this
                return buildString {
                    val toSplit = current
                        .substring(j)
                        .takeWhile(Char::isDigit)
                    val num = toSplit.toInt()
                    val left = num.floorDiv(2)
                    val right = num - left
                    append(current.substring(0, j))
                    append('[', left, ',', right, ']')
                    append(current.substring(j + toSplit.length))
                }
            }
        }
        return this
    }

    fun String.reduce(): String {
        var current = this
        while (true) {
            val exploded = current.explode()
            if (current != exploded) {
                current = exploded
                continue
            }

            val split = current.split()
            if (current != split) {
                current = split
                continue
            }

            break
        }
        return current
    }

    fun String.add(other: String): String {
        return "[$this,$other]".reduce()
    }

    fun String.magnitude(): Int {
        return if (contains('[')) {
            var brackets = 1
            var i = 1
            do {
                when (this[i++]) {
                    '[' -> brackets++
                    ']' -> brackets--
                }
            } while (brackets > 1)
            val j = indexOf(',', i)
            val left = 3 * substring(1, j).magnitude()
            val right = 2 * substring(j + 1, lastIndex).magnitude()

            left + right
        } else {
            toInt()
        }
    }

    fun part1(input: List<String>): Any {
        return input.reduce(String::add).magnitude()
    }

    fun part2(input: List<String>): Any {
        return input
            .flatMap { a ->
                input
                    .minusElement(a)
                    .flatMap { b ->
                        listOf(
                            a.add(b).magnitude(),
                            b.add(a).magnitude(),
                        )
                    }
            }
            .max()
    }

    val testInput = testInput("""
        [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
        [[[5,[2,8]],4],[5,[[9,9],0]]]
        [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
        [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
        [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
        [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
        [[[[5,4],[7,7]],8],[[8,3],8]]
        [[9,3],[[9,9],[6,[4,9]]]]
        [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
        [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """)
    val input = readInput("Year2021/Day18")

    // part 1
    expect(part1(testInput), 4140)
    println(part1(input))

    // part 2
    expect(part2(testInput), 3993)
    println(part2(input))
}
