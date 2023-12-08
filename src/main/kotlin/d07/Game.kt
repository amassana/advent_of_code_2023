package d07

import all.Signal
import kotlin.properties.Delegates

object Game {
    var gameType: GameType
        by Delegates.observable(GameType.STANDARD) { _, _, newValue -> gameTypeSignal.emit(newValue) }

    private val gameTypeSignal = Signal<GameType>()

    fun observeGameType(callback: (newValue: GameType) -> Unit) {
        gameTypeSignal.connect(callback)
    }
}

enum class GameType {
    STANDARD,
    JOKER_WILDCARD,
}
