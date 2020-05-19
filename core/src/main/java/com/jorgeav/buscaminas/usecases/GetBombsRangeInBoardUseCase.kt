package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.domain.BoardUtils
import javax.inject.Inject

/**
 * Created by Jorge Avila on 16/05/2020.
 */
class GetBombsRangeInBoardUseCase @Inject constructor() {
    operator fun invoke(cellsBySide: Int) : Pair<Int, Int> =
        BoardUtils.getBombsRangeInBoard(cellsBySide)
}