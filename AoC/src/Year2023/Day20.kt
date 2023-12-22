package Year2023

import lcm
import readInput
import testInput

fun main() {

    fun modules(input: List<String>): Map<String, Module> {
        return buildMap {
            input.forEach { line ->
                val (module, connections) = line.split(" -> ")
                val c = connections.split(", ")
                when {
                    module.startsWith('%') -> FlipFlop(
                        name = module.drop(1),
                        connections = c,
                    )

                    module.startsWith('&') -> Conjunction(
                        name = module.drop(1),
                        memory = mutableMapOf(),
                        connections = c,
                    )

                    module == "broadcaster" -> Broadcaster(
                        name = module,
                        connections = c,
                    )

                    else -> null
                }?.let {
                    this[it.name] = it
                }
            }
            this["rx"] = Rx
            values.filterIsInstance<Conjunction>().forEach { conj ->
                val name = conj.name
                val inputs = values.filter { it.connections.contains(name) }.map { it.name }
                conj.memory = inputs.associateWith { Pulse.Low }.toMutableMap()
                this[name] = conj
            }
        }
    }

    fun press(
        modules: Map<String, Module>,
        onHighPulse: (String) -> Unit = {},
    ): Pair<Int, Int> {
        var high = 0
        var low = 0

        val toProcess = mutableListOf(State(modules.getValue("broadcaster"), Pulse.Low))
        while (toProcess.isNotEmpty()) {
            val (module, pulse, source) = toProcess.removeFirst()
            when (pulse) {
                Pulse.High -> high++
                Pulse.Low -> low++
            }
            when (module) {
                is Broadcaster -> {
                    module.connections.map { output ->
                        State(modules.getValue(output), Pulse.Low, module.name)
                    }.let(toProcess::addAll)
                }

                is Conjunction -> {
                    module.memory[source] = pulse
                    val allHigh = module.memory.values.all { p -> p == Pulse.High }
                    val p = if (allHigh) Pulse.Low else Pulse.High
                    if (p == Pulse.High) {
                        onHighPulse(module.name)
                    }
                    module.connections.map { output ->
                        State(modules.getValue(output), p, module.name)
                    }.let(toProcess::addAll)
                }

                is FlipFlop -> {
                    if (pulse == Pulse.Low) {
                        module.on = !module.on
                        val p = if (module.on) Pulse.High else Pulse.Low
                        module.connections.map { output ->
                            State(modules.getValue(output), p, module.name)
                        }.let(toProcess::addAll)
                    }
                }

                is Rx -> Unit
            }
        }

        return high to low
    }

    fun part1(input: List<String>): Long {
        val modules = modules(input)
        var high = 0L
        var low = 0L
        repeat(1000) {
            val (h, l) = press(modules)
            high += h
            low += l
        }
        return high * low
    }

    fun part2(input: List<String>): Long {
        val modules = modules(input)

        val rxInput = modules.values.single { Rx.name in it.connections }
        val rxInputInputs = modules
            .filter { rxInput.name in it.value.connections }
            .map { it.key to 0 }
            .toMap()
            .toMutableMap()

        var buttonPresses = 0
        while (rxInputInputs.any { (_, value) -> value == 0 }) {
            buttonPresses++
            press(modules) { name ->
                if (name in rxInputInputs) {
                    rxInputInputs[name] = buttonPresses
                }
            }
        }
        return lcm(rxInputInputs.values.map(Int::toLong))
    }

    val testInput1 = testInput("""
        broadcaster -> a, b, c
        %a -> b
        %b -> c
        %c -> inv
        &inv -> a
    """)
    check(part1(testInput1).also { println("part1 test: $it") } == 32000000L)

    val input = readInput("Year2023/Day20")
    println(part1(input))
    println(part2(input))
}

private enum class Pulse { High, Low }

private sealed interface Module {
    val name: String
    val connections: List<String>
}

private data class Broadcaster(
    override val name: String,
    override val connections: List<String>,
) : Module

private data class FlipFlop(
    override val name: String,
    var on: Boolean = false,
    override val connections: List<String>
) : Module

private data class Conjunction(
    override val name: String,
    var memory: MutableMap<String, Pulse>,
    override val connections: List<String>,
) : Module

private data object Rx : Module {
    override val name = "rx"
    override val connections = emptyList<String>()
}

private data class State(val module: Module, val pulse: Pulse, val source: String = "button")