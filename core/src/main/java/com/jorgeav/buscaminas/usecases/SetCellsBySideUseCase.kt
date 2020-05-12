package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository

/**
 * Created by Jorge Avila on 12/05/2020.
 */
class SetCellsBySideUseCase(private val cellsRepository: Repository) {
    operator fun invoke(cellsBySide: Int) = cellsRepository.setCellsBySide(cellsBySide)
}