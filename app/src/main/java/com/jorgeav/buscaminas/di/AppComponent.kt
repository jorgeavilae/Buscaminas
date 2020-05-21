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

package com.jorgeav.buscaminas.di

import android.content.Context
import com.jorgeav.buscaminas.ui.finishgame.GameLoseFragment
import com.jorgeav.buscaminas.ui.finishgame.GameWinFragment
import com.jorgeav.buscaminas.ui.minesweeper.MinesweeperFragment
import com.jorgeav.buscaminas.ui.newboard.NewBoardFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Jorge Avila on 18/05/2020.
 */
@Singleton
@Component(modules = [DataSourceModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: NewBoardFragment)
    fun inject(fragment: MinesweeperFragment)
    fun inject(fragment: GameWinFragment)
    fun inject(fragment: GameLoseFragment)
}