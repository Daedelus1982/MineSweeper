package minesweeper

import kotlin.random.Random

fun main() {
    val mineField = MineField(9, 9, 10)
    println(mineField)
}

class MineField(private val rows: Int, private val columns: Int, mines: Int) {
    private val mineField = Array(rows){CharArray(columns) {'.'} }

    init {
        repeat(mines) {
            val randomRow = Random.nextInt(0, rows)
            val randomColumn = Random.nextInt(0, columns)

            mineField[randomRow][randomColumn] = 'X'
        }
    }

    override fun toString(): String {
        return mineField.joinToString("") { it.joinToString("", postfix = "\n") }
    }
}
