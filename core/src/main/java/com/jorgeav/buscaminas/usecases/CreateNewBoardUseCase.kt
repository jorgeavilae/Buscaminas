package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class CreateNewBoardUseCase(private val deleteBoardUseCase: DeleteBoardUseCase,
                            private val setElapsedMillisInBoardUseCase: SetElapsedMillisInBoardUseCase,
                            private val setCellsBySideUseCase: SetCellsBySideUseCase,
                            private val generateBoardUseCase: GenerateBoardUseCase,
                            private val repository: Repository) {
    suspend operator fun invoke(cellsBySide : Int, bombs : Int) {
        deleteBoardUseCase()
        setElapsedMillisInBoardUseCase(0L)
        setCellsBySideUseCase(cellsBySide)
        val cells = generateBoardUseCase(cellsBySide, bombs)

        withContext(Dispatchers.IO) {
            repository.addAll(cells)
        }
    }
}