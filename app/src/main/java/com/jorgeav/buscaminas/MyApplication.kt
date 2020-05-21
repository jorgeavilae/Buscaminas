package com.jorgeav.buscaminas

import android.app.Application
import com.jorgeav.buscaminas.di.AppComponent
import com.jorgeav.buscaminas.di.DaggerAppComponent

/**
 * Created by Jorge Avila on 18/05/2020.
 */
open class MyApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}