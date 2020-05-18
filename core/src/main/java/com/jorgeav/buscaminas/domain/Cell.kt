package com.jorgeav.buscaminas.domain

/**
 * Created by Jorge Avila on 07/05/2020.
 */
data class Cell (val x: Int, val y: Int,
                 var isShowing: Boolean = false,
                 var isMarked: Boolean = false,
                 var isBomb: Boolean = false,
                 var isRevealed: Boolean = false,
                 var numberOfBombsInBounds: Int = 0
) {
    val id: String = "$x-$y"

    fun getStateOfCell () : CellShowingState {
        return if (!isShowing)
            // Indicates there is a bomb
            if (isBomb && isRevealed) CellShowingState.StateBombRevealed
            // Hide with mark
            else if (isMarked) CellShowingState.StateMark
            // Hide without mark
            else CellShowingState.StateIdle
        else
            // Showing with bomb
            if (isBomb) CellShowingState.StateBombHit
            // Showing without bomb (number or empty)
            else CellShowingState.StateNumber(numberOfBombsInBounds)
    }
}

sealed class CellShowingState {
    // Hide with mark
    object StateMark : CellShowingState()
    // Hide without mark
    object StateIdle : CellShowingState()
    // Showing with bomb
    object StateBombHit : CellShowingState()
    // Indicates there is a bomb
    object StateBombRevealed : CellShowingState()
    // Showing without bomb (number or empty)
    data class StateNumber(val numberOfBombsInBounds: Int) : CellShowingState()
}