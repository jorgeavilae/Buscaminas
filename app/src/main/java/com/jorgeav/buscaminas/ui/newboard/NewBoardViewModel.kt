package com.jorgeav.buscaminas.ui.newboard

import androidx.lifecycle.*
import com.jorgeav.buscaminas.usecases.CreateNewBoardUseCase
import com.jorgeav.buscaminas.usecases.GetBombsInBoardUseCase
import com.jorgeav.buscaminas.usecases.GetCellsBySideUseCase
import kotlinx.coroutines.launch

const val MIN_CELLS = 8
const val MAX_CELLS = 12

class NewBoardViewModel(private val createNewBoardUseCase: CreateNewBoardUseCase,
                        private val getCellsBySideUseCase: GetCellsBySideUseCase,
                        private val getBombsInBoardUseCase: GetBombsInBoardUseCase) : ViewModel() {
    private val _cellsBySide = MutableLiveData<Int>()
    val cellsBySide : LiveData<Int>
        get() = _cellsBySide

    private val _bombs = MutableLiveData<Int>()
    val bombs : LiveData<Int>
        get() = _bombs

    var minBombs : Int = MIN_CELLS
    var maxBombs : Int = MAX_CELLS

    private val _finishGenerateBoardState = MutableLiveData<Boolean>()
    val finishGenerateBoardState : LiveData<Boolean>
        get() = _finishGenerateBoardState

    init {
        updateCellsSelected(getBombsInBoardUseCase())
        _bombs.value = getBombsInBoardUseCase()
        _finishGenerateBoardState.value = false
    }

    private fun updateCellsSelected(value : Int) {
        if (value in MIN_CELLS..MAX_CELLS)
            _cellsBySide.value = value
        else
            _cellsBySide.value = MIN_CELLS

        val c = _cellsBySide.value?: MIN_CELLS
        updateBombsRange(c)
    }

    private fun updateBombsRange(cellsBySide : Int) {
        minBombs = (cellsBySide*cellsBySide * 0.1).toInt()
        maxBombs = (cellsBySide*cellsBySide * 0.5).toInt()

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

            _finishGenerateBoardState.value = true
        }
    }

    fun finishGenerateBoardStateConsumed() {
        _finishGenerateBoardState.value = false
    }

    class Factory (private val createNewBoardUseCase: CreateNewBoardUseCase,
                   private val getCellsBySideUseCase: GetCellsBySideUseCase,
                   private val getBombsInBoardUseCase: GetBombsInBoardUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewBoardViewModel::class.java)) {
                return NewBoardViewModel(createNewBoardUseCase,
                getCellsBySideUseCase, getBombsInBoardUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
