package Year2024

import Day

private object Day17 : Day {
    data class Command(val opcode: Int, val operand: Int)

    class Device(
        private var a: Long = 0,
        private var b: Long = 0,
        private var c: Long = 0,
        private var program: List<Command> = emptyList(),
    ) {
        private var pointer = 0
        private val output = mutableListOf<Int>()

        fun run(): List<Int> {
            while (pointer < program.size) {
                runOp()
            }
            return output
        }

        fun runSingleLoop(registerA: Long): Int {
            a = registerA
            pointer = 0
            output.clear()
            while (output.isEmpty()) {
                runOp()
            }
            return output.single()
        }

        @Suppress("ReplaceWithOperatorAssignment")
        private fun runOp() {
            val (opcode, operand) = program[pointer++]
            when (opcode) {
                0 -> a = a / 1.shl(combo(operand).toInt())
                1 -> b = b xor operand.toLong()
                2 -> b = combo(operand) % 8
                3 -> if (a != 0L) pointer = operand
                4 -> b = b xor c
                5 -> output.add((combo(operand) % 8).toInt())
                6 -> b = a / 1.shl(combo(operand).toInt())
                7 -> c = a / 1.shl(combo(operand).toInt())
            }
        }

        private fun combo(operand: Int): Long {
            return when (operand) {
                in 0..3 -> operand.toLong()
                4 -> a
                5 -> b
                6 -> c
                else -> error("Invalid operand: $operand")
            }
        }
    }

    override fun part1(input: List<String>): Any {
        val device = Device(
            a = input[0].substringAfterLast(": ").toLong(),
            b = input[1].substringAfterLast(": ").toLong(),
            c = input[2].substringAfterLast(": ").toLong(),
            program = input[4]
                .substringAfterLast(": ")
                .split(',')
                .map(String::toInt)
                .chunked(2)
                .map { (opcode, operand) ->
                    Command(opcode, operand)
                },
        )
        val result = device.run()
        return result.joinToString(",")
    }

    override fun part2(input: List<String>): Any {
        val program = input[4]
            .substringAfterLast(": ")
            .split(',')
            .map(String::toInt)
        val device = Device(
            a = input[0].substringAfterLast(": ").toLong(),
            b = input[1].substringAfterLast(": ").toLong(),
            c = input[2].substringAfterLast(": ").toLong(),
            program = program
                .chunked(2)
                .map { (opcode, operand) -> Command(opcode, operand) },
        )

        val range = (0L..7L)
        var possibleA = range.toList()
        for (i in program.reversed().dropLast(1)) {
            possibleA = possibleA
                .filter { device.runSingleLoop(it) == i }
                .flatMap { range.map { j -> (it shl 3) + j } }
                .distinct()
        }

        return possibleA
            .filter { device.runSingleLoop(it) == program.first() }
            .min()
    }
}

fun main() = with(Day17) {
    test1(
        input = """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
        """.trimIndent(),
        expected = "4,6,3,5,6,3,5,2,1,0",
    )
    result1()

    test2(
        input = """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0
        """.trimIndent(),
        expected = 117440L,
    )
    result2()
}
