package d07

data class Card(val figure: Char) : Comparable<Card> {
    private val value: Char = figureValue.value(figure)

    override fun compareTo(other: Card) = this.value - other.value

    companion object {
        private lateinit var figureValue: FigureValue

        fun onGameTypeChanged(newGameType: GameType) =
            when (newGameType) {
                GameType.STANDARD -> figureValue = StandardValue
                GameType.JOKER_WILDCARD -> figureValue = JokerValue
            }
    }
}

interface FigureValue {
    fun value(figure: Char): Char
}

object StandardValue : FigureValue {
    override fun value(figure: Char): Char =
        when (figure) {
            'A' -> '9' + 5
            'K' -> '9' + 4
            'Q' -> '9' + 3
            'J' -> '9' + 2
            'T' -> '9' + 1
            else -> figure
        }
}

object JokerValue : FigureValue {
    override fun value(figure: Char): Char =
        when (figure) {
            'A' -> '9' + 5
            'K' -> '9' + 4
            'Q' -> '9' + 3
            'J' -> '1'
            'T' -> '9' + 1
            else -> figure
        }
}
