package com.jorgeav.buscaminas.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Jorge Avila on 07/05/2020.
 */
@Dao
interface CellDBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllToDatabase(cells: List<CellDB>)

    @Query("DELETE FROM cell_table")
    suspend fun deleteAllCellsFromDatabase()

    @Query("SELECT * FROM cell_table")
    suspend fun readAllFromDatabase(): List<CellDB>

    @Query("UPDATE cell_table SET isShowing = 1 WHERE x = :x AND y = :y")
    suspend fun updateFlipCellInDatabase(x: Int, y: Int)

    @Query("UPDATE cell_table SET isMarked = 1 WHERE x = :x AND y = :y")
    suspend fun updateMarkCellInDatabase(x: Int, y: Int)

    @Query("UPDATE cell_table SET isMarked = 0 WHERE x = :x AND y = :y")
    suspend fun updateUnmarkCellInDatabase(x: Int, y: Int)
}