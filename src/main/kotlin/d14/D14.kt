package d14

import java.io.File

fun main() {
    println(firstPart("files/14/sample.txt"))
    println(firstPart("files/14/input.txt"))
    println(secondPart("files/14/sample.txt"))
    println(secondPart("files/14/input.txt"))
}

fun secondPart(fileName: String): Int {
    val lines = File(fileName).readLines()

    val charMatrix = transformToCharMatrix(lines)

    repeat(950) {
        spinCycle(charMatrix)
    }

    val cycleWeights = IntArray(50)

    repeat(50) {
        spinCycle(charMatrix)
        cycleWeights[it] = calcWeight(charMatrix)
    }

    val cycleLength = findCycleLength(cycleWeights)

    val position = (1_000_000_000 - 950) % cycleLength - 1

    return cycleWeights[position]
}

fun findCycleLength(list: IntArray): Int {

    for (i in (1..30)) {
        if (list[0] == list[i] && list[1] == list[i + 1] && list[2] == list[i + 2] && list[3] == list[i + 3]) {
            return i
        }
    }

    return -1
}

fun spinCycle(charMatrix: Array<CharArray>) {
    tiltNorth(charMatrix)
    tiltWest(charMatrix)
    tiltSouth(charMatrix)
    tiltEast(charMatrix)
}

fun firstPart(fileName: String): Int {
    val lines = File(fileName).readLines()

    val charMatrix = transformToCharMatrix(lines)

    tiltNorth(charMatrix)

    val weight = calcWeight(charMatrix)

    return weight
}

fun calcWeight(charMatrix: Array<CharArray>): Int {
    // only 'O' characters while be weighted. The weight can be from 1 to charMatrix.length. The weight is inverted (charMatrix[0] weights charMatrix.length and charMatrix[charMatrix.length - 1] weights 1)

    var weight = 0

    for (rowIndex in charMatrix.indices) {
        for (colIndex in charMatrix[0].indices) {
            if (charMatrix[rowIndex][colIndex] == 'O') {
                weight += charMatrix.size - rowIndex
            }
        }
    }

    return weight
}


fun tiltNorth(charMatrix: Array<CharArray>) {
// this function will move to the lowest possible row (current row - N) any character that is equal to 'O' while the row above is a '.' character
    for (colIndex in charMatrix[0].indices) {
        for (rowIndex in 1 .. charMatrix.indices.last) {
            var currentRow = rowIndex
            while (currentRow > 0 && charMatrix[currentRow][colIndex] == 'O' && charMatrix[currentRow - 1][colIndex] == '.') {
                charMatrix[currentRow][colIndex] = '.'
                charMatrix[currentRow - 1][colIndex] = 'O'
                currentRow--
            }
        }
    }
}

fun tiltSouth(charMatrix: Array<CharArray>) {
// this function will move to the highest possible row (current row + N) any character that is equal to 'O' while the row below is a '.' character
    for (colIndex in charMatrix[0].indices) {
        for (rowIndex in charMatrix.indices.reversed()) {
            var currentRow = rowIndex
            while (currentRow < charMatrix.size - 1 && charMatrix[currentRow][colIndex] == 'O' && charMatrix[currentRow + 1][colIndex] == '.') {
                charMatrix[currentRow][colIndex] = '.'
                charMatrix[currentRow + 1][colIndex] = 'O'
                currentRow++
            }
        }
    }
}

fun tiltWest(charMatrix: Array<CharArray>) {
// this function will move to the leftmost possible column (current column - N) any character that is equal to 'O' while the column to the left is a '.' character
    for (rowIndex in charMatrix.indices) {
        for (colIndex in 1 .. charMatrix[0].indices.last) {
            var currentCol = colIndex
            while (currentCol > 0 && charMatrix[rowIndex][currentCol] == 'O' && charMatrix[rowIndex][currentCol - 1] == '.') {
                charMatrix[rowIndex][currentCol] = '.'
                charMatrix[rowIndex][currentCol - 1] = 'O'
                currentCol--
            }
        }
    }
}

fun tiltEast(charMatrix: Array<CharArray>) {
// this function will move to the rightmost possible column (current column + N) any character that is equal to 'O' while the column to the right is a '.' character
    for (rowIndex in charMatrix.indices) {
        for (colIndex in charMatrix[0].indices.reversed()) {
            var currentCol = colIndex
            while (currentCol < charMatrix[0].size - 1 && charMatrix[rowIndex][currentCol] == 'O' && charMatrix[rowIndex][currentCol + 1] == '.') {
                charMatrix[rowIndex][currentCol] = '.'
                charMatrix[rowIndex][currentCol + 1] = 'O'
                currentCol++
            }
        }
    }
}

fun transformToCharMatrix(lines: List<String>): Array<CharArray> {
    val charMatrix = Array(lines.size) { CharArray(lines.first().length) }

    for (lineIndex in lines.indices) {
        val line = lines[lineIndex]
        for (charIndex in line.indices) {
            charMatrix[lineIndex][charIndex] = lines[lineIndex][charIndex]
        }
    }

    return charMatrix
}




