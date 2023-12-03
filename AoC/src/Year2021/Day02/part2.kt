package Year2021.Day02

import java.io.File

/*
* - down X increases your aim by X units.
* - up X decreases your aim by X units.
* - forward X does two things:
*     1) It increases your horizontal position by X units.
*     2) It increases your depth by your aim multiplied by X.
* Calculate the horizontal position and depth you would have after
* following the planned course. What do you get if you multiply
* your final horizontal position by your final depth?
*/
fun main() {
    val testSubmarine = Submarine2()
    listOf(
        "forward 5",
        "down 5",
        "forward 8",
        "up 3",
        "down 8",
        "forward 2",
    )
        .map(::parseCommand)
        .forEach(testSubmarine::runCommand)
    assert(testSubmarine.position() == 900)

    val submarine = Submarine2()
    File("res/Year2021/Day02/input.txt")
        .readLines()
        .map(::parseCommand)
        .forEach(submarine::runCommand)
    println(submarine.position())
}

class Submarine2 : Submarine {
    private var horizontal: Int = 0
    private var vertical: Int = 0
    private var aim: Int = 0

    override fun position() = horizontal * vertical

    override fun runCommand(command: Command) {
        when (command) {
            is Command.Forward -> {
                horizontal += command.value
                vertical += aim * command.value
            }

            is Command.Down -> {
                aim += command.value
            }

            is Command.Up -> {
                aim -= command.value
            }
        }
    }
}
