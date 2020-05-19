package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import javax.inject.Inject

/**
 * Created by Jorge Avila on 11/05/2020.
 */
class SetElapsedMillisInBoardUseCase @Inject constructor(private val cellsRepository: Repository) {
    operator fun invoke(millis: Long) = cellsRepository.setElapsedMillis(millis)
}