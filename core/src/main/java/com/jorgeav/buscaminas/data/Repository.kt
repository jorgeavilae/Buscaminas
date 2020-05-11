package com.jorgeav.buscaminas.data

import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class Repository(private val persistentDataSource: IPersistentDataSource, private val keyValueDataSource: IKeyValueDataSource) {

    suspend fun addAll(cells : Map<Pair<Int, Int>, Cell>) = persistentDataSource.addAll(cells.values.toList())

    suspend fun deleteAllCells() = persistentDataSource.deleteAllCells()

    suspend fun readAll() : Map<Pair<Int, Int>, Cell> =
        persistentDataSource.readAll().map { Pair(it.x, it.y) to it }.toMap()

    suspend fun showCell(cell : Cell) {
        persistentDataSource.unmarkCell(cell.x, cell.y)
        persistentDataSource.showCell(cell.x, cell.y)
    }

    suspend fun changeMarkCell(cell : Cell) =
        if (!cell.isMarked) persistentDataSource.markCell(cell.x, cell.y)
        else persistentDataSource.unmarkCell(cell.x, cell.y)

    fun getElapsedMillis() : Long = keyValueDataSource.getElapsedMillis()
    fun setElapsedMillis(millis : Long) = keyValueDataSource.setElapsedMillis(millis)
}