package d07

data class Hand(
    val hand: String,
    val bid: Int,
) : Comparable<Hand> {
    private val handInternal = hand.map { Card(it) }

    val type: HandType = handTypeEvaluator.evaluate(hand)

    override fun compareTo(other: Hand): Int {
        if (this.type != other.type) {
            return this.type.compareTo(other.type)
        }

        return this.handInternal
            .asSequence()
            .zip(other.handInternal.asSequence())
            .map { it.first.compareTo(it.second) }
            .firstOrNull { it != 0 }
            ?: 0
    }

    override fun toString() = "hand=$hand type=$type"

    companion object {
        private lateinit var handTypeEvaluator: HandTypeEvaluator

        fun onGameTypeChanged(newGameType: GameType) =
            when (newGameType) {
                GameType.STANDARD -> handTypeEvaluator = StandardHandTypeEvaluator
                GameType.JOKER_WILDCARD -> handTypeEvaluator = JokerHandTypeEvaluator
            }
    }
}

interface HandTypeEvaluator {
    fun evaluate(hand: String): HandType
}

object StandardHandTypeEvaluator : HandTypeEvaluator {
    override fun evaluate(hand: String): HandType {
        val x =
            hand
                .groupBy { it }
                .toList()
                .sortedByDescending { it.second.size }

        return when (x[0].second.size) {
            5 -> HandType.FIVE_OF_A_KIND // aaaaa
            4 -> HandType.FOUR_OF_A_KIND // aaaab
            3 -> {
                if (x.size == 2) {
                    HandType.FULL_HOUSE // aaabb
                } else {
                    HandType.THREE_OF_A_KIND // aaabc
                }
            }
            2 -> {
                if (x.size == 3) {
                    HandType.TWO_PAIR // aabbc
                } else {
                    HandType.ONE_PAIR // aabcd
                }
            }
            else -> HandType.HIGH_CARD // abcd
        }
    }
}

object JokerHandTypeEvaluator : HandTypeEvaluator {
    override fun evaluate(hand: String): HandType {
        if (!hand.contains("J")) {
            return StandardHandTypeEvaluator.evaluate(hand)
        } else {
            val x =
                hand
                    .replace("J", "")
                    .groupBy { it }
                    .toList()
                    .sortedByDescending { it.second.size }

            return when (x.size) {
                0 -> HandType.FIVE_OF_A_KIND // JJJJJ
                1 -> HandType.FIVE_OF_A_KIND // aJJJJ
                2 -> {
                    if (x[1].second.size == 2) { // aaJbb
                        HandType.FULL_HOUSE
                    } else {
                        HandType.FOUR_OF_A_KIND // aJJJb, aaJJb, aaaJb
                    }
                }

                3 -> HandType.THREE_OF_A_KIND // aaJbc, aJJbc
                4 -> HandType.ONE_PAIR // aJbcd
                else -> throw Exception("We should not be here")
            }
        }
    }
}

enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
}

fun Hand(s: String): Hand {
    return Hand(
        hand = s.substring(0, 5),
        bid = s.substring(6).toInt(),
    )
}
