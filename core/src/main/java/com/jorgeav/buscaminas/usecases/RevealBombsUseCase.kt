package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Jorge Avila on 18/05/2020.
 */
class RevealBombsUseCase(private val cellsRepository: Repository) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            cellsRepository.revealBombs()
        }
    }
}