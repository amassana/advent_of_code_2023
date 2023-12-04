package d04

import java.io.File

fun main() {
    println(firstPart())
    println(secondPart())
}

fun firstPart(): Int {
    val lines = File("files/04/input.txt").inputStream().bufferedReader().lineSequence()

    return lines
        .sumOf { Card(it).value() }
}

fun secondPart(): Int {
    val lines = File("files/04/input.txt").inputStream().bufferedReader().readLines()

    val map = Array(lines.size) { 1 }

    lines.forEachIndexed { index, line ->
        (1..Card(line).numMatches()).forEach {
            map[index + it] += map[index]
        }
    }

    return map.sumOf { it }
}

fun Card(s: String): Card {
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53

    val (_, winningUnprocessed, ownedUnprocessed) = s.split(":", " |")

    return Card(
        winning =
            winningUnprocessed
                .chunked(3)
                .map { it.trim().toInt() }
                .toSet(),
        owned =
            ownedUnprocessed
                .chunked(3)
                .map { it.trim().toInt() }
                .toSet(),
    )
}

data class Card(
    val winning: Set<Int>,
    val owned: Set<Int>,
) {
    fun value(): Int {
        val matches = matches()

        if (matches.isEmpty()) {
            return 0
        }

        return 1 shl (matches.size - 1)
    }

    private fun matches() = winning.intersect(owned)

    fun numMatches() = matches().size
}
