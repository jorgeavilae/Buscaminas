/*
 * Copyright 2020 Jorge Ávila Estévez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jorgeav.buscaminas.domain

/**
 * Created by Jorge Avila on 10/05/2020.
 */
internal object BoardUtils {

    private const val MIN_CELLS_BY_SIDE = 8
    private const val MAX_CELLS_BY_SIDE = 12

    fun getCellsBySideRange() : Pair<Int, Int> =
        Pair(MIN_CELLS_BY_SIDE, MAX_CELLS_BY_SIDE)
    fun getBombsRangeInBoard(cellsBySide: Int) : Pair<Int, Int> =
        Pair((cellsBySide*cellsBySide * 0.1).toInt(), (cellsBySide*cellsBySide * 0.5).toInt())

    /**
     * Generates a minesweeper square board with cells and bombs.
     *
     * Each cell represent a space where to place a mark. Also, it could be flipped to show
     * a number of adjacent cells with bombs or a bomb.
     *
     * Bombs are placed randomly.
     *
     * @param cellsBySide cells by side of the board. Board contains cellsBySide*cellBySide cells.
     * @param bombs numbers of bombs to place
     *
     * @return a Map with the cells generated as value and his position in board as key.
     */
    fun generateBoard(cellsBySide: Int, bombs: Int): Map<Pair<Int, Int>, Cell> {
        val cells = mutableMapOf<Pair<Int, Int>, Cell>()

        // Generate empty cells
        for (i in 0 until cellsBySide)
            for (j in 0 until cellsBySide)
                cells[Pair(i,j)] = Cell(i, j)

        // Fill board with bombs
        for (k in 0 until bombs) {
            // Select random Cell
            var randomPosition: Pair<Int, Int>
            do {
                randomPosition = Pair((0 until cellsBySide).random(), (0 until cellsBySide).random())
                val cellSelected = cells[randomPosition]
            } while (cellSelected == null || cellSelected.isBomb)

            // Place bomb in board
            cells[randomPosition]?.isBomb = true
            cells[randomPosition]?.numberOfBombsInBounds = -1

            // Add one to adjacent cells on top
            cells[Pair(randomPosition.first-1, randomPosition.second-1)]?.let {
                if (!it.isBomb) it.numberOfBombsInBounds += 1 }
            cells[Pair(randomPosition.first-1, randomPosition.second)]?.let {
                if (!it.isBomb) it.numberOfBombsInBounds += 1 }
            cells[Pair(randomPosition.first-1, randomPosition.second+1)]?.let {
                if (!it.isBomb) it.numberOfBombsInBounds += 1 }

            // Add one to adjacent cells on left and right
            cells[Pair(randomPosition.first, randomPosition.second-1)]?.let {
                if (!it.isBomb) it.numberOfBombsInBounds += 1 }
            cells[Pair(randomPosition.first, randomPosition.second+1)]?.let {
                if (!it.isBomb) it.numberOfBombsInBounds += 1 }

            // Add one to adjacent cells on bottom
            cells[Pair(randomPosition.first+1, randomPosition.second-1)]?.let {
                if (!it.isBomb) it.numberOfBombsInBounds += 1 }
            cells[Pair(randomPosition.first+1, randomPosition.second)]?.let {
                if (!it.isBomb) it.numberOfBombsInBounds += 1 }
            cells[Pair(randomPosition.first+1, randomPosition.second+1)]?.let {
                if (!it.isBomb) it.numberOfBombsInBounds += 1 }
        }

        return cells
    }

    fun countBombs(board: Map<Pair<Int, Int>, Cell>?) : Int? {
        board?.let {
            var bombs = 0
            for (cell in it.values)
                if (cell.isBomb)
                    bombs++
            return bombs
        }
        return null
    }

    fun countMarks(board: Map<Pair<Int, Int>, Cell>?) : Int? {
        board?.let {
            var marks = 0
            for (cell in it.values)
                if (cell.isMarked)
                    marks++
            return marks
        }
        return null
    }

    fun isBoardWinOrLose(board: Map<Pair<Int, Int>, Cell>?) : Boolean? {
        var aBombIsShowing = false
        var aCellIsNotFlipped = false
        board?.let {
            for (cell in it.values) {
                if (cell.isBomb && cell.isShowing) aBombIsShowing = true
                if (!cell.isBomb && !cell.isShowing) aCellIsNotFlipped = true
            }
        }

        return if (aBombIsShowing) false else if (aCellIsNotFlipped) null else true
    }


    /**
     * Function to determine which cells should flip.
     *
     * Uses private function with correct parameter to check if cell passed as parameter
     * should flip and checks adjacent cells recursively
     *
     * @param  cell  current cell to evaluate
     * @param  board complete board with all cells
     *
     * @return a List with the cells in board should flip.
     */
    fun cellsToFlipInBoard(cell: Cell?, board: Map<Pair<Int, Int>, Cell>?) : List<Cell> {
        return cellsToFlipInBoard(cell, board, mutableMapOf()).values.toList()
    }

    /**
     * Recursive function to determine which cells should flip.
     * Check if cell passed as parameter should flip and checks adjacent cells recursively
     *
     * @param  cell  current cell to evaluate
     * @param  board complete board with all cells
     * @param  mapOfCellsChecked cells flipped so far (prevent to evaluate a cell that is already checked)
     *
     * @return a Map with the cells in board should flip as value and his position as key.
     */
    private fun cellsToFlipInBoard(cell: Cell?,
                                   board: Map<Pair<Int, Int>, Cell>?,
                                   mapOfCellsChecked: MutableMap<Pair<Int, Int>, Cell> = mutableMapOf())
            : Map<Pair<Int, Int>, Cell> {

        if (cell != null && board != null) {

            // Should include this cell? If not already included or flipped, yes.
            val x = cell.x
            val y = cell.y
            if (!mapOfCellsChecked.containsKey(Pair(x,y)) && !cell.isShowing) {
                mapOfCellsChecked[Pair(x,y)] = cell

                // Check adjacent cells recursively when it's zero because no bomb surrounding
                if (cell.numberOfBombsInBounds == 0 && !cell.isBomb) {

                    // Cells on top
                    mapOfCellsChecked.putAll(
                        cellsToFlipInBoard(board[Pair(x-1, y-1)], board, mapOfCellsChecked))
                    mapOfCellsChecked.putAll(
                        cellsToFlipInBoard(board[Pair(x-1, y)], board, mapOfCellsChecked))
                    mapOfCellsChecked.putAll(
                        cellsToFlipInBoard(board[Pair(x-1, y+1)], board, mapOfCellsChecked))

                    // Cells on left and right
                    mapOfCellsChecked.putAll(
                        cellsToFlipInBoard(board[Pair(x, y-1)], board, mapOfCellsChecked))
                    mapOfCellsChecked.putAll(
                        cellsToFlipInBoard(board[Pair(x, y+1)], board, mapOfCellsChecked))

                    // Cells on bottom
                    mapOfCellsChecked.putAll(
                        cellsToFlipInBoard(board[Pair(x+1, y-1)], board, mapOfCellsChecked))
                    mapOfCellsChecked.putAll(
                        cellsToFlipInBoard(board[Pair(x+1, y)], board, mapOfCellsChecked))
                    mapOfCellsChecked.putAll(
                        cellsToFlipInBoard(board[Pair(x+1, y+1)], board, mapOfCellsChecked))
                }
            }
        }
        return mapOfCellsChecked
    }
}