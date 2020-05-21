package com.jorgeav.buscaminas.di

import com.jorgeav.buscaminas.data.IKeyValueDataSource
import com.jorgeav.buscaminas.data.IPersistentDataSource
import com.jorgeav.buscaminas.db.StructuredDataSource
import dagger.Binds
import dagger.Module

/**
 * Created by Jorge Avila on 19/05/2020.
 */
@Module
abstract class DataSourceModule {

    // Makes Dagger provide StructuredDataSource when
    // IPersistentDataSource and IKeyValueDataSource are requested
    @Binds
    abstract fun providePersistentDataSource(dataSource: StructuredDataSource): IPersistentDataSource
    @Binds
    abstract fun provideKeyValueDataSource(dataSource: StructuredDataSource): IKeyValueDataSource
}