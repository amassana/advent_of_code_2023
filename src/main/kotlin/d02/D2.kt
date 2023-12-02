package d02

import d02.Cube.Companion.maxOf
import java.io.File

fun main() {
    println(firstPart())
    println(secondPart())
}

fun firstPart(): Int {
    val lines = File("files/02/input.txt").inputStream().bufferedReader().lineSequence()

    val maxCubeSet = CubeSet(Red(12), Green(13), Blue(14))

    return lines
        .map { Game(it) }
        .filter { it isPossibleWith maxCubeSet }
        .sumOf { it.id }
}

fun secondPart(): Int {
    val lines = File("files/02/input.txt").inputStream().bufferedReader().lineSequence()

    return lines
        .sumOf { Game(it).power() }
}

data class CubeSet(
    val red: Red,
    val green: Green,
    val blue: Blue,
) {
    infix fun union(that: CubeSet) =
        CubeSet(
            red = maxOf(red, that.red),
            green = maxOf(green, that.green),
            blue = maxOf(blue, that.blue),
        )

    operator fun compareTo(that: CubeSet) =
        if (red <= that.red && green <= that.green && blue <= that.blue) {
            -1
        } else {
            1
        }

    companion object {
        fun empty() = CubeSet(Red(0), Green(0), Blue(0))
    }
}

fun CubeSet(s: String): CubeSet {
    val sets = s.split(",")

    var red = 0
    var green = 0
    var blue = 0

    sets.forEach { set ->
        val elements = set.trim().split(" ")

        when (elements[1]) {
            "red" -> red = elements[0].toInt()
            "green" -> green = elements[0].toInt()
            "blue" -> blue = elements[0].toInt()
        }
    }

    return CubeSet(Red(red), Green(green), Blue(blue))
}

data class Game(
    val id: Int,
    val examples: List<CubeSet>,
) {
    infix fun isPossibleWith(maxCubeSet: CubeSet): Boolean {
        return examples
            .all { example -> example <= maxCubeSet }
    }

    private fun requiredCubes(): CubeSet {
        return examples
            .fold(CubeSet.empty()) { currentMinimum, example ->
                currentMinimum union example
            }
    }

    fun power() = requiredCubes().multiply()

    private fun CubeSet.multiply() = this.red.count * this.green.count * this.blue.count
}

fun Game(s: String): Game {
    val parts = s.split(": ")

    val id = parts[0].substring(5).toInt()
    val sets = parts[1].split(";")

    val cubeSets = sets.map { CubeSet(it) }

    return Game(id, cubeSets)
}

sealed class Cube(
    val count: Int,
) {
    operator fun plus(that: Cube): Cube {
        return UnknownType(count + that.count)
    }

    operator fun compareTo(that: Cube): Int {
        return count - that.count
    }

    companion object {
        fun <A : Cube> maxOf(
            a: A,
            b: A,
        ): A {
            if (a.count >= b.count) {
                return a
            }

            return b
        }
    }
}

class UnknownType(count: Int) : Cube(count)

class Red(count: Int) : Cube(count)

class Green(count: Int) : Cube(count)

class Blue(count: Int) : Cube(count)
