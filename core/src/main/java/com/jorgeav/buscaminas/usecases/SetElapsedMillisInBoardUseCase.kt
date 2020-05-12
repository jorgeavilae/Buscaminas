package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository

/**
 * Created by Jorge Avila on 11/05/2020.
 */
class SetElapsedMillisInBoardUseCase(private val cellsRepository: Repository) {
    operator fun invoke(millis: Long) = cellsRepository.setElapsedMillis(millis)
}