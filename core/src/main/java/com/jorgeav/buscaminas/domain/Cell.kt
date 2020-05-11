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
        fun getStateOfCell (cell: Cell) : CellShowingState {
            return if (!cell.isShowing)
                // Hide with mark
                if (cell.isMarked) CellShowingState.StateMark
                // Hide without mark
                else CellShowingState.StateUnMark
            else
                // Showing with bomb
                if (cell.isBomb) CellShowingState.StateBomb
                // Showing without bomb (number or empty)
                else CellShowingState.StateNumber(cell.numberOfBombsInBounds)
        }
    }
}

sealed class CellShowingState {
    object StateMark : CellShowingState()
    object StateUnMark : CellShowingState()
    object StateBomb : CellShowingState()
    data class StateNumber(val numberOfBombsInBounds: Int) : CellShowingState()
}