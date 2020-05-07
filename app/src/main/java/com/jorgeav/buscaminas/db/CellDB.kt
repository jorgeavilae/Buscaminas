package com.jorgeav.buscaminas.db

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Created by Jorge Avila on 07/05/2020.
 */
@Entity(tableName = "cell_table", primaryKeys = ["x","y"])
data class CellDB (

    @ColumnInfo(name = "x")
    var x: Int,

    @ColumnInfo(name = "y")
    var y: Int,

    @ColumnInfo(name = "isShowing")
    var isShowing: Boolean = false,

    @ColumnInfo(name = "isMarked")
    var isMarked: Boolean = false,

    @ColumnInfo(name = "isBomb")
    var isBomb: Boolean = false,

    @ColumnInfo(name = "numberOfBombsInBounds")
    var numberOfBombsInBounds: Int = 0
)