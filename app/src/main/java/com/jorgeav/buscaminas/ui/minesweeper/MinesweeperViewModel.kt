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

    private val _chronoRunning = MutableLiveData<Boolean>()
    val chronoRunning : LiveData<Boolean>
        get() = _chronoRunning

    private val _bombs = MutableLiveData<Int>()
    val bombs : LiveData<Int>
        get() = _bombs

    private val _marks = MutableLiveData<Int>()
    val marks : LiveData<Int>
        get() = _marks

    init {
        loadCellsData()
        _newBoardButtonState.value = false
    }

    fun loadCellsData() {
        viewModelScope.launch {
            _cells.value = loadBoardUseCase()
            _bombs.value = BoardUtils.getBombs(_cells.value)
            _marks.value = BoardUtils.getMarks(_cells.value)
            _chronoRunning.value = true
        }
    }

    fun updateCellsData() {
        viewModelScope.launch {
            _cells.value = loadBoardUseCase()
            _marks.value = BoardUtils.getMarks(_cells.value)
        }
    }

    fun cellGridClicked(cell: Cell) {
        if (!cell.isShowing) {
            viewModelScope.launch {
                showCellUseCase(BoardUtils.cellsToFlipInBoard(cell, _cells.value))
                updateCellsData()
            }
        }
    }

    fun cellGridLongClicked(cell: Cell) {
        viewModelScope.launch {
            changeMarkInCellUseCase(cell)
            updateCellsData()
        }
    }

    fun onNewBoardClicked() {
        _cells.value = null
        _chronoRunning.value = false
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
