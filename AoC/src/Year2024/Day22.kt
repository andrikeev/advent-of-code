package Year2024

import Day

private object Day22 : Day {
    private fun Long.next(): Long {
        var num = this

        num = xor (num * 64)
        num %= 16777216

        num = num xor (num.floorDiv(32))
        num %= 16777216

        num = num xor (num * 2048)
        num %= 16777216

        return num
    }

    override fun part1(input: List<String>): Any {
        val secrets = input.map(String::toLong)
        return secrets.sumOf {
            var secret = it
            repeat(2000) {
                secret = secret.next()
            }
            secret
        }
    }

    override fun part2(input: List<String>): Any {
        val secrets = input.map(String::toLong)
        val map = mutableMapOf<String, Array<Long>>()
        fun ArrayDeque<Int>.key(): String = joinToString(",")
        secrets.forEachIndexed { index, start ->
            var secret = start
            val deque = ArrayDeque<Int>()
            repeat(2000) {
                val prevPrice = secret % 10
                secret = secret.next()
                val curPrice = secret % 10
                val diff = (curPrice - prevPrice).toInt()
                deque.addLast(diff)
                if (deque.size > 4) {
                    deque.removeFirst()
                }
                if (deque.size == 4) {
                    val prices = map.getOrPut(deque.key()) { Array(secrets.size) { 0 } }
                    if (prices[index] == 0L) {
                        prices[index] = curPrice
                    }
                }
            }
        }
        return map
            .mapValues { it.value.sum() }
            .maxBy(Map.Entry<String, Long>::value)
            .value
    }
}

fun main() = with(Day22) {
    test1(
        input = """
            1
            10
            100
            2024
        """.trimIndent(),
        expected = 37327623L,
    )
    result1()

    test2(
        input = """
            1
            2
            3
            2024
        """.trimIndent(),
        expected = 23L,
    )
    result2()
}
