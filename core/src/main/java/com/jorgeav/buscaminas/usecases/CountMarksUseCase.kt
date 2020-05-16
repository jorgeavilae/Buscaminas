package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.domain.BoardUtils
import com.jorgeav.buscaminas.domain.Cell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Jorge Avila on 16/05/2020.
 */
class CountMarksUseCase() {
    suspend operator fun invoke(board : Map<Pair<Int, Int>, Cell>?) : Int? =
        withContext(Dispatchers.Default) {
            BoardUtils.countMarks(board)
        }
}