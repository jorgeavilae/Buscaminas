package com.jorgeav.buscaminas.data

/**
 * Created by Jorge Avila on 11/05/2020.
 */
interface IKeyValueDataSource {
    fun getElapsedMillis() : Long
    fun setElapsedMillis(millis : Long)

    fun getCellsBySide() : Int
    fun setCellsBySide(cellsBySide : Int)

    fun getBombs() : Int
    fun setBombs(bombs : Int)
}