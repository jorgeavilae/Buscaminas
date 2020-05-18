package com.jorgeav.buscaminas.data

import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class Repository(private val persistentDataSource: IPersistentDataSource,
                 private val keyValueDataSource: IKeyValueDataSource) {

    suspend fun addAll(cellsList : List<Cell>) =
        persistentDataSource.addAll(cellsList)
    suspend fun addAll(cellsMap : Map<Pair<Int, Int>, Cell>) =
        persistentDataSource.addAll(cellsMap.values.toList())

    suspend fun deleteAllCells() = persistentDataSource.deleteAllCells()

    suspend fun readAll() : Map<Pair<Int, Int>, Cell> =
        persistentDataSource.readAll()
            .map { Pair(it.x, it.y) to it }
            .sortedBy { it.first.first*1000 + it.first.second }
            .toMap()

    suspend fun showCell(cell : Cell) {
        persistentDataSource.unmarkCell(cell.x, cell.y)
        persistentDataSource.showCell(cell.x, cell.y)
    }

    suspend fun changeMarkCell(cell : Cell) =
        if (!cell.isMarked) persistentDataSource.markCell(cell.x, cell.y)
        else persistentDataSource.unmarkCell(cell.x, cell.y)

    suspend fun markCellsWithBomb() = persistentDataSource.markCellsWithBomb()
    suspend fun revealBombs() = persistentDataSource.revealBombs()

    fun getElapsedMillis() : Long = keyValueDataSource.getElapsedMillis()
    fun setElapsedMillis(millis : Long) = keyValueDataSource.setElapsedMillis(millis)

    fun getCellsBySide() : Int = keyValueDataSource.getCellsBySide()
    fun setCellsBySide(cellsBySide : Int) = keyValueDataSource.setCellsBySide(cellsBySide)

    fun getBombs() : Int = keyValueDataSource.getBombs()
    fun setBombs(bombs : Int) = keyValueDataSource.setBombs(bombs)
}