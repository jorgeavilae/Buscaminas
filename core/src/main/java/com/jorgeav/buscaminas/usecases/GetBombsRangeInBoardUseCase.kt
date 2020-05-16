package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.domain.BoardUtils

/**
 * Created by Jorge Avila on 16/05/2020.
 */
class GetBombsRangeInBoardUseCase() {
    operator fun invoke(cellsBySide: Int) : Pair<Int, Int> =
        BoardUtils.getBombsRangeInBoard(cellsBySide)
}