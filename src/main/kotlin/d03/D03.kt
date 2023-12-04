package d03

import all.Coordinate
import java.io.File

fun main() {
    println(firstPart())
    println(secondPart())
}

fun firstPart(): Int {
    val table = File("files/03/input.txt").inputStream().bufferedReader().readLines()

    val iterator = NumberOccurrenceIterator(table)
    var sum = 0

    iterator.forEach { occurrence ->
        val surroundingCoordinates = occurrence.surroundingCoordinates(table)
        if (table containsSymbolIn surroundingCoordinates) {
            sum += occurrence.number.toInt()
        }
    }

    return sum
}

fun secondPart(): Int {
    val table = File("files/03/input.txt").inputStream().bufferedReader().readLines()

    val iterator = NumberOccurrenceIterator(table)

    val stars: MutableMap<Coordinate, MutableList<Occurrence>> = mutableMapOf()

    iterator.forEach { occurrence ->
        val surroundingCoordinates = occurrence.surroundingCoordinates(table)
        table.listSymbolsIn(surroundingCoordinates)
            .filter { it.first == '*' }
            .forEach { symbol ->
                if (stars[symbol.second] == null) {
                    stars[symbol.second] = mutableListOf(occurrence)
                } else {
                    stars[symbol.second]?.add(occurrence)
                }
            }
    }

    return stars
        .values
        .filter { it.size == 2 }
        .sumOf { it[0].number.toInt() * it[1].number.toInt() }
}

private infix fun List<String>.containsSymbolIn(coordinates: List<Coordinate>): Boolean {
    return coordinates.any { coordinate ->
        return@any this[coordinate.first][coordinate.second] != '.' && !this[coordinate.first][coordinate.second].isDigit()
    }
}

private fun List<String>.listSymbolsIn(coordinates: List<Coordinate>): List<Pair<Char, Coordinate>> {
    return coordinates
        .filter { this[it.first][it.second] != '.' && !this[it.first][it.second].isDigit() }
        .map { Pair(this[it.first][it.second], it) }
}

class NumberOccurrenceIterator(
    private val table: List<String>,
) : Iterator<Occurrence> {
    private val maxRow: Int = table.size - 1
    private val maxColumn: Int = table[0].length - 1
    private val digitCount: Int = table.sumOf { it.count { it.isDigit() } }
    private var currentRow = 0
    private var currentColumn = -1
    private var currentDigitCount = 0

    override fun hasNext() = currentDigitCount < digitCount

    override fun next(): Occurrence {
        // advance until the pointer points to a digit
        do {
            advancePointer()
        } while (pointerInBounds() && !pointsToDigit())

        val startPosition = Pair(currentRow, currentColumn)
        var number = ""

        // accumulate digits
        var rowOverflow = false
        while (pointerInBounds() && pointsToDigit() && !rowOverflow) {
            number += currentDigit()
            rowOverflow = advancePointer()
        }

        currentDigitCount += number.length

        // the pointer now points either to a non-digit or beyond the bounds

        return Occurrence(startPosition, number)
    }

    private fun currentDigit() = table[currentRow][currentColumn]

    private fun pointsToDigit() = table[currentRow][currentColumn].isDigit()

    private fun pointerInBounds() = currentRow <= maxRow && currentColumn <= maxColumn

    private fun advancePointer(): Boolean {
        if (currentColumn < maxColumn) {
            currentColumn++
            return false
        } else {
            currentColumn = 0
            currentRow++
            return true
        }
    }
}

data class Occurrence(val at: Coordinate, val number: String) {
    fun surroundingCoordinates(table: List<String>): List<Coordinate> {
        return (at.second - 1..at.second + number.length).map {
            Pair(at.first - 1, it)
        }.union(
            (at.second - 1..at.second + number.length).map {
                Pair(at.first + 1, it)
            },
        ).union(
            listOf(
                Pair(at.first, at.second - 1),
                Pair(at.first, at.second + number.length),
            ),
        )
            .filter {
                it.first >= 0 &&
                    it.first < table.size &&
                    it.second >= 0 &&
                    it.second < table[0].length
            }.toList()
    }
}
