package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.domain.BoardUtils
import com.jorgeav.buscaminas.domain.Cell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Jorge Avila on 16/05/2020.
 */
class CheckBoardWinOrLoseUseCase @Inject constructor() {
    suspend operator fun invoke(board : Map<Pair<Int, Int>, Cell>?) : Boolean? =
        withContext(Dispatchers.Default) {
            BoardUtils.isBoardWinOrLose(board)
        }
}