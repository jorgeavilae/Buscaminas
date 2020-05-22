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

package com.jorgeav.buscaminas.ui.minesweeper

import android.os.SystemClock
import androidx.lifecycle.*
import com.jorgeav.buscaminas.domain.Cell
import com.jorgeav.buscaminas.usecases.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MinesweeperViewModel(private val loadBoardUseCase: LoadBoardUseCase,
                           private val changeMarkInCellUseCase: ChangeMarkInCellUseCase,
                           private val showCellUseCase: ShowCellUseCase,
                           private val getElapsedMillisInBoardUseCase: GetElapsedMillisInBoardUseCase,
                           private val setElapsedMillisInBoardUseCase: SetElapsedMillisInBoardUseCase,
                           private val getCellsBySideUseCase: GetCellsBySideUseCase,
                           private val getBombsInBoardUseCase: GetBombsInBoardUseCase,
                           private val countMarksUseCase: CountMarksUseCase,
                           private val checkBoardWinOrLoseUseCase: CheckBoardWinOrLoseUseCase,
                           private val markCellsWithBombUseCase: MarkCellsWithBombUseCase,
                           private val revealBombsUseCase: RevealBombsUseCase)
    : ViewModel() {

    private var initViewModelJob : Job? = null

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
        initViewModelJob = viewModelScope.launch {
            loadBoardData()
            gameFinishedEvent(checkBoardWinOrLoseUseCase(_cells.value))
        }
        _newBoardButtonEvent.value = false
        _gameFinishedState.value = false
        _gameWonEvent.value = false
        _gameLoseEvent.value = false
        _startChronoEvent.value = false
        _setChronoTimeEvent.value = false
    }

    suspend fun refreshBoard() {
        loadBoardData()
        _gameFinishedState.value = checkBoardWinOrLoseUseCase(_cells.value) != null
        if (_gameFinishedState.value!!.not()) _startChronoEvent.value = true
    }

    private suspend fun updateBoardDataAndLaunchFinishEvents() {
        loadBoardData()
        if (gameFinishedEvent(checkBoardWinOrLoseUseCase(_cells.value)))
            _setChronoTimeEvent.value = true
    }

    private suspend fun loadBoardData()  {
        _cells.value = loadBoardUseCase()
        val bombs = getBombsInBoardUseCase()
        val marks = countMarksUseCase(_cells.value)
        _bombsLeft.value = bombs - (marks?:0)
    }

    private suspend fun gameFinishedEvent(isBoardWinOrLose: Boolean?) : Boolean {
        if (isBoardWinOrLose != null) {
            _gameFinishedState.value = true

            if (isBoardWinOrLose) {
                markCellsWithBombUseCase()
                loadBoardData()
                _gameWonEvent.value = true
            } else {
                revealBombsUseCase()
                loadBoardData()
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
        initViewModelJob?.cancel()
        _newBoardButtonEvent.value = true
    }

    fun onNewBoardButtonEventConsumed() { _newBoardButtonEvent.value = false }
    fun gameWonEventConsumed() { _gameWonEvent.value = false }
    fun gameLoseEventConsumed() { _gameLoseEvent.value = false }
    fun startChronoEventConsumed() { _startChronoEvent.value = false }
    fun setChronoTimeEventConsumed() { _setChronoTimeEvent.value = false }

    class Factory @Inject constructor(
        private val loadBoardUseCase: LoadBoardUseCase,
        private val changeMarkInCellUseCase: ChangeMarkInCellUseCase,
        private val showCellUseCase: ShowCellUseCase,
        private val getElapsedMillisInBoardUseCase: GetElapsedMillisInBoardUseCase,
        private val setElapsedMillisInBoardUseCase: SetElapsedMillisInBoardUseCase,
        private val getCellsBySideUseCase: GetCellsBySideUseCase,
        private val getBombsInBoardUseCase: GetBombsInBoardUseCase,
        private val countMarksUseCase: CountMarksUseCase,
        private val checkBoardWinOrLoseUseCase: CheckBoardWinOrLoseUseCase,
        private val markCellsWithBombUseCase: MarkCellsWithBombUseCase,
        private val revealBombsUseCase: RevealBombsUseCase
    ) : ViewModelProvider.Factory {

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
                    checkBoardWinOrLoseUseCase,
                    markCellsWithBombUseCase,
                    revealBombsUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
