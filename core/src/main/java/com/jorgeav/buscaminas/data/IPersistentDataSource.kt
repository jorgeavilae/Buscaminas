package com.jorgeav.buscaminas.data

import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
interface IPersistentDataSource {
    suspend fun addAll(cells : List<Cell>)

    suspend fun deleteAllCells()

    suspend fun readAll() : List<Cell>

    suspend fun showCell(x : Int, y : Int)

    suspend fun markCell(x : Int, y : Int)
    suspend fun unmarkCell(x : Int, y : Int)

    suspend fun markCellsWithBomb()
    suspend fun revealBombs()
}