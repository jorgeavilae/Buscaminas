package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import com.jorgeav.buscaminas.domain.Cell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Jorge Avila on 08/05/2020.
 */
class ChangeMarkInCellUseCase(private val cellsRepository: Repository) {
    suspend operator fun invoke(cell : Cell) {
            withContext(Dispatchers.IO) {
                cellsRepository.changeMarkCell(cell)
            }
    }
}