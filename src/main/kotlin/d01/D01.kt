package d01

import java.io.File
import java.time.Duration
import java.time.Instant

fun main2() {
    println(secondPartVersion1())
}

fun main() {
    val a = Instant.now()
    repeat(10000) {
        secondPartVersion1()
    }
    val b = Instant.now()
    println(Duration.between(a, b))
}

fun firstPart(): Int {
    val lines = File("files/01/input.txt").inputStream().bufferedReader().lineSequence()

    val result =
        lines.sumOf {
            val leftDigit = findLeftDigit(it)
            val rightDigit = findRightDigit(it)

            "$leftDigit$rightDigit".toInt()
        }

    return result
}

fun secondPartVersion3(): Int {
    val lines = File("files/01/input.txt").inputStream().bufferedReader().lineSequence()

    val result =
        lines
            .sumOf { line ->
                val leftDigit = findLeftDigitOrNumber(line)
                val rightDigit = findRightDigitOrNumber(line)

                "$leftDigit$rightDigit".toInt()
            }

    return result
}

fun secondPartVersion2(): Int {
    val lines = File("files/01/input.txt").inputStream().bufferedReader().lineSequence()

    val result =
        lines
            .sumOf { line ->
                val leftLine = replaceLeftmostNumberByDigit(line)
                val leftDigit = findLeftDigit(leftLine)

                val rightLine = replaceRightmostNumberByDigit(line)
                val rightDigit = findRightDigitOrNumber(rightLine)

                "$leftDigit$rightDigit".toInt()
            }

    return result
}

fun secondPartVersion1(): Int {
    val lines = File("files/01/input.txt").inputStream().bufferedReader().lineSequence()

    val result =
        lines
            .sumOf { line ->
                val leftLine = substituteLeftmostNumberInitial(line)
                val leftDigit = findLeftDigit(leftLine)

                val rightLine = substituteRightmostNumberInitial(line)
                val rightDigit = findRightDigitOrNumber(rightLine)

                "$leftDigit$rightDigit".toInt()
            }

    return result
}

fun findLeftDigit(s: String) = s.first { it.isDigit() }

fun findRightDigit(s: String) = s.last { it.isDigit() }

val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

fun replaceNumberByDigit(
    line: String,
    number: String,
) = when (number) {
    "one" -> line.replace(number, "1")
    "two" -> line.replace(number, "2")
    "three" -> line.replace(number, "3")
    "four" -> line.replace(number, "4")
    "five" -> line.replace(number, "5")
    "six" -> line.replace(number, "6")
    "seven" -> line.replace(number, "7")
    "eight" -> line.replace(number, "8")
    "nine" -> line.replace(number, "9")
    else -> throw Exception(number)
}

fun findLeftDigitOrNumber(line: String): Char {
    val digit = findLeftDigit(line)
    val digitIndex = line.indexOf(digit)

    // Pair of <index in the line, index in the number list>
    val leftmost =
        numbers
            .foldIndexed(initiallyWeHaveNothing2) { indexOfNumber, current, number ->

                val indexInLine = line.indexOf(number)

                return@foldIndexed if (indexInLine == -1) {
                    current
                } else if (current.index == -1) {
                    Pair(indexInLine, indexOfNumber)
                } else if (indexInLine < current.index) {
                    Pair(indexInLine, indexOfNumber)
                } else {
                    current
                }
            }

    if (leftmost.isNotFound()) {
        return digit
    }

    if (digitIndex < leftmost.index) {
        return digit
    }

    return (leftmost.number + 1).digitToChar()
}

fun findRightDigitOrNumber(line: String): Char {
    val digit = findRightDigit(line)

    // Pair of <index in the line, index in the number list>
    val rightmost =
        numbers
            .foldIndexed(initiallyWeHaveNothing2) { indexOfNumber, current, number ->

                val indexInLine = line.lastIndexOf(number)

                return@foldIndexed if (indexInLine == -1) {
                    current
                } else if (current.index == -1) {
                    Pair(indexInLine, indexOfNumber)
                } else if (indexInLine > current.index) {
                    Pair(indexInLine, indexOfNumber)
                } else {
                    current
                }
            }

    if (rightmost.isNotFound()) {
        return digit
    }

    val digitIndex = line.lastIndexOf(digit)

    if (digitIndex > rightmost.index) {
        return digit
    }

    return (rightmost.number + 1).digitToChar()
}

fun replaceLeftmostNumberByDigit(line: String): String {
    // Pair of <index in the line, "number">
    val leftmost =
        numbers
            .fold(initiallyWeHaveNothing) { current, number ->

                val index = line.indexOf(number)

                return@fold if (index == -1) {
                    current
                } else if (current.index == -1) {
                    Pair(index, number)
                } else if (index < current.index) {
                    Pair(index, number)
                } else {
                    current
                }
            }

    if (leftmost.isNotFound()) {
        return line
    }

    return replaceNumberByDigit(line, leftmost.number)
}

fun replaceRightmostNumberByDigit(line: String): String {
    // Pair of <index in the line, "number">
    val rightmost =
        numbers
            .fold(initiallyWeHaveNothing) { current, number ->

                val index = line.lastIndexOf(number)

                return@fold if (index == -1) {
                    current
                } else if (current.index == -1) {
                    Pair(index, number)
                } else if (index > current.index) {
                    Pair(index, number)
                } else {
                    current
                }
            }

    if (rightmost.isNotFound()) {
        return line
    }

    return replaceNumberByDigit(line, rightmost.number)
}

private fun <A, B> Pair<A, B>.isNotFound() = this.first == -1

private val <A, B> Pair<A, B>.number: B
    get() = this.second

private val <A, B> Pair<A, B>.index: A
    get() = this.first

val initiallyWeHaveNothing = Pair(-1, "")

val initiallyWeHaveNothing2 = Pair(-1, 0)

fun substituteLeftmostNumberInitial(s: String): String {
    val index =
        numbers
            .map { s.indexOf(it) }
            .map {
                if (it == -1) {
                    Int.MAX_VALUE
                } else {
                    it
                }
            }
            .withIndex()
            .minBy { it.value }
            .index

    return replaceNumberByDigit(s, numbers[index])
}

fun substituteRightmostNumberInitial(s: String): String {
    val index =
        numbers
            .map { s.lastIndexOf(it) }
            .withIndex()
            .maxBy { it.value }
            .index

    return replaceNumberByDigit(s, numbers[index])
}
