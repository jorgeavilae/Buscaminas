package com.jorgeav.buscaminas.ui.main

import androidx.lifecycle.*
import com.jorgeav.buscaminas.usecases.CreateNewBoardUseCase
import kotlinx.coroutines.launch

const val MIN_CELLS = 8
const val MAX_CELLS = 12

class MainViewModel(private val createNewBoardUseCase: CreateNewBoardUseCase) : ViewModel() {
    private val _cellsBySide = MutableLiveData<Int>()
    val cellsBySide : LiveData<Int>
        get() = _cellsBySide

    private val _bombs = MutableLiveData<Int>()
    val bombs : LiveData<Int>
        get() = _bombs

    var minBombs : Int = MIN_CELLS
    var maxBombs : Int = MAX_CELLS

    private val _navigateToBoardFragmentState = MutableLiveData<Boolean>()
    val navigateToBoardFragmentState : LiveData<Boolean>
        get() = _navigateToBoardFragmentState

    init {
        updateCellsSelected(MIN_CELLS)
        _bombs.value = minBombs
        _navigateToBoardFragmentState.value = false
    }

    private fun updateCellsSelected(value : Int) {
        _cellsBySide.value = value
        minBombs = (value*value * 0.1).toInt()
        maxBombs = (value*value * 0.5).toInt()

        updateBombsNumber()
    }

    private fun updateBombsNumber() {
        val bombsNow = _bombs.value
        if (bombsNow == null) _bombs.value = minBombs
        else {
            if (bombsNow < minBombs) _bombs.value = minBombs
            else if (bombsNow > maxBombs) _bombs.value = maxBombs
        }
    }

    fun onLessCellsClicked() {
        val cells : Int = _cellsBySide.value ?: MIN_CELLS + 1
        updateCellsSelected( if (cells > MIN_CELLS) cells.minus(1) else cells )
    }

    fun onMoreCellsClicked() {
        val cells : Int = _cellsBySide.value ?: MAX_CELLS - 1
        updateCellsSelected( if (cells < MAX_CELLS) cells.plus(1) else cells )
    }

    fun onLessBombsClicked() {
        val minBombs : Int = minBombs ?: MIN_CELLS
        val bombs : Int = _bombs.value ?: minBombs + 1
        _bombs.value = if (bombs > minBombs) bombs.minus(1) else bombs
    }

    fun onMoreBombsClicked() {
        val maxBombs : Int = maxBombs ?: MIN_CELLS
        val bombs : Int = _bombs.value ?: maxBombs - 1
        _bombs.value = if (bombs < maxBombs) bombs.plus(1) else bombs
    }

    fun onGenerateBoardClicked() {
        viewModelScope.launch {
            val cellsBySide = _cellsBySide.value ?: MIN_CELLS
            val bombs = _bombs.value ?: cellsBySide //Use cells by side as default bombs value
            createNewBoardUseCase(cellsBySide, bombs)

            _navigateToBoardFragmentState.value = true
        }
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
