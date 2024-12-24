package Year2024

import Day

private object Day23 : Day {
    override fun part1(input: List<String>): Any {
        val graph = mutableMapOf<String, MutableList<String>>()
        input.forEach { line ->
            val (a, b) = line.split('-')
            graph.getOrPut(a, ::mutableListOf).add(b)
            graph.getOrPut(b, ::mutableListOf).add(a)
        }

        val lans = mutableSetOf<Set<String>>()

        graph.forEach { a, connections ->
            for (i in connections.indices) {
                for (j in i + 1..<connections.size) {
                    val b = connections[i]
                    val c = connections[j]
                    if (graph.getValue(b).contains(c) && graph.getValue(c).contains(b)) {
                        lans.add(setOf(a, b, c))
                    }
                }
            }
        }

        return lans.count { lan -> lan.any { it.startsWith('t') } }
    }

    override fun part2(input: List<String>): Any {
        val graph = mutableMapOf<String, MutableList<String>>()
        input.forEach { line ->
            val (a, b) = line.split('-')
            graph.getOrPut(a, ::mutableListOf).add(b)
            graph.getOrPut(b, ::mutableListOf).add(a)
        }

        val vertices = graph.keys
        val lans = mutableSetOf<Set<String>>()
        vertices.forEach { vertex ->
            lans.add(
                buildSet {
                    add(vertex)
                    vertices.forEach { other ->
                        if (all { graph.getValue(it).contains(other) }) {
                            add(other)
                        }
                    }
                }
            )
        }

        return lans
            .maxBy(Set<String>::size)
            .sorted()
            .joinToString(",")
    }
}

fun main() = with(Day23) {
    test1(
        input = """
            kh-tc
            qp-kh
            de-cg
            ka-co
            yn-aq
            qp-ub
            cg-tb
            vc-aq
            tb-ka
            wh-tc
            yn-cg
            kh-ub
            ta-co
            de-co
            tc-td
            tb-wq
            wh-td
            ta-ka
            td-qp
            aq-cg
            wq-ub
            ub-vc
            de-ta
            wq-aq
            wq-vc
            wh-yn
            ka-de
            kh-ta
            co-tc
            wh-qp
            tb-vc
            td-yn
        """.trimIndent(),
        expected = 7,
    )
    result1()

    test2(
        input = """
            kh-tc
            qp-kh
            de-cg
            ka-co
            yn-aq
            qp-ub
            cg-tb
            vc-aq
            tb-ka
            wh-tc
            yn-cg
            kh-ub
            ta-co
            de-co
            tc-td
            tb-wq
            wh-td
            ta-ka
            td-qp
            aq-cg
            wq-ub
            ub-vc
            de-ta
            wq-aq
            wq-vc
            wh-yn
            ka-de
            kh-ta
            co-tc
            wh-qp
            tb-vc
            td-yn
        """.trimIndent(),
        expected = "co,de,ka,ta",
    )
    result2()
}
