package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class CreateNewBoardUseCase(private val deleteBoardUseCase: DeleteBoardUseCase,
                            private val generateBoardUseCase: GenerateBoardUseCase,
                            private val cellsRepository: Repository) {
    suspend operator fun invoke(cellsBySide : Int, bombs : Int) {
        deleteBoardUseCase()
        val cells = generateBoardUseCase(cellsBySide, bombs)

        withContext(Dispatchers.IO) {
            cellsRepository.addAll(cells)
        }
    }
}