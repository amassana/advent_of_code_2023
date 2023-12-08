package d08

data class Node(
    val label: Label,
    val left: Label,
    val right: Label,
) {
    fun isTerminal() = label[2] == 'Z'

    fun isStart() = label[2] == 'A'

    companion object {
        fun of(s: String) =
            Node(
                label = s.substring(0, 3),
                left = s.substring(7, 10),
                right = s.substring(12, 15),
            )
    }
}

typealias Label = String
