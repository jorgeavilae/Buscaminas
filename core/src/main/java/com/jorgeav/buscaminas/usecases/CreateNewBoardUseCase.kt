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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Jorge Avila on 07/05/2020.
 */
class CreateNewBoardUseCase @Inject constructor(private val repository: Repository) {
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