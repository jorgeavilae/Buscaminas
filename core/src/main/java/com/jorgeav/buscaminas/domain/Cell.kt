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
}