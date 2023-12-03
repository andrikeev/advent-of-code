package Year2021.Day08

import java.io.File

/*
 * https://adventofcode.com/2021/day/8
 */
fun main() {
    val testInput = listOf(
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe",
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc",
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg",
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb",
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea",
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb",
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe",
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef",
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb",
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce",
    )
    assert(countOutputDigits(testInput) == 61229)

    val input = File("res/Year2021/Day08/input.txt")
        .readLines()
    println(countOutputDigits(input))
}

private fun countOutputDigits(input: List<String>): Int {
    return input.sumOf { countEntry(it) }
}

private fun countEntry(entry: String): Int {
    val (input, output) = entry.split(" | ")
    val digitsMap = mapDigits(input.split(" "))
    return output
        .split(" ")
        .mapNotNull { segments -> digitsMap[segments.toSet()] }
        .joinToString(separator = "")
        .toInt()
}

private fun mapDigits(input: List<String>): Map<Set<Char>, Char> {
    val segmented = input.map(String::toSet)
    val map = mutableMapOf<Char, Set<Char>>()
    segmented.forEach { segments ->
        when (segments.size) {
            2 -> map['1'] = segments
            4 -> map['4'] = segments
            3 -> map['7'] = segments
            7 -> map['8'] = segments
        }
    }
    segmented
        .filter { segments -> segments.size == 6 }
        .forEach { segments ->
            if (segments.containsAll(map.getValue('1'))) {
                if (segments.containsAll(map.getValue('4'))) {
                    map['9'] = segments
                } else {
                    map['0'] = segments
                }
            } else {
                map['6'] = segments
            }
        }
    segmented
        .filter { segments -> segments.size == 5 }
        .forEach { segments ->
            if (segments.containsAll(map.getValue('1'))) {
                map['3'] = segments
            } else {
                if (map.getValue('6').containsAll(segments)) {
                    map['5'] = segments
                } else {
                    map['2'] = segments
                }
            }
        }
    return map.map { (key, value) -> value to key }.toMap()
}
