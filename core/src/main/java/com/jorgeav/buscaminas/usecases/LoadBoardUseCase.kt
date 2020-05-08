package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import com.jorgeav.buscaminas.domain.Cell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Jorge Avila on 08/05/2020.
 */
class LoadBoardUseCase(private val cellsRepository: Repository) {
    suspend operator fun invoke() : Map<Pair<Int, Int>, Cell> =
        withContext(Dispatchers.IO) {
            cellsRepository.readAll()
        }
}
