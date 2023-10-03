package minesweeper

import kotlin.random.Random

fun main() {
    print("How many mines do you want on the field? ")
    val mineCount = readln().toInt()
    val mineField = MineField(9, 9, mineCount)
    println(mineField)
}

class MineField(private val rows: Int, private val columns: Int, mines: Int) {
    private val mineField = Array(rows){CharArray(columns) {'.'} }

    init {
        repeat(mines) {
            var randomRow = Random.nextInt(0, rows)
            var randomColumn = Random.nextInt(0, columns)

            while (mineField[randomRow][randomColumn] == 'X') {
                randomRow = Random.nextInt(0, rows)
                randomColumn = Random.nextInt(0, columns)
            }

            mineField[randomRow][randomColumn] = 'X'
        }
    }

    override fun toString(): String {
        return mineField.joinToString("") { it.joinToString("", postfix = "\n") }
    }
}
