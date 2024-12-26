package Year2021

import Day
import Year2021.Day23.Room.Companion.Room
import kotlin.math.abs

private object Day23 : Day {
    enum class Amphipod(
        val room: Int,
        val energy: Int
    ) {
        A(0, 1), B(1, 10), C(2, 100), D(3, 1000),
    }

    data class Hallway(val places: List<Amphipod?>)

    sealed interface Room {
        val places: List<Amphipod?>
        val index: Int

        companion object {
            fun Room(index: Int, places: List<Amphipod?>): Room {
                return when (index) {
                    0 -> RoomA(places)
                    1 -> RoomB(places)
                    2 -> RoomC(places)
                    3 -> RoomD(places)
                    else -> error("Unknown room index: $index")
                }
            }
        }
    }

    data class RoomA(
        override val places: List<Amphipod?>,
        override val index: Int = 2,
    ) : Room

    data class RoomB(
        override val places: List<Amphipod?>,
        override val index: Int = 4,
    ) : Room

    data class RoomC(
        override val places: List<Amphipod?>,
        override val index: Int = 6,
    ) : Room

    data class RoomD(
        override val places: List<Amphipod?>,
        override val index: Int = 8,
    ) : Room

    data class State(
        val hallway: Hallway = Hallway(List(11) { null }),
        val rooms: List<Room>,
    ) {
        override fun toString(): String {
            return buildString {
                append(hallway.places.joinToString("", prefix = "[", postfix = "]") { it?.name ?: "." })
                append("\n")
                for (i in rooms.first().places.indices) {
                    append("   ")
                    append(rooms.map { it.places[i] }.joinToString(" ") { it?.name ?: "." })
                    append("   ")
                    append("\n")
                }
            }
        }
    }

    private fun find(state: State, finalState: State): Int {
        fun Hallway.hasPath(from: Int, to: Int): Boolean {
            return this.places.slice(minOf(from, to)..maxOf(from, to)).all { it == null }
        }

        fun Room.availableFor(amphipod: Amphipod): Boolean {
            return when (this) {
                is RoomA -> amphipod == Amphipod.A
                is RoomB -> amphipod == Amphipod.B
                is RoomC -> amphipod == Amphipod.C
                is RoomD -> amphipod == Amphipod.D
            } && this.places.all { it == null || it == amphipod }
        }

        fun Room.emptyPlaceIndex(): Int {
            return this.places.indexOfLast { it == null }
        }

        var minEnergy = Int.MAX_VALUE
        fun find(state: State, energy: Int): Int {
            return if (state == finalState) {
                if (energy < minEnergy) {
                    minEnergy = energy
                    println(minEnergy)
                }
                energy
            } else {
                buildList<Pair<State, Int>> {
                    val (hallway, rooms) = state
                    rooms
                        .withIndex()
                        .filter { (index, room) -> room.places.any { it != null && it.room != index } }
                        .forEach { (index, room) ->
                            val roomEntryIndex = 2 + 2 * index
                            val amphipodIndex = room.places.indexOfFirst { it != null }
                            val amphipod = room.places[amphipodIndex]!!
                            val targetRoomIndex = amphipod.room
                            val targetRoomEntryIndex = 2 + 2 * targetRoomIndex
                            val targetRoom = rooms[targetRoomIndex]
                            if (
                                hallway.hasPath(roomEntryIndex, targetRoomEntryIndex) &&
                                targetRoom.availableFor(amphipod)
                            ) {
                                val emptyPlaceIndex = targetRoom.emptyPlaceIndex()
                                val distance = abs(roomEntryIndex - targetRoomEntryIndex) +
                                    amphipodIndex + 1 + emptyPlaceIndex + 1
                                add(
                                    state.copy(
                                        rooms = state.rooms.toMutableList().apply {
                                            this[index] = Room(
                                                index = index,
                                                places = this[index].places.toMutableList().apply {
                                                    this[amphipodIndex] = null
                                                },
                                            )
                                            this[targetRoomIndex] = Room(
                                                index = targetRoomIndex,
                                                places = this[targetRoomIndex].places.toMutableList().apply {
                                                    this[emptyPlaceIndex] = amphipod
                                                },
                                            )
                                        },
                                    ) to energy + amphipod.energy * distance,
                                )
                            } else {
                                hallway.places
                                    .asSequence()
                                    .withIndex()
                                    .filter { (index) -> index in listOf(0, 1, 3, 5, 7, 9, 10) }
                                    .filter { (_, place) -> place == null }
                                    .map(IndexedValue<Amphipod?>::index)
                                    .filter { targetIndex -> hallway.hasPath(targetIndex, roomEntryIndex) }
                                    .forEach { targetIndex ->
                                        val distance = abs(targetIndex - roomEntryIndex) + amphipodIndex + 1
                                        add(
                                            state.copy(
                                                hallway = state.hallway.copy(
                                                    places = state.hallway.places.toMutableList().apply {
                                                        this[targetIndex] = amphipod
                                                    },
                                                ),
                                                rooms = state.rooms.toMutableList().apply {
                                                    this[index] = Room(
                                                        index = index,
                                                        places = this[index].places.toMutableList().apply {
                                                            this[amphipodIndex] = null
                                                        },
                                                    )
                                                },
                                            ) to energy + amphipod.energy * distance,
                                        )
                                    }
                            }
                        }
                    hallway.places
                        .withIndex()
                        .filter { it.value != null }
                        .map { it.index to it.value!! }
                        .forEach { (amphipodIndex, amphipod) ->
                            rooms
                                .withIndex()
                                .map { Triple(it.value, it.index, 2 + 2 * it.index) }
                                .filter { (room, _, roomEntryIndex) ->
                                    room.availableFor(amphipod) &&
                                        if (roomEntryIndex > amphipodIndex) {
                                            hallway.hasPath(amphipodIndex + 1, roomEntryIndex)
                                        } else {
                                            hallway.hasPath(roomEntryIndex, amphipodIndex - 1)
                                        }
                                }
                                .forEach { (room, roomIndex, roomEntryIndex) ->
                                    val emptyPlaceIndex = room.emptyPlaceIndex()
                                    val distance = abs(roomEntryIndex - amphipodIndex) + emptyPlaceIndex + 1
                                    add(
                                        state.copy(
                                            hallway = state.hallway.copy(
                                                places = state.hallway.places.toMutableList().apply {
                                                    this[amphipodIndex] = null
                                                },
                                            ),
                                            rooms = state.rooms.toMutableList().apply {
                                                this[roomIndex] = Room(
                                                    index = roomIndex,
                                                    places = this[roomIndex].places.toMutableList().apply {
                                                        this[emptyPlaceIndex] = amphipod
                                                    },
                                                )
                                            }
                                        ) to energy + amphipod.energy * distance,
                                    )
                                }
                        }
                }
                    .minOfOrNull { (state, energy) -> find(state, energy) }
                    ?: Int.MAX_VALUE
            }
        }

        return find(state, 0)
    }

    override fun part1(input: List<String>): Any {
        val (top, bottom) = input.drop(2)
        val state = State(
            rooms = buildList {
                val (t0, t1, t2, t3) = top.substring(3, 10).split('#').map(Amphipod::valueOf)
                val (b0, b1, b2, b3) = bottom.substring(3, 10).split('#').map(Amphipod::valueOf)
                add(RoomA(listOf(t0, b0)))
                add(RoomB(listOf(t1, b1)))
                add(RoomC(listOf(t2, b2)))
                add(RoomD(listOf(t3, b3)))
            },
        )
        val finalState = State(
            rooms = listOf(
                RoomA(List(2) { Amphipod.A }),
                RoomB(List(2) { Amphipod.B }),
                RoomC(List(2) { Amphipod.C }),
                RoomD(List(2) { Amphipod.D }),
            ),
        )

        return find(state, finalState)
    }

    override fun part2(input: List<String>): Any {
        val (top, bottom) = input.drop(2)
        val state = State(
            rooms = buildList {
                val (t0, t1, t2, t3) = top.substring(3, 10).split('#').map(Amphipod::valueOf)
                val (mt0, mt1, mt2, mt3) = "D#C#B#A".split('#').map(Amphipod::valueOf)
                val (mb0, mb1, mb2, mb3) = "D#B#A#C".split('#').map(Amphipod::valueOf)
                val (b0, b1, b2, b3) = bottom.substring(3, 10).split('#').map(Amphipod::valueOf)
                add(RoomA(listOf(t0, mt0, mb0, b0)))
                add(RoomB(listOf(t1, mt1, mb1, b1)))
                add(RoomC(listOf(t2, mt2, mb2, b2)))
                add(RoomD(listOf(t3, mt3, mb3, b3)))
            },
        )
        val finalState = State(
            rooms = listOf(
                RoomA(List(4) { Amphipod.A }),
                RoomB(List(4) { Amphipod.B }),
                RoomC(List(4) { Amphipod.C }),
                RoomD(List(4) { Amphipod.D }),
            ),
        )

        return find(state, finalState)
    }
}

fun main() = with(Day23) {
    test1(
        input = """
            #############
            #...........#
            ###B#C#B#D###
              #A#D#C#A#
              #########
        """.trimIndent(),
        expected = 12521,
    )
    result1()
    test2(
        input = """
            #############
            #...........#
            ###B#C#B#D###
              #A#D#C#A#
              #########
        """.trimIndent(),
        expected = 44169,
    )
    result2()
}
