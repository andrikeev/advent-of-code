package Year2024

import Day

private object Day07 : Day {
    override fun part1(input: List<String>): Any {
        val equations = input.associate { line ->
            val (part1, part2) = line.split(": ")
            val target = part1.toLong()
            val numbers = part2.split(" ").map(String::toLong)
            target to numbers
        }

        fun check(numbers: List<Long>, target: Long): Boolean {
            fun calc(i: Int, v: Long): Boolean {
                if (i == numbers.size) {
                    return v == target
                }

                val add = calc(i + 1, v + numbers[i])
                val mul = calc(i + 1, v * numbers[i])

                return add || mul
            }

            return calc(1, numbers[0])
        }

        var result = 0L
        equations.forEach { (target, numbers) ->
            if (check(numbers, target)) {
                result += target
            }
        }
        return result
    }

    override fun part2(input: List<String>): Any {
        val equations = input.associate { line ->
            val (part1, part2) = line.split(": ")
            val target = part1.toLong()
            val numbers = part2.split(" ").map(String::toLong)
            target to numbers
        }

        fun check(numbers: List<Long>, target: Long): Boolean {
            fun calc(i: Int, v: Long): Boolean {
                if (i == numbers.size) {
                    return v == target
                }

                val add = calc(i + 1, v + numbers[i])
                val mul = calc(i + 1, v * numbers[i])
                val con = calc(i + 1, "${v}${numbers[i]}".toLong())

                return add || mul || con
            }

            return calc(1, numbers[0])
        }

        var result = 0L
        equations.forEach { (target, numbers) ->
            if (check(numbers, target)) {
                result += target
            }
        }
        return result
    }
}

fun main() = with(Day07) {
    test1(
        input = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """.trimIndent(),
        expected = 3749L,
    )
    result1()

    test2(
        input = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """.trimIndent(),
        expected = 11387L,
    )
    result2()
}
