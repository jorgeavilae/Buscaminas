package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository

/**
 * Created by Jorge Avila on 12/05/2020.
 */
class GetCellsBySideUseCase(private val cellsRepository: Repository) {
    operator fun invoke() : Int = cellsRepository.getCellsBySide()
}