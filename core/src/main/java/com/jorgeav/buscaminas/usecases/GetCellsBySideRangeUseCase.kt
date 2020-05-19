package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.domain.BoardUtils
import javax.inject.Inject

/**
 * Created by Jorge Avila on 16/05/2020.
 */
class GetCellsBySideRangeUseCase @Inject constructor() {
    operator fun invoke() : Pair<Int, Int> =
        BoardUtils.getCellsBySideRange()
}