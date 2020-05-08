package com.jorgeav.buscaminas.db

import android.content.Context
import com.jorgeav.buscaminas.data.IPersistentDataSource
import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class PersistenceDataSource(context: Context) : IPersistentDataSource {

    private val cellDBDao = CellDBDatabase.getInstance(context).cellDBDao

    override suspend fun addAll(cells: List<Cell>) {
        val cellsDB : List<CellDB> = cells.map {
            CellDB(it.x, it.y, it.isShowing, it.isMarked, it.isBomb, it.numberOfBombsInBounds) }
        cellDBDao.addAllToDatabase(cellsDB)
    }

    override suspend fun deleteAllCells() = cellDBDao.deleteAllCellsFromDatabase()

    override suspend fun readAll(): List<Cell> =
        cellDBDao.readAllFromDatabase().map {
            Cell(it.x, it.y, it.isShowing, it.isMarked, it.isBomb, it.numberOfBombsInBounds) }

    override suspend fun showCell(x: Int, y: Int) = cellDBDao.updateShowCellInDatabase(x,y)

    override suspend fun markCell(x: Int, y: Int) = cellDBDao.updateMarkCellInDatabase(x,y)
    override suspend fun unmarkCell(x: Int, y: Int) = cellDBDao.updateUnmarkCellInDatabase(x,y)
}