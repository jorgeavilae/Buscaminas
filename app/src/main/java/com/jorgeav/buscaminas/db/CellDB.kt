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

    @ColumnInfo(name = "isRevealed")
    var isRevealed: Boolean = false,

    @ColumnInfo(name = "numberOfBombsInBounds")
    var numberOfBombsInBounds: Int = 0
)