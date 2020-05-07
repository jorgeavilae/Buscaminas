package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class GenerateBoardUseCase() {
    suspend operator fun invoke(rows : Int, columns : Int, bombs : Int) : Map<Pair<Int, Int>, Cell> =
        Cell.generateBoard(rows, columns, bombs)
}