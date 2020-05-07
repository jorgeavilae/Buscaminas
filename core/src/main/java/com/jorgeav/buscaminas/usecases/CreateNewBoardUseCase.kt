package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class CreateNewBoardUseCase(private val generateBoardUseCase: GenerateBoardUseCase,
                            private val cellsRepository: Repository) {
    suspend operator fun invoke(rows : Int, columns : Int, bombs : Int) {
        val cells = generateBoardUseCase(rows, columns, bombs)
        cellsRepository.addAll(cells)
    }
}