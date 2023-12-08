package d05

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.Duration
import java.time.Instant

fun main() {
    println(firstPart())
    println(secondPart()) // 2m30s
}

fun secondPart(): Long {
    val lines = File("files/05/input.txt").inputStream().bufferedReader().readLines()

    val seeds =
        lines[0]
            .removePrefix("seeds: ")
            .split(" ")
            .map { it.toLong() }
            .chunked(2)
            .map { Pair(it[0], it[1]) }

    val needs = read(lines)

    return Seeds(seeds, needs).lowestLocation()
}

fun firstPart(): Long {
    val lines = File("files/05/input.txt").inputStream().bufferedReader().readLines()

    val seeds =
        lines[0]
            .removePrefix("seeds: ")
            .split(" ")
            .map { Pair(it.toLong(), 1L) }

    val needs = read(lines)

    return Seeds(seeds, needs).lowestLocation()
}

fun read(lines: List<String>): List<SeedNeeds> {
    val needs = mutableListOf<SeedNeeds>()

    val iterator = lines.drop(2).iterator()

    while (iterator.hasNext()) {
        val seedNeed = SeedNeeds(iterator)
        needs.add(seedNeed)
    }

    return needs
}

class Seeds(
    private val seeds: List<Pair<Long, Long>>,
    private val needs: List<SeedNeeds>,
) {
    fun lowestLocation(): Long {
        var min: Long = Long.MAX_VALUE

        val from = Instant.now()

        runBlocking(Dispatchers.Default) {
            val all =
                seeds.map {
                    async {
                        var min2 = Long.MAX_VALUE
                        for (i: Long in it.first..<it.first + it.second)
                            min2 = min2.coerceAtMost(findLocation(i))
                        min2
                    }
                }.awaitAll()

            min = all.min()
        }

        val to = Instant.now()

        println(Duration.between(from, to))

        return min
    }

    private fun findLocation(seed: Long): Long {
        var current = seed
        needs.forEach { current = it.mapSeed(current) }
        return current
    }
}

class SeedNeeds(
    val mapName: String,
    val map: List<SeedMap>,
) {
    fun mapSeed(source: Long): Long {
        return map
            .find { it.isInRange(source) }
            ?.map(source)
            ?: source
    }
}

fun SeedNeeds(iterator: Iterator<String>): SeedNeeds {
    val mapName = iterator.next()
    val list = mutableListOf<SeedMap>()

    while (iterator.hasNext()) {
        val current = iterator.next()

        if (current == "") {
            break
        }

        list.add(SeedMap(current))
    }

    return SeedNeeds(mapName, list)
}

class SeedMap(
    val destination: Long,
    val source: Long,
    val range: Long,
) {
    fun isInRange(value: Long) = value in (source..<source + range)

    fun map(value: Long) = destination + value - source
}

fun SeedMap(s: String): SeedMap {
    val (destination, source, range) = s.split(" ").map { it.toLong() }

    return SeedMap(destination, source, range)
}
