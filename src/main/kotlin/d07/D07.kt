package d07

import java.io.File

fun main() {
    Game.observeGameType(Hand::onGameTypeChanged)
    Game.observeGameType(Card::onGameTypeChanged)

    Game.gameType = GameType.STANDARD

    assertionsStandard()
    println(firstPart("files/07/sample.txt"))
    println(firstPart("files/07/input.txt"))

    Game.gameType = GameType.JOKER_WILDCARD

    assertionsJoker()
    println(secondPart("files/07/sample.txt"))
    println(secondPart("files/07/input.txt"))
}

fun assertionsStandard() {
    check(Card('K') > Card('T'))
    check(HandType.FULL_HOUSE > HandType.HIGH_CARD)
    check(Hand("AAAAJ 1") > Hand("J2222 1"))
    check(Hand("33332 1") > Hand("2AAAA 1"))
    check(Hand("77888 1") > Hand("77788 1"))
    check(Hand("KK677 1") > Hand("KTJJT 1"))
    check(Hand("QQQJA 1") > Hand("T55J5 1"))
}

fun firstPart(fileName: String): Int {
    val lines = File(fileName).inputStream().bufferedReader().readLines()

    val totalWinnings =
        lines
            .map { Hand(it) }
            .sorted()
            .mapIndexed { rank, hand -> (rank + 1) * hand.bid }
            .sum()

    return totalWinnings
}

fun assertionsJoker() {
    check(Card('2') > Card('J'))
    check(HandType.FULL_HOUSE > HandType.HIGH_CARD)
    check(Hand("JJJJJ 1").type == Hand("J2222 1").type)
    check(Hand("QJJQ2 1").type == HandType.FOUR_OF_A_KIND)
    check(Hand("32T3K 1").type == HandType.ONE_PAIR)
    check(Hand("T55J5 1").type == HandType.FOUR_OF_A_KIND)
    check(Hand("KTJJT 1").type == HandType.FOUR_OF_A_KIND)
    check(Hand("QQQJA 1").type == HandType.FOUR_OF_A_KIND)
}

fun secondPart(fileName: String): Long {
    val lines = File(fileName).inputStream().bufferedReader().lineSequence()

    val totalWinnings =
        lines
            .map { Hand(it) }
            .sorted()
            .mapIndexed { rank, hand -> ((rank + 1) * hand.bid).toLong() }
            .sum()

    return totalWinnings
}
