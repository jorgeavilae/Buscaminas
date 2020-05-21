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