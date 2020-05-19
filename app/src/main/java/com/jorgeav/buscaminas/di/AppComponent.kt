package com.jorgeav.buscaminas.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

/**
 * Created by Jorge Avila on 18/05/2020.
 */
@Component()
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}