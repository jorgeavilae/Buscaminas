package com.jorgeav.buscaminas.ui.minesweeper

import androidx.lifecycle.*
import com.jorgeav.buscaminas.domain.BoardUtils
import com.jorgeav.buscaminas.domain.Cell
import com.jorgeav.buscaminas.usecases.*
import kotlinx.coroutines.launch

class MinesweeperViewModel(private val loadBoardUseCase: LoadBoardUseCase,
                           private val changeMarkInCellUseCase: ChangeMarkInCellUseCase,
                           private val showCellUseCase: ShowCellUseCase,
                           private val getElapsedMillisInBoardUseCase: GetElapsedMillisInBoardUseCase,
                           private val setElapsedMillisInBoardUseCase: SetElapsedMillisInBoardUseCase,
                           private val getCellsBySideUseCase: GetCellsBySideUseCase) : ViewModel() {
    private val _cells = MutableLiveData<Map<Pair<Int, Int>, Cell>>()
    val cells : LiveData<Map<Pair<Int, Int>, Cell>>
        get() = _cells

    private val _bombsLeft = MutableLiveData<Int>()
    val bombsLeft : LiveData<Int>
        get() = _bombsLeft

    private val _isGameWinOrLose = MutableLiveData<Boolean?>()
    val isGameWinOrLose : LiveData<Boolean?>
        get() = _isGameWinOrLose

    private val _newBoardButtonState = MutableLiveData<Boolean>()
    val newBoardButtonState : LiveData<Boolean>
        get() = _newBoardButtonState

    init {
        updateCellsData()
        _newBoardButtonState.value = false
    }

    fun loadCellsData() {
        viewModelScope.launch {
            _cells.value = loadBoardUseCase()
            val bombs = BoardUtils.getBombs(_cells.value)
            val marks = BoardUtils.getMarks(_cells.value)
            _bombsLeft.value = (bombs?:0) - (marks?:0)
        }
    }

    private fun updateCellsData() {
        viewModelScope.launch {
            _cells.value = loadBoardUseCase()
            val bombs = BoardUtils.getBombs(_cells.value)
            val marks = BoardUtils.getMarks(_cells.value)
            _bombsLeft.value = (bombs?:0) - (marks?:0)

            _isGameWinOrLose.value = BoardUtils.isBoardWinOrLose(_cells.value)
        }
    }

    fun cellGridClicked(cell: Cell) {
        if (isGameWinOrLose.value == null && !cell.isShowing) {
                viewModelScope.launch {
                    showCellUseCase(BoardUtils.cellsToFlipInBoard(cell, _cells.value))
                    updateCellsData()
                }
            }
    }

    fun cellGridLongClicked(cell: Cell) : Boolean {
        if (isGameWinOrLose.value == null && !cell.isShowing) {
            viewModelScope.launch {
                changeMarkInCellUseCase(cell)
                updateCellsData()
            }
            return true
        }
        return false
    }

    fun getCellsBySide() : Int = getCellsBySideUseCase()

    fun onNewBoardClicked() {
        _cells.value = null
        _newBoardButtonState.value = true
    }
    fun onNewBoardButtonStateConsumed() {
        _newBoardButtonState.value = false
    }
    fun isGameWinOrLoseConsumed() {
        _isGameWinOrLose.value = null
    }

    class Factory (private val loadBoardUseCase: LoadBoardUseCase,
                   private val changeMarkInCellUseCase: ChangeMarkInCellUseCase,
                   private val showCellUseCase: ShowCellUseCase,
                   private val getElapsedMillisInBoardUseCase: GetElapsedMillisInBoardUseCase,
                   private val setElapsedMillisInBoardUseCase: SetElapsedMillisInBoardUseCase,
                   private val getCellsBySideUseCase: GetCellsBySideUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MinesweeperViewModel::class.java)) {
                return MinesweeperViewModel(
                    loadBoardUseCase,
                    changeMarkInCellUseCase,
                    showCellUseCase,
                    getElapsedMillisInBoardUseCase,
                    setElapsedMillisInBoardUseCase,
                    getCellsBySideUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
