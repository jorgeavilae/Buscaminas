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

package com.jorgeav.buscaminas.data

import com.jorgeav.buscaminas.domain.Cell
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Jorge Avila on 07/05/2020.
 */
@Singleton
class Repository @Inject constructor(private val persistentDataSource: IPersistentDataSource,
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