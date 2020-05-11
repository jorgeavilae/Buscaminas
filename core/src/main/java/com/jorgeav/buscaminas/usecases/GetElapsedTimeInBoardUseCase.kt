package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository

/**
 * Created by Jorge Avila on 11/05/2020.
 */
class GetElapsedMillisInBoardUseCase(private val cellsRepository: Repository) {
    operator fun invoke() : Long = cellsRepository.getElapsedMillis()
}