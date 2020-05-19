package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import com.jorgeav.buscaminas.domain.BoardUtils
import com.jorgeav.buscaminas.domain.Cell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Jorge Avila on 08/05/2020.
 */
class ShowCellUseCase @Inject constructor(private val cellsRepository: Repository) {
    suspend operator fun invoke(cellToFlip: Cell, board: Map<Pair<Int, Int>, Cell>?) {
        withContext(Dispatchers.IO) {
            val listOfCells = BoardUtils.cellsToFlipInBoard(cellToFlip, board)
            cellsRepository.addAll(
                listOfCells.map {
                    Cell(it.x, it.y,
                        isShowing = true,
                        isMarked = false,
                        isBomb = it.isBomb,
                        isRevealed = it.isRevealed,
                        numberOfBombsInBounds = it.numberOfBombsInBounds
                    ) })
        }
    }
}