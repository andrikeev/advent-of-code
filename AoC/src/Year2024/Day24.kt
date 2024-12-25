package Year2024

import Day
import split
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor

private object Day24 : Day {
    private interface Op {
        operator fun invoke(a: Byte, b: Byte): Byte
        operator fun invoke(ins: List<Byte>): Byte = ins.reduce(::invoke)
    }

    private object And : Op {
        override fun invoke(a: Byte, b: Byte) = a and b
    }

    private object Or : Op {
        override fun invoke(a: Byte, b: Byte) = a or b
    }

    private object Xor : Op {
        override fun invoke(a: Byte, b: Byte) = a xor b
    }

    private data class Gate(
        val ins: List<String>,
        val out: String,
        val op: Op,
    )

    override fun part1(input: List<String>): Any {
        val (a, b) = input.split(String::isEmpty)
        val outs = mutableSetOf<String>()
        val gates = b.map { line ->
            val (left, out) = line.split(" -> ")
            val (i1, op, i2) = left.split(" ")
            Gate(
                ins = listOf(i1, i2),
                out = out.also {
                    if (it.startsWith('z')) {
                        outs.add(it)
                    }
                },
                op = when (op) {
                    "AND" -> And
                    "OR" -> Or
                    "XOR" -> Xor
                    else -> error("Unknown op: $op")
                }
            )
        }
        val wires = mutableMapOf<String, Byte>()
        a.forEach {
            val (name, value) = it.split(": ")
            wires[name] = value.toByte()
        }

        while (!outs.all(wires::containsKey)) {
            gates
                .filter { wires.keys.containsAll(it.ins) }
                .forEach { gate ->
                    wires[gate.out] = gate.op(gate.ins.map(wires::getValue))
                }
        }

        return outs
            .sortedDescending()
            .map(wires::getValue)
            .joinToString("")
            .toLong(2)
    }

    override fun part2(input: List<String>): Any {
        return 0
    }
}

fun main() = with(Day24) {

    test1(
        input = """
            x00: 1
            x01: 0
            x02: 1
            x03: 1
            x04: 0
            y00: 1
            y01: 1
            y02: 1
            y03: 1
            y04: 1

            ntg XOR fgs -> mjb
            y02 OR x01 -> tnw
            kwq OR kpj -> z05
            x00 OR x03 -> fst
            tgd XOR rvg -> z01
            vdt OR tnw -> bfw
            bfw AND frj -> z10
            ffh OR nrd -> bqk
            y00 AND y03 -> djm
            y03 OR y00 -> psh
            bqk OR frj -> z08
            tnw OR fst -> frj
            gnj AND tgd -> z11
            bfw XOR mjb -> z00
            x03 OR x00 -> vdt
            gnj AND wpb -> z02
            x04 AND y00 -> kjc
            djm OR pbm -> qhw
            nrd AND vdt -> hwm
            kjc AND fst -> rvg
            y04 OR y02 -> fgs
            y01 AND x02 -> pbm
            ntg OR kjc -> kwq
            psh XOR fgs -> tgd
            qhw XOR tgd -> z09
            pbm OR djm -> kpj
            x03 XOR y03 -> ffh
            x00 XOR y04 -> ntg
            bfw OR bqk -> z06
            nrd XOR fgs -> wpb
            frj XOR qhw -> z04
            bqk OR frj -> z07
            y03 OR x01 -> nrd
            hwm AND bqk -> z03
            tgd XOR rvg -> z12
            tnw OR pbm -> gnj
        """.trimIndent(),
        expected = 2024L,
    )
    result1()

    test2(
        input = """
            x00: 1
            x01: 0
            x02: 1
            x03: 1
            x04: 0
            y00: 1
            y01: 1
            y02: 1
            y03: 1
            y04: 1

            ntg XOR fgs -> mjb
            y02 OR x01 -> tnw
            kwq OR kpj -> z05
            x00 OR x03 -> fst
            tgd XOR rvg -> z01
            vdt OR tnw -> bfw
            bfw AND frj -> z10
            ffh OR nrd -> bqk
            y00 AND y03 -> djm
            y03 OR y00 -> psh
            bqk OR frj -> z08
            tnw OR fst -> frj
            gnj AND tgd -> z11
            bfw XOR mjb -> z00
            x03 OR x00 -> vdt
            gnj AND wpb -> z02
            x04 AND y00 -> kjc
            djm OR pbm -> qhw
            nrd AND vdt -> hwm
            kjc AND fst -> rvg
            y04 OR y02 -> fgs
            y01 AND x02 -> pbm
            ntg OR kjc -> kwq
            psh XOR fgs -> tgd
            qhw XOR tgd -> z09
            pbm OR djm -> kpj
            x03 XOR y03 -> ffh
            x00 XOR y04 -> ntg
            bfw OR bqk -> z06
            nrd XOR fgs -> wpb
            frj XOR qhw -> z04
            bqk OR frj -> z07
            y03 OR x01 -> nrd
            hwm AND bqk -> z03
            tgd XOR rvg -> z12
            tnw OR pbm -> gnj
        """.trimIndent(),
        expected = 0,
    )
    result2()
}
