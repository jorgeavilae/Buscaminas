package com.jorgeav.buscaminas.ui.main

import androidx.lifecycle.*
import com.jorgeav.buscaminas.usecases.CreateNewBoardUseCase
import kotlinx.coroutines.launch

const val MIN_ROWS = 4
const val MAX_ROWS = 20
const val MIN_COLUMNS = 4
const val MAX_COLUMNS = 20

class MainViewModel(private val createNewBoardUseCase: CreateNewBoardUseCase) : ViewModel() {
    private val _rows = MutableLiveData<Int>()
    val rows : LiveData<Int>
        get() = _rows

    private val _columns = MutableLiveData<Int>()
    val columns : LiveData<Int>
        get() = _columns

    private val _navigateToBoardFragmentState = MutableLiveData<Boolean>()
    val navigateToBoardFragmentState : LiveData<Boolean>
        get() = _navigateToBoardFragmentState

    init {
        _rows.value = MIN_ROWS
        _columns.value = MIN_COLUMNS
        _navigateToBoardFragmentState.value = false
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

    fun onGenerateBoardClicked() {
        viewModelScope.launch {
            val rows = _rows.value ?: MIN_ROWS
            val columns = _columns.value ?: MIN_COLUMNS
            val bombs = ((rows * columns) / 4).toInt()
            createNewBoardUseCase(rows, columns, bombs)
        }

        _navigateToBoardFragmentState.value = true
    }

    fun navigateToBoardFragmentConsumed() {
        _navigateToBoardFragmentState.value = false
    }

    class Factory (private val createNewBoardUseCase: CreateNewBoardUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(createNewBoardUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
