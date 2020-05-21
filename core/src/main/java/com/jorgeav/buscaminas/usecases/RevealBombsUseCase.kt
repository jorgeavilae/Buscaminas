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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Jorge Avila on 18/05/2020.
 */
class RevealBombsUseCase @Inject constructor(private val cellsRepository: Repository) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            cellsRepository.revealBombs()
        }
    }
}