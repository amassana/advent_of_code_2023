package d13.approach1

import java.io.File

fun main() {
    println(firstPart("files/d13/sample.txt"))
    println(firstPart("files/d13/input.txt"))
}

fun firstPart(fileName: String): Int {
    val lines = File(fileName).inputStream().bufferedReader().lineSequence().iterator()

    var sum = 0

    while (lines.hasNext()) {
        val block = getBlock(lines)

        val vertical = verticalLinesOfReflection(block)
        val horizontal = horizontalLinesOfReflection(block)

        sum += horizontal * 100 + vertical
    }

    return sum
}

fun horizontalLinesOfReflection(block: List<String>): Int {
    return verticalLinesOfReflection(invertBlock(block))
}

fun invertBlock(block: List<String>): List<String> {
    val first = block.first()

    val inverted = mutableListOf<String>()

    for (index in first.indices) {
        val line = StringBuilder()

        for (lineIndex in block.indices) {
            line.append(block[lineIndex][index])
        }

        inverted.add(line.toString())
    }

    return inverted
}

fun verticalLinesOfReflection(block: List<String>): Int {
    val first = block.first()

    val candidates = (1..<first.length).filter { block[0].hasMirrorAt(it) }

    val lineOfReflection = candidates.filter { index ->
        block.all { it.hasMirrorAt(index) }
    }

    if (lineOfReflection.size > 1)
        throw Exception("Too many lines of reflection")

    return lineOfReflection.firstOrNull() ?: 0
}

fun String.hasMirrorAt(index: Int): Boolean {
    val pair = this.mirrorAt(index)
    val minLength = minOf(pair.first.length, pair.second.length)
    val mask = (2 shl minLength - 1) - 1
    val isIt = pair.second.intRepresentation() and mask == pair.first.intRepresentation() and mask
    return isIt
}

fun getBlock(iterator: Iterator<String>): List<String> {
    val block = mutableListOf<String>()

    while (iterator.hasNext()) {
        val line = iterator.next()

        if (line.isEmpty()) {
            break
        }

        block.add(line)
    }

    return block
}

fun String.intRepresentation(): Int {
    return Integer
        .parseInt(
            this
                .replace("#", "1")
                .replace(".", "0")
            , 2)
}

fun String.mirrorAt(index: Int): Pair<String, String> {
    return this.substring(0, index) to this.substring(index).reversed()
}
