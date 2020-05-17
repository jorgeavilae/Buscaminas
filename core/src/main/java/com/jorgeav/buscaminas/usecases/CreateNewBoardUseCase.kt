package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import com.jorgeav.buscaminas.domain.BoardUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class CreateNewBoardUseCase(private val repository: Repository) {
    suspend operator fun invoke(cellsBySide : Int, bombs : Int) {
        withContext(Dispatchers.IO) {
            repository.deleteAllCells()
            repository.setCellsBySide(cellsBySide)
            repository.setBombs(bombs)
            repository.setElapsedMillis(0L)

            val cells = withContext(Dispatchers.Default) {
                BoardUtils.generateBoard(cellsBySide, bombs)
            }

            repository.addAll(cells)
        }
    }
}