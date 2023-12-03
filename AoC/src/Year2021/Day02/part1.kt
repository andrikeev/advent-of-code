package Year2021.Day02

import java.io.File

/*
* - forward X increases the horizontal position by X units.
* - down X increases the depth by X units.
* - up X decreases the depth by X units.
* Calculate the horizontal position and depth you would have after
* following the planned course. What do you get if you multiply
* your final horizontal position by your final depth?
*/
fun main() {
    val testSubmarine = Submarine1()
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
    assert(testSubmarine.position() == 150)

    val submarine = Submarine1()
    File("res/Year2021/Day02/input.txt")
        .readLines()
        .map(::parseCommand)
        .forEach(submarine::runCommand)
    println(submarine.position())
}

class Submarine1 : Submarine {
    private var horizontal: Int = 0
    private var vertical: Int = 0

    override fun position() = horizontal * vertical

    override fun runCommand(command: Command) {
        when (command) {
            is Command.Forward -> {
                horizontal += command.value
            }

            is Command.Down -> {
                vertical += command.value
            }

            is Command.Up -> {
                vertical -= command.value
            }
        }
    }
}
