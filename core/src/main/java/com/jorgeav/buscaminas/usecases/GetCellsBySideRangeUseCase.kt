package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.domain.BoardUtils

/**
 * Created by Jorge Avila on 16/05/2020.
 */
class GetCellsBySideRangeUseCase() {
    operator fun invoke() : Pair<Int, Int> =
        BoardUtils.getCellsBySideRange()
}