package com.jorgeav.buscaminas.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val MIN_ROWS = 4
const val MAX_ROWS = 20
const val MIN_COLUMNS = 4
const val MAX_COLUMNS = 20

class MainViewModel : ViewModel() {
    private val _rows = MutableLiveData<Int>()
    val rows : LiveData<Int>
        get() = _rows

    private val _columns = MutableLiveData<Int>()
    val columns : LiveData<Int>
        get() = _columns

    private val _generateButtonClicked = MutableLiveData<Boolean>()
    val generateButtonClicked : LiveData<Boolean>
        get() = _generateButtonClicked

    init {
        _rows.value = MIN_ROWS
        _columns.value = MIN_COLUMNS
        _generateButtonClicked.value = false
    }

    fun onLessRowsClicked() {
        val rows : Int = _rows.value ?: MIN_ROWS + 1
        _rows.value = if (rows > MIN_ROWS) rows.minus(1) else rows
    }

    fun onMoreRowsClicked() {
        val rows : Int = _rows.value ?: MIN_ROWS - 1
        _rows.value = if (rows < MAX_ROWS) rows.plus(1) else rows
    }

    fun onLessColumnsClicked() {
        val columns : Int = _columns.value ?: MIN_COLUMNS + 1
        _columns.value = if (columns > MIN_COLUMNS) columns.minus(1) else columns
    }

    fun onMoreColumnsClicked() {
        val columns : Int = _columns.value ?: MIN_COLUMNS - 1
        _columns.value = if (columns < MAX_COLUMNS) columns.plus(1) else columns
    }

    fun onGenerateButtonClicked() {
        _generateButtonClicked.value = true
    }

    fun generateButtonClickedConsumed() {
        _generateButtonClicked.value = false
    }
}
