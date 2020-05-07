package com.jorgeav.buscaminas.data

import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class Repository(private val dataSource: IPersistentDataSource) {

    suspend fun addAll(cells : List<Cell>) = dataSource.addAll(cells)

    suspend fun readAll() : List<Cell> = dataSource.readAll()

    suspend fun showCell(cell : Cell) = dataSource.showCell(cell.x, cell.y)

    suspend fun markCell(cell : Cell) = dataSource.markCell(cell.x, cell.y)
}