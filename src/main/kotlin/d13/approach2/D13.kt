package d13.approach2

import java.io.File
/*

fun main() {
    println(firstPart("files/d13/sample.txt"))
    println(firstPart("files/d13/input.txt"))
}

fun firstPart(fileName: String): Int {
    val blocks = readBlocks(fileName)

    return blocks.sumOf {
        val vertical = verticalLinesOfReflection(it.first)
        val horizontal = verticalLinesOfReflection(it.second)

        horizontal * 100 + vertical
    }
}

class Block(
    val vertical: List<Int>,
    val verticalLength: Int,
    val horizontal: List<Int>,
    val horizontalLength: Int,
)

fun readBlocks(fileName: String): List<Block> {
    val lines = File(fileName).inputStream().bufferedReader().lineSequence().iterator()

    val blocks = mutableListOf<Block>()

    while (lines.hasNext()) {
        val blockDefinition = getBlock(lines)

        val verticalBlock = blockDefinition
            .map { line -> line.intRepresentation() }

        val horizontalBlock = invertBlock(blockDefinition)
            .map { line -> line.intRepresentation() }

        blocks.add(Block(
            vertical = verticalBlock,
            verticalLength = blockDefinition.first().length,
            horizontal = horizontalBlock,
            horizontalLength = blockDefinition.size,
        ))
    }

    return blocks
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

fun verticalLinesOfReflection(block: Block): Int {
    val first = block.vertical.first()

    val candidates = (1 ..< block.verticalLength)
        .filter { first.hasMirrorAt(it) }

    val lineOfReflection = candidates
        .filter { index -> block
            .all { it.hasMirrorAt(index) }
        }

    if (lineOfReflection.size > 1)
        throw Exception("Too many lines of reflection")

    return lineOfReflection.firstOrNull() ?: 0
}

private fun Int.hasMirrorAt(it: Int): Boolean {
    val pair = this.mirrorAt(it)
    val minLength = minOf(pair.first.length, pair.second.length)
    val mask = (2 shl minLength - 1) - 1
    val isIt = pair.second and mask == pair.first and mask
    return isIt
}

private fun Int.mirrorAt(index: Int, length: Int): Pair<Int, Int> {
    val maskHigh = (2 shl index) - 1 // 1111100
    val maskLow = -1 - maskLow

    val high = (index and maskHigh) shl
    val low = inverseBits(index and maskLow)

    return Pair(0, low)
}

*/
/* returns the integer with its bits inverted in order, that is, the highest bit becomes the lowest and vice versa *//*

fun inverseBits(i: Int): Int {
    var mask: Int = Integer.MIN_VALUE
    var result = 0

    for (index in 0..31) {
        if ((i and mask) != 0) {
            result = result or (1 shl index)
        }

        mask = mask shr 1
    }

    return result
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
*/
