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

    private val _newBoardButtonEvent = MutableLiveData<Boolean>()
    val newBoardButtonEvent : LiveData<Boolean>
        get() = _newBoardButtonEvent

    private val _gameFinishedState = MutableLiveData<Boolean>()
    val gameFinishedState : LiveData<Boolean>
        get() = _gameFinishedState

    private val _gameWonEvent = MutableLiveData<Boolean>()
    val gameWonEvent : LiveData<Boolean>
        get() = _gameWonEvent

    private val _gameLoseEvent = MutableLiveData<Boolean>()
    val gameLoseEvent : LiveData<Boolean>
        get() = _gameLoseEvent

    init {
        updateBoardDataAndLaunchFinishEvents()
        _newBoardButtonEvent.value = false
        _gameFinishedState.value = false
        _gameWonEvent.value = false
        _gameLoseEvent.value = false
    }

    fun refreshBoard() {
        viewModelScope.launch {
            _cells.value = loadBoardUseCase()
            val bombs = BoardUtils.getBombs(_cells.value)
            val marks = BoardUtils.getMarks(_cells.value)
            _bombsLeft.value = (bombs?:0) - (marks?:0)

            val isBoardWinOrLose = BoardUtils.isBoardWinOrLose(_cells.value)
            _gameFinishedState.value = isBoardWinOrLose != null
        }
    }

    private fun updateBoardDataAndLaunchFinishEvents() {
        viewModelScope.launch {
            _cells.value = loadBoardUseCase()
            val bombs = BoardUtils.getBombs(_cells.value)
            val marks = BoardUtils.getMarks(_cells.value)
            _bombsLeft.value = (bombs?:0) - (marks?:0)

            val isBoardWinOrLose = BoardUtils.isBoardWinOrLose(_cells.value)
            if (isBoardWinOrLose != null) gameFinished(isBoardWinOrLose)
        }
    }

    private fun gameFinished(isWon: Boolean) {
        _gameFinishedState.value = true

        if (isWon) _gameWonEvent.value = true
        else _gameLoseEvent.value = true
    }

    fun cellGridClicked(cell: Cell) {
        if (gameFinishedState.value!!.not() && !cell.isShowing) {
                viewModelScope.launch {
                    showCellUseCase(BoardUtils.cellsToFlipInBoard(cell, _cells.value))
                    updateBoardDataAndLaunchFinishEvents()
                }
            }
    }

    fun cellGridLongClicked(cell: Cell) : Boolean {
        if (gameFinishedState.value!!.not() && !cell.isShowing) {
            viewModelScope.launch {
                changeMarkInCellUseCase(cell)
                updateBoardDataAndLaunchFinishEvents()
            }
            return true
        }
        return false
    }

    fun getCellsBySide() : Int = getCellsBySideUseCase()

    fun onNewBoardClicked() {
        _cells.value = null
        _newBoardButtonEvent.value = true
    }
    fun onNewBoardButtonEventConsumed() {
        _newBoardButtonEvent.value = false
    }
    fun gameWonEventConsumed() {
        _gameWonEvent.value = false
    }
    fun gameLoseEventConsumed() {
        _gameLoseEvent.value = false
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
