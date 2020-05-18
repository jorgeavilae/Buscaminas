package com.jorgeav.buscaminas.db

import android.app.Activity
import android.content.Context
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.data.IKeyValueDataSource
import com.jorgeav.buscaminas.data.IPersistentDataSource
import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class StructuredDataSource(private val context: Context)
    : IPersistentDataSource, IKeyValueDataSource {

    private val cellDBDao = CellDBDatabase.getInstance(context).cellDBDao

    override suspend fun addAll(cells: List<Cell>) {
        val cellsDB : List<CellDB> = cells.map {
            CellDB(it.x, it.y, it.isShowing, it.isMarked, it.isBomb, it.isRevealed, it.numberOfBombsInBounds) }
        cellDBDao.addAllToDatabase(cellsDB)
    }

    override suspend fun deleteAllCells() = cellDBDao.deleteAllCellsFromDatabase()

    override suspend fun readAll(): List<Cell> =
        cellDBDao.readAllFromDatabase().map {
            Cell(it.x, it.y, it.isShowing, it.isMarked, it.isBomb, it.isRevealed, it.numberOfBombsInBounds) }

    override suspend fun showCell(x: Int, y: Int) = cellDBDao.updateShowCellInDatabase(x,y)

    override suspend fun markCell(x: Int, y: Int) = cellDBDao.updateMarkCellInDatabase(x,y)
    override suspend fun unmarkCell(x: Int, y: Int) = cellDBDao.updateUnmarkCellInDatabase(x,y)

    override suspend fun markCellsWithBomb() = cellDBDao.updateMarkAllBombsInDatabase()
    override suspend fun revealBombs() = cellDBDao.updateRevealAllBombsInDatabase()

    override fun getElapsedMillis(): Long {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE) ?: return 0L
        return sharedPref.getLong(context.getString(R.string.elapsed_time_preference_key), 0L)
    }

    override fun setElapsedMillis(millis: Long) {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putLong(context.getString(R.string.elapsed_time_preference_key), millis)
            commit()
        }
    }

    override fun getCellsBySide(): Int {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE) ?: return 1
        return sharedPref.getInt(context.getString(R.string.cells_side_preference_key), 1)
    }

    override fun setCellsBySide(cellsBySide: Int) {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(context.getString(R.string.cells_side_preference_key), cellsBySide)
            commit()
        }
    }

    override fun getBombs(): Int {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE) ?: return 0
        return sharedPref.getInt(context.getString(R.string.bombs_preference_key), 0)
    }

    override fun setBombs(bombs: Int) {
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(context.getString(R.string.bombs_preference_key), bombs)
            commit()
        }
    }

}