package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import javax.inject.Inject

/**
 * Created by Jorge Avila on 15/05/2020.
 */
class GetBombsInBoardUseCase @Inject constructor(private val cellsRepository: Repository) {
    operator fun invoke() : Int = cellsRepository.getBombs()
}