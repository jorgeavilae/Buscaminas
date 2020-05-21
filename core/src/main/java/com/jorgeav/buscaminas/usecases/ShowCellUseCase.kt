/*
 * Copyright 2020 Jorge Ávila Estévez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jorgeav.buscaminas.usecases

import com.jorgeav.buscaminas.data.Repository
import com.jorgeav.buscaminas.domain.BoardUtils
import com.jorgeav.buscaminas.domain.Cell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Jorge Avila on 08/05/2020.
 */
class ShowCellUseCase(private val cellsRepository: Repository) {
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