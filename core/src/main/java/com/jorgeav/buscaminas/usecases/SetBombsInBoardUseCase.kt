package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository

/**
 * Created by Jorge Avila on 15/05/2020.
 */
class SetBombsInBoardUseCase(private val cellsRepository: Repository) {
    operator fun invoke(bombs: Int) = cellsRepository.setBombs(bombs)
}