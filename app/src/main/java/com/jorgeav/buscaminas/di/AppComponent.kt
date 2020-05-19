package com.jorgeav.buscaminas.di

import android.content.Context
import com.jorgeav.buscaminas.ui.finishgame.GameLoseFragment
import com.jorgeav.buscaminas.ui.finishgame.GameWinFragment
import com.jorgeav.buscaminas.ui.minesweeper.MinesweeperFragment
import com.jorgeav.buscaminas.ui.newboard.NewBoardFragment
import dagger.BindsInstance
import dagger.Component

/**
 * Created by Jorge Avila on 18/05/2020.
 */
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