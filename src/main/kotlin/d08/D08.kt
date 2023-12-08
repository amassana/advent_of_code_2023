package d08

import java.io.File

fun main() {
    println(execute("files/08/sample1.txt").stepsUntilTerminalFrom("AAA"))
    println(execute("files/08/sample2.txt").stepsUntilTerminalFrom("AAA"))
    println(execute("files/08/input.txt").stepsUntilTerminalFrom("AAA"))
    println(execute("files/08/sample3.txt").ghostSteps())
    println(execute("files/08/input.txt").ghostSteps())
}

fun execute(fileName: String): Network {
    val lines = File(fileName).inputStream().bufferedReader().readLines()

    val instructions = lines[0]

    return Network(instructions, lines.drop(2))
}
