package com.jorgeav.buscaminas.ui.minesweeper

import androidx.lifecycle.*
import com.jorgeav.buscaminas.domain.BoardUtils
import com.jorgeav.buscaminas.domain.Cell
import com.jorgeav.buscaminas.usecases.ChangeMarkInCellUseCase
import com.jorgeav.buscaminas.usecases.LoadBoardUseCase
import com.jorgeav.buscaminas.usecases.ShowCellUseCase
import kotlinx.coroutines.launch

class MinesweeperViewModel(private val loadBoardUseCase: LoadBoardUseCase,
                           private val changeMarkInCellUseCase: ChangeMarkInCellUseCase,
                           private val showCellUseCase: ShowCellUseCase) : ViewModel() {
    private val _cells = MutableLiveData<Map<Pair<Int, Int>, Cell>>()
    val cells : LiveData<Map<Pair<Int, Int>, Cell>>
        get() = _cells

    private val _newBoardButtonState = MutableLiveData<Boolean>()
    val newBoardButtonState : LiveData<Boolean>
        get() = _newBoardButtonState

    init {
        refreshCellsData()
        _newBoardButtonState.value = false
    }

    fun refreshCellsData() {
        viewModelScope.launch {
            _cells.value = loadBoardUseCase()
        }
    }

    fun cellGridClicked(cell: Cell) {
        if (!cell.isShowing) {
            viewModelScope.launch {
                showCellUseCase(BoardUtils.cellsToFlipInBoard(cell, _cells.value))
                _cells.value = loadBoardUseCase()
            }
        }
    }

    fun cellGridLongClicked(cell: Cell) {
        viewModelScope.launch {
            changeMarkInCellUseCase(cell)
            _cells.value = loadBoardUseCase()
        }
    }

    fun onNewBoardClicked() {
        _cells.value = null
        _newBoardButtonState.value = true
    }
    fun onNewBoardButtonStateConsumed() {
        _newBoardButtonState.value = false
    }

    class Factory (private val loadBoardUseCase: LoadBoardUseCase,
                   private val changeMarkInCellUseCase: ChangeMarkInCellUseCase,
                   private val showCellUseCase: ShowCellUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MinesweeperViewModel::class.java)) {
                return MinesweeperViewModel(
                    loadBoardUseCase,
                    changeMarkInCellUseCase,
                    showCellUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
