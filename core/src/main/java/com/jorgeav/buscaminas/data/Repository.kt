package com.jorgeav.buscaminas.data

import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class Repository(private val dataSource: IPersistentDataSource) {

    suspend fun addAll(cells : Map<Pair<Int, Int>, Cell>) = dataSource.addAll(cells.values.toList())

    suspend fun readAll() : Map<Pair<Int, Int>, Cell> =
        dataSource.readAll().map { Pair(it.x, it.y) to it }.toMap()

    suspend fun showCell(cell : Cell) = dataSource.showCell(cell.x, cell.y)

    suspend fun markCell(cell : Cell) = dataSource.markCell(cell.x, cell.y)
}