package d15

import java.io.File

fun main() {
    println(firstPart("files/15/sample.txt"))
    println(firstPart("files/15/input.txt"))
    println(secondPart("files/15/sample.txt"))
    println(secondPart("files/15/input.txt"))
}

fun firstPart(fileName: String): Int =
    File(fileName).readText()
        .splitToSequence(",")
        .sumOf { hash(it) }

fun hash(s: String): Int =
    s.fold(0) { acc, char ->
        ((acc + char.code) * 17 ) % 256
    }

fun secondPart(fileName: String): Int {
    val boxes = Array(256) { Box(it.toShort()) }

    File(fileName).readText()
        .splitToSequence(",")
        .forEach { step ->
            if (step.last() == '-') {
                val label = step.substring(0, step.lastIndex)
                val box = hash(label)
                boxes[box].remove(label)
            }
            else {
                val (label, focalLength) = step.split("=")
                val box = hash(label)
                boxes[box].put(label, focalLength.toInt())
            }
        }

    return boxes
        .sumOf { it.focusingPower() }
}

data class Lens(
    val label: String,
    var focalLength: Int,
)

data class Box(
    private val id: Short,
    private val lenses: MutableList<Lens> = mutableListOf(),
) {
    fun remove(label: String) {
        lenses.removeIf { it.label == label }
    }

    fun put(label: String, focalLength: Int) {
        val lens = lenses.firstOrNull { it.label == label }

        if (lens == null) {
            lenses.add(Lens(label, focalLength))
        } else {
            lens.focalLength = focalLength
        }
    }

    fun focusingPower(): Int {
        return lenses
            .mapIndexed { index, lens ->
                (id + 1) * (index + 1) * lens.focalLength
            }.sum()
    }
}
