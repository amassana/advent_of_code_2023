package d08

import all.lcm

class Network(
    val instructions: Instructions,
    val network: Map<Label, Node>,
) {
    fun stepsUntilTerminalFrom(label: Label): Int {
        var currentNode = network.getValue(label)
        val iterator = iterator(currentNode)
        var steps = 0

        while (!currentNode.isTerminal()) {
            currentNode = iterator.next()

            steps++
        }

        return steps
    }

    fun ghostSteps() =
        network.values
            .filter { it.isStart() }
            .map { stepsUntilTerminalFrom(it.label) }
            .map { it.toLong() }
            .lcm()

    fun iterator(startNode: Node) = NetworkIterator(this, startNode)
}

class NetworkIterator(
    private val network: Network,
    startNode: Node,
) : Iterator<Node> {
    private val instructionIterator = network.instructions.iterator()
    private var currentNode = network.network.getValue(startNode.label)

    override fun hasNext() = true

    override fun next(): Node {
        currentNode =
            if (instructionIterator.next().isLeft()) {
                network.network.getValue(currentNode.left)
            } else {
                network.network.getValue(currentNode.right)
            }

        return currentNode
    }
}

fun Network(
    instructions: Instructions,
    lines: List<String>,
): Network {
    val network =
        lines
            .map { Node.of(it) }
            .associateBy { it.label }

    return Network(instructions, network)
}
