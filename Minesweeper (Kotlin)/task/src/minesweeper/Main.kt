package minesweeper

import kotlin.random.Random

const val ROWS = 9
const val COLS = 9

fun Char.isMine() = this == 'X'

fun main() {
    print("How many mines do you want on the field? ")
    val mineCount = readln().toInt()
    val mineField = MineField(ROWS, COLS, mineCount)
    println()
    println(mineField)
    while (!mineField.hasWon()) {
        print("Set/unset mines marks or claim a cell as free: ")
        val (x, y, type) = readln().split(" ")
        val cellChoice = Pair(y.toInt() - 1, x.toInt() - 1)
        if (type == "mine") {
            mineField.toggleMark(cellChoice.first, cellChoice.second)
        } else {
            if (mineField.mineLocs.contains(cellChoice)){
                mineField.revealMines()
                println()
                println(mineField)
                println("You stepped on a mine and failed!")
                return
            } else {
                mineField.free(cellChoice)
            }
        }
        println()
        println(mineField)
    }
    println("Congratulations! You found all the mines!")
}

class MineField(private val rows: Int, private val columns: Int, private val mineCount: Int) {
    private val mineField = Array(rows){CharArray(columns) {'.'} }
    private val gameField = Array(rows){CharArray(columns) {'.'} }
    private val totalCells = rows * columns
    val mineLocs: MutableList<Pair<Int, Int>> = mutableListOf()
    private val markLocs: MutableList<Pair<Int, Int>> = mutableListOf()
    private val freeLocs: MutableList<Pair<Int, Int>> = mutableListOf()

    init {
        repeat(mineCount) {
            var randomRow: Int
            var randomColumn: Int
            do {
                randomRow = Random.nextInt(0, rows)
                randomColumn = Random.nextInt(0, columns)
            } while (mineField[randomRow][randomColumn].isMine())

            mineField[randomRow][randomColumn] = 'X'
            mineLocs.add(Pair(randomRow, randomColumn))
        }
        mineField.indices
            .forEach { r -> mineField[0].indices
                .forEach { c -> if (!mineField[r][c].isMine()) applyMineCount(r, c)} }
    }

    private fun getSurroundingCells(row: Int, column: Int): List<Pair<Int, Int>> {
        val rowRange = row - 1..row + 1
        val columnRange = column - 1..column + 1
        return rowRange.flatMap { r -> columnRange.map { c -> Pair(r, c) } }
            .filter { it.first >= 0 && it.first <= rows - 1}
            .filter { it.second >= 0 && it.second <= columns - 1 }
            .filter { !(it.first == row && it.second == column) }
    }

    private fun countSurroundingMines(row: Int, column: Int) = getSurroundingCells(row, column)
        .count { mineField[it.first][it.second].isMine() }

    private fun applyMineCount(row: Int, column: Int) {
        val surroundingMines = countSurroundingMines(row, column)
        if (surroundingMines != 0) mineField[row][column] = surroundingMines.digitToChar()
    }

    fun hasWon(): Boolean {
        return (mineLocs.count() == markLocs.count() && markLocs.containsAll(markLocs)) ||
                (totalCells - mineCount - freeLocs.count() == 0)
    }

    fun toggleMark(row: Int, column: Int) {
        if (gameField[row][column] != '*') {
            gameField[row][column] = '*'
            markLocs.add(Pair(row, column))
        } else {
            gameField[row][column] = '.'
            markLocs.remove(Pair(row, column))
        }
    }

    fun free(cell: Pair<Int, Int>) {
        when  {
            mineField[cell.first][cell.second].isDigit() ->
                gameField[cell.first][cell.second] = mineField[cell.first][cell.second]
            gameField[cell.first][cell.second] == '/' -> return
            else -> {
                gameField[cell.first][cell.second] ='/'
                freeLocs.add(cell)
                val surrounding = getSurroundingCells(cell.first, cell.second)
                surrounding.forEach{ free(it) }
            }
        }
    }

    fun revealMines() = mineLocs.forEach { gameField[it.first][it.second] = 'X' }

    override fun toString(): String {
        return " |123456789|\n" +
                "-|---------|\n" +
                gameField.indices.joinToString("") { gameField[it].joinToString("", prefix = "${it + 1}|", postfix = "|\n") } +
                "-|---------|"
    }
}
