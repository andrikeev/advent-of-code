package Year2023

import readInput
import testInput

fun main() {

    fun findGroups(nodes: Set<String>, edges: Set<Pair<String, String>>): Int {
        while (true) {
            val subgroups = nodes.map { mutableSetOf(it) }.toMutableSet()

            while (subgroups.size > 2) {
                val (from, to) = edges.random()
                val s1 = subgroups.first { from in it }
                val s2 = subgroups.first { to in it }
                if (s1 != s2) {
                    s1.addAll(s2)
                    subgroups.removeIf { it == s2 }
                } else {
                    continue
                }
            }

            if (edges.count { (a, b) -> subgroups.first { a in it } != subgroups.first { b in it } } > 3) {
                continue
            }

            return subgroups
                .map(MutableSet<String>::size)
                .reduce(Int::times)
        }
    }

    fun part1(input: List<String>): Int {
        val nodes = mutableSetOf<String>()
        val edges = mutableSetOf<Pair<String, String>>()
        input.forEach { line ->
            val (src, dst) = line.split(": ")
            nodes.add(src)
            dst.split(' ').forEach { d ->
                nodes.add(d)
                if (!edges.contains(src to d) && !edges.contains(d to src)) {
                    edges.add(src to d)
                }
            }
        }

        return findGroups(nodes, edges)
    }

    val testInput = testInput("""
        jqt: rhn xhk nvd
        rsh: frs pzl lsr
        xhk: hfx
        cmg: qnr nvd lhk bvb
        rhn: xhk bvb hfx
        bvb: xhk hfx
        pzl: lsr hfx nvd
        qnr: nvd
        ntq: jqt hfx bvb xhk
        nvd: lhk
        lsr: lhk
        rzs: qnr cmg lsr rsh
        frs: qnr lhk lsr
    """)
    check(part1(testInput).also { println("part1 test: $it") } == 54)

    val input = readInput("Year2023/Day25")
    println(part1(input))
}
