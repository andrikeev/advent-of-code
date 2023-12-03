package Year2021.Day02

sealed class Command(open val value: Int) {
    data class Forward(override val value: Int) : Command(value)
    data class Down(override val value: Int) : Command(value)
    data class Up(override val value: Int) : Command(value)
}

fun parseCommand(str: String): Command {
    val (command, value) = str.split(" ")
    return when (command) {
        "forward" -> Command.Forward(value.toInt())
        "down" -> Command.Down(value.toInt())
        "up" -> Command.Up(value.toInt())
        else -> error("Wrong command $command")
    }
}

interface Submarine {
    fun runCommand(command: Command)
    fun position(): Int
}
