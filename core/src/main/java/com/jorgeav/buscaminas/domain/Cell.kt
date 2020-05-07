package com.jorgeav.buscaminas.domain

/**
 * Created by Jorge Avila on 07/05/2020.
 */
data class Cell (val x: Int, val y: Int,
                 var isShowing: Boolean = false,
                 var isMarked: Boolean = false,
                 var isBomb: Boolean = false,
                 var numberOfBombsInBounds: Int = 0
) {
    val id: String = "$x-$y"

    companion object {
        fun generateBoard(rows: Int, columns: Int, bombs: Int): Map<Pair<Int, Int>, Cell> {
            val cells = mutableMapOf<Pair<Int, Int>, Cell>()

            // Generate empty cells
            for (i in 0 until rows)
                for (j in 0 until columns)
                    cells[Pair(i,j)] = Cell(i, j)

            // Fill board with bombs
            for (k in 0 until bombs) {
                // Select random Cell
                var randomPosition: Pair<Int, Int>
                do {
                    randomPosition = Pair((0 until rows).random(), (0 until columns).random())
                    val cellSelected = cells[randomPosition]
                } while (cellSelected == null || cellSelected.isBomb)

                // Place bomb in board
                cells[randomPosition]?.isBomb = true
                cells[randomPosition]?.numberOfBombsInBounds = 0

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
    }
}