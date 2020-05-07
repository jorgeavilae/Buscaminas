package com.jorgeav.buscaminas.db

import com.jorgeav.buscaminas.data.IPersistentDataSource
import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class PersistenceDataSource : IPersistentDataSource {
    override suspend fun addAll(cells: List<Cell>) {
        TODO("Not yet implemented")
    }

    override suspend fun readAll(): List<Cell> {
        TODO("Not yet implemented")
    }

    override suspend fun showCell(x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun markCell(x: Int, y: Int) {
        TODO("Not yet implemented")
    }
}