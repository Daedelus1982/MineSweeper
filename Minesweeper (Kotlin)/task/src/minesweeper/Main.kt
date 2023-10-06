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
            var randomRow: Int
            var randomColumn: Int
            do {
                randomRow = Random.nextInt(0, rows)
                randomColumn = Random.nextInt(0, columns)
            } while (mineField[randomRow][randomColumn] == 'X')

            mineField[randomRow][randomColumn] = 'X'
        }
        mineField.indices
            .forEach { r -> mineField[0].indices
                .forEach { c -> if (!isMine(r, c)) applyMineCount(r, c)} }
    }

    private fun isMine(row: Int, column: Int) = mineField[row][column] == 'X'

    private fun surroundingCells(row: Int, column: Int): List<Pair<Int, Int>> {
        val rowRange = row - 1..row + 1
        val columnRange = column - 1..column + 1
        return rowRange.flatMap { r -> columnRange.map { c -> Pair(r, c) } }
            .filter { it.first >= 0 && it.first <= rows - 1}
            .filter { it.second >= 0 && it.second <= columns - 1 }
            .filter { !(it.first == row && it.second == column) }
    }

    private fun countSurroundingMines(row: Int, column: Int) = surroundingCells(row, column)
        .count { isMine(it.first, it.second) }

    private fun applyMineCount(row: Int, column: Int) {
        val surroundingMines = countSurroundingMines(row, column)
        if (surroundingMines != 0) mineField[row][column] = surroundingMines.digitToChar()
    }

    override fun toString(): String {
        return mineField.joinToString("") { it.joinToString("", postfix = "\n") }
    }
}
