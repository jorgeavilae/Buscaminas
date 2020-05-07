package com.jorgeav.buscaminas.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Jorge Avila on 07/05/2020.
 */

const val DATABASE_NAME = "cell_database"

@Database(entities = [CellDB::class], version = 2, exportSchema = false)
abstract class CellDBDatabase : RoomDatabase() {
    abstract val cellDBDao: CellDBDao

    companion object {
        @Volatile
        private var INSTANCE : CellDBDatabase? = null

        fun getInstance(context: Context) : CellDBDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CellDBDatabase::class.java,
                        DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}