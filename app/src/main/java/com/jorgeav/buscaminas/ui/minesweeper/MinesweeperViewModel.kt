package com.jorgeav.buscaminas.ui.minesweeper

import android.os.SystemClock
import androidx.lifecycle.*
import com.jorgeav.buscaminas.domain.Cell
import com.jorgeav.buscaminas.usecases.*
import kotlinx.coroutines.launch

class MinesweeperViewModel(private val loadBoardUseCase: LoadBoardUseCase,
                           private val changeMarkInCellUseCase: ChangeMarkInCellUseCase,
                           private val showCellUseCase: ShowCellUseCase,
                           private val getElapsedMillisInBoardUseCase: GetElapsedMillisInBoardUseCase,
                           private val setElapsedMillisInBoardUseCase: SetElapsedMillisInBoardUseCase,
                           private val getCellsBySideUseCase: GetCellsBySideUseCase,
                           private val getBombsInBoardUseCase: GetBombsInBoardUseCase,
                           private val countMarksUseCase: CountMarksUseCase,
                           private val checkBoardWinOrLoseUseCase: CheckBoardWinOrLoseUseCase) : ViewModel() {
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

    // ViewModel use this to warn View that Chrono should start, but must be in onResume
    private val _startChronoEvent = MutableLiveData<Boolean>()
    val startChronoEvent : LiveData<Boolean>
        get() = _startChronoEvent

    // ViewModel use this to warn View that it should save chrono time. True when game ends.
    // Important: it's an event, not a state (when ends, not while is ended).
    private val _setChronoTimeEvent = MutableLiveData<Boolean>()
    val setChronoTimeEvent : LiveData<Boolean>
        get() = _setChronoTimeEvent

    init {
        viewModelScope.launch {
            gameFinishedEvent(loadBoardDataAndCheckWinOrLose())
        }
        _newBoardButtonEvent.value = false
        _gameFinishedState.value = false
        _gameWonEvent.value = false
        _gameLoseEvent.value = false
        _startChronoEvent.value = false
        _setChronoTimeEvent.value = false
    }

    fun refreshBoard() {
        viewModelScope.launch {
            _gameFinishedState.value = loadBoardDataAndCheckWinOrLose() != null
            if (_gameFinishedState.value!!.not()) _startChronoEvent.value = true
        }
    }

    private suspend fun updateBoardDataAndLaunchFinishEvents() {
        if (gameFinishedEvent(loadBoardDataAndCheckWinOrLose()))
            _setChronoTimeEvent.value = true
    }

    private suspend fun loadBoardDataAndCheckWinOrLose() : Boolean?  {
        _cells.value = loadBoardUseCase()
        val bombs = getBombsInBoardUseCase()
        val marks = countMarksUseCase(_cells.value)
        _bombsLeft.value = (bombs?:0) - (marks?:0)

        return checkBoardWinOrLoseUseCase(_cells.value)
    }

    private fun gameFinishedEvent(isBoardWinOrLose: Boolean?) : Boolean {
        if (isBoardWinOrLose != null) {
            _gameFinishedState.value = true

            if (isBoardWinOrLose) {
                // todo mark bombs
                _gameWonEvent.value = true
            } else {
                // todo reveal bombs
                _gameLoseEvent.value = true
            }
        }
        return (isBoardWinOrLose != null)
    }

    fun cellGridClicked(cell: Cell) {
        if (_gameFinishedState.value!!.not() && !cell.isShowing) {
                viewModelScope.launch {
                    showCellUseCase.invoke(cell, _cells.value)
                    updateBoardDataAndLaunchFinishEvents()
                }
            }
    }

    fun cellGridLongClicked(cell: Cell) : Boolean {
        if (_gameFinishedState.value!!.not() && !cell.isShowing) {
            viewModelScope.launch {
                changeMarkInCellUseCase(cell)
                updateBoardDataAndLaunchFinishEvents()
            }
            return true
        }
        return false
    }

    fun getCellsBySide() : Int =
        getCellsBySideUseCase()

    fun getBaseForChronometer() : Long =
        SystemClock.elapsedRealtime() - getElapsedMillisInBoardUseCase()

    fun setElapsedMillisInBoardSinceStarted(lastBaseTime: Long) =
        setElapsedMillisInBoardUseCase(SystemClock.elapsedRealtime() - lastBaseTime)

    fun onNewBoardClicked() {
        _cells.value = null
        _newBoardButtonEvent.value = true
    }

    fun onNewBoardButtonEventConsumed() { _newBoardButtonEvent.value = false }
    fun gameWonEventConsumed() { _gameWonEvent.value = false }
    fun gameLoseEventConsumed() { _gameLoseEvent.value = false }
    fun startChronoEventConsumed() { _startChronoEvent.value = false }
    fun setChronoTimeEventConsumed() { _setChronoTimeEvent.value = false }

    class Factory (private val loadBoardUseCase: LoadBoardUseCase,
                   private val changeMarkInCellUseCase: ChangeMarkInCellUseCase,
                   private val showCellUseCase: ShowCellUseCase,
                   private val getElapsedMillisInBoardUseCase: GetElapsedMillisInBoardUseCase,
                   private val setElapsedMillisInBoardUseCase: SetElapsedMillisInBoardUseCase,
                   private val getCellsBySideUseCase: GetCellsBySideUseCase,
                   private val getBombsInBoardUseCase: GetBombsInBoardUseCase,
                   private val countMarksUseCase: CountMarksUseCase,
                   private val checkBoardWinOrLoseUseCase: CheckBoardWinOrLoseUseCase) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MinesweeperViewModel::class.java)) {
                return MinesweeperViewModel(
                    loadBoardUseCase,
                    changeMarkInCellUseCase,
                    showCellUseCase,
                    getElapsedMillisInBoardUseCase,
                    setElapsedMillisInBoardUseCase,
                    getCellsBySideUseCase,
                    getBombsInBoardUseCase,
                    countMarksUseCase,
                    checkBoardWinOrLoseUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
