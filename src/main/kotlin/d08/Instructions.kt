package d08

typealias Instructions = String
typealias Instruction = Char

fun Instructions.iterator() = InstructionsIterator(this)

fun Instruction.isLeft() = this == 'L'

fun Instruction.isRight() = this == 'R'

class InstructionsIterator(
    private val instructions: Instructions,
) : Iterator<Instruction> {
    private var i = 0

    override fun hasNext() = true

    override fun next(): Instruction {
        val c = instructions[i % instructions.length]
        i++
        return c
    }
}
