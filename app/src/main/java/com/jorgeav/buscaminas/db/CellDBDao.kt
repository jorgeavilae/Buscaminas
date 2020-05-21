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
    suspend fun updateShowCellInDatabase(x: Int, y: Int)

    @Query("UPDATE cell_table SET isMarked = 1 WHERE x = :x AND y = :y")
    suspend fun updateMarkCellInDatabase(x: Int, y: Int)

    @Query("UPDATE cell_table SET isMarked = 0 WHERE x = :x AND y = :y")
    suspend fun updateUnmarkCellInDatabase(x: Int, y: Int)

    @Query("UPDATE cell_table SET isMarked = 1 WHERE isBomb = 1")
    suspend fun updateMarkAllBombsInDatabase()

    @Query("UPDATE cell_table SET isRevealed = 1 WHERE isBomb = 1")
    suspend fun updateRevealAllBombsInDatabase()
}