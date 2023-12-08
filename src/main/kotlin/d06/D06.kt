package d06

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.time.measureTime

fun main() {
    println(firstPartExample())
    println(firstPartActual())
    println(Race(71530, 940200).numWaysToWin())
    println(Race(47986698, 400121310111540).numWaysToWin())

    println(
        measureTime {
            repeat(50) {
                Race(47986698, 400121310111540).numWaysToWin()
            }
        },
    )
}

fun firstPartActual(): Any {
    return listOf(
        Race(47, 400),
        Race(98, 1213),
        Race(66, 1011),
        Race(98, 1540),
    )
        .map { it.numWaysToWin() }
        .product()
}

fun firstPartExample(): Any {
    return listOf(
        Race(7, 9),
        Race(15, 40),
        Race(30, 200),
    )
        .map { it.numWaysToWin() }
        .product()
}

private fun List<Int>.product(): Int = this.fold(1, Int::times)

data class Race(
    private val time: Time,
    private val record: Distance,
) {
    fun numWaysToWin() = numWaysToWin0()

    private fun numWaysToWin0(): Int {
        val sqrt = sqrt(time * time - 4 * record.toDouble())
        val floor = floor((time + sqrt) / 2).toInt()
        val ceil = ceil((time - sqrt) / 2).toInt()
        // discard 2 solutions when they are equal to the record we must beat
        if (sqrt - sqrt.toInt() == 0.toDouble()) {
            return floor - ceil + 1 - 2
        }
        return floor - ceil + 1
    }

    private fun numWaysToWin1(): Int {
        val chargeTimes = (1..<time)
        return chargeTimes.count { distanceWhenCharged(it) > record }
    }

    private fun numWaysToWin2(): Int {
        val chargeTimes = (1..<time)

        val shortestCharge = chargeTimes.indexOfFirst { distanceWhenCharged(it) > record }
        val longestCharge = chargeTimes.indexOfLast { distanceWhenCharged(it) > record }

        return longestCharge - shortestCharge + 1
    }

    private fun distanceWhenCharged(chargeTime: Time) = (time - chargeTime) * chargeTime
}

typealias Time = Long
typealias Distance = Long
