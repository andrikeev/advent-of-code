package Year2021

import expect
import readInput
import testInput

fun main() {

    fun readBits(input: String): MutableList<Char> {
        return input
            .map { it.digitToInt(16) }
            .map { it.toString(2) }
            .map { it.padStart(4, '0') }
            .flatMapTo(mutableListOf(), String::toList)
    }

    fun MutableList<Char>.readStr(n: Int): String {
        return buildString { repeat(n) { append(removeFirst()) } }
    }

    fun MutableList<Char>.readInt(n: Int): Int {
        return readStr(n).toInt(2)
    }

    fun MutableList<Char>.readPacketVersion(): Int {
        var result = readInt(3)
        val id = readInt(3)
        if (id == 4) {
            var last = false
            while (!last) {
                last = readInt(1) == 0
                readStr(4)
            }
        } else {
            val lenId = readInt(1)
            if (lenId == 0) {
                val len = readInt(15)
                val currentSize = size
                while (currentSize - size < len) {
                    result += readPacketVersion()
                }
            } else {
                val num = readInt(11)
                repeat(num) {
                    result += readPacketVersion()
                }
            }
        }
        return result
    }

    fun MutableList<Char>.readPacketValue(): Long {
        readInt(3)
        val id = readInt(3)
        if (id == 4) {
            return buildString {
                var last = false
                while (!last) {
                    last = readInt(1) == 0
                    append(readStr(4))
                }
            }.toLong(2)
        } else {
            val values = mutableListOf<Long>()
            val lenId = readInt(1)
            if (lenId == 0) {
                val len = readInt(15)
                val currentSize = size
                while (currentSize - size < len) {
                    values.add(readPacketValue())
                }
            } else {
                val num = readInt(11)
                repeat(num) {
                    values.add(readPacketValue())
                }
            }
            return when (id) {
                0 -> values.sum()
                1 -> values.reduce(Long::times)
                2 -> values.min()
                3 -> values.max()
                5 -> if (values.first() > values.last()) 1 else 0
                6 -> if (values.first() < values.last()) 1 else 0
                7 -> if (values.first() == values.last()) 1 else 0
                else -> error("!!!")
            }
        }
    }

    fun part1(input: List<String>): Any {
        val bits = readBits(input.single())
        return bits.readPacketVersion()
    }

    fun part2(input: List<String>): Any {
        val bits = readBits(input.single())
        return bits.readPacketValue()
    }

    val test1Input1 = testInput("8A004A801A8002F478")
    val test1Input2 = testInput("620080001611562C8802118E34")
    val test1Input3 = testInput("C0015000016115A2E0802F182340")
    val test1Input4 = testInput("A0016C880162017C3686B18A3D4780")
    val input = readInput("Year2021/Day16")

    // part 1
    expect(part1(test1Input1), 16)
    expect(part1(test1Input2), 12)
    expect(part1(test1Input3), 23)
    expect(part1(test1Input4), 31)
    println(part1(input))

    val test2Input1 = testInput("C200B40A82")
    val test2Input2 = testInput("04005AC33890")
    val test2Input3 = testInput("880086C3E88112")
    val test2Input4 = testInput("CE00C43D881120")
    val test2Input5 = testInput("D8005AC2A8F0")
    val test2Input6 = testInput("F600BC2D8F")
    val test2Input7 = testInput("9C005AC2F8F0")
    val test2Input8 = testInput("9C0141080250320F1802104A08")

    // part 2
    expect(part2(test2Input1), 3L)
    expect(part2(test2Input2), 54L)
    expect(part2(test2Input3), 7L)
    expect(part2(test2Input4), 9L)
    expect(part2(test2Input5), 1L)
    expect(part2(test2Input6), 0L)
    expect(part2(test2Input7), 0L)
    expect(part2(test2Input8), 1L)
    println(part2(input))
}
