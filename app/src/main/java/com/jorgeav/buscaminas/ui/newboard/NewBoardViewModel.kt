package com.jorgeav.buscaminas.ui.newboard

import androidx.lifecycle.*
import com.jorgeav.buscaminas.usecases.*
import kotlinx.coroutines.launch

class NewBoardViewModel(private val createNewBoardUseCase: CreateNewBoardUseCase,
                        private val getCellsBySideUseCase: GetCellsBySideUseCase,
                        private val getBombsInBoardUseCase: GetBombsInBoardUseCase,
                        private val getCellsBySideRangeUseCase: GetCellsBySideRangeUseCase,
                        private val getBombsRangeInBoardUseCase: GetBombsRangeInBoardUseCase ) : ViewModel() {
    private val _cellsBySide = MutableLiveData<Int>()
    val cellsBySide : LiveData<Int>
        get() = _cellsBySide

    private val _bombs = MutableLiveData<Int>()
    val bombs : LiveData<Int>
        get() = _bombs

    private val _finishGenerateBoardEvent = MutableLiveData<Boolean>()
    val finishGenerateBoardEvent : LiveData<Boolean>
        get() = _finishGenerateBoardEvent

    val minCellsBySide : Int = getCellsBySideRangeUseCase().first
    val maxCellsBySide : Int = getCellsBySideRangeUseCase().second
    var minBombs : Int = getBombsRangeInBoardUseCase(minCellsBySide).first
    var maxBombs : Int = getBombsRangeInBoardUseCase(minCellsBySide).second

    init {
        setCellsBySideSelected(getCellsBySideUseCase())
        setBombs(getBombsInBoardUseCase())
        _finishGenerateBoardEvent.value = false
    }

    private fun setCellsBySideSelected(cellsBySide: Int) {
        val value =
            if (cellsBySide in minCellsBySide..maxCellsBySide) cellsBySide
            else minCellsBySide

        _cellsBySide.value = value
        updateBombsRange(value)
    }

    private fun updateBombsRange(cellsBySide : Int) {
        minBombs = getBombsRangeInBoardUseCase(cellsBySide).first
        maxBombs = getBombsRangeInBoardUseCase(cellsBySide).second

        val bombsNow = _bombs.value
        setBombs(bombsNow)
    }

    private fun setBombs(bombs : Int?) {
        if (bombs == null) _bombs.value = minBombs
        else when {
                bombs < minBombs -> _bombs.value = minBombs
                bombs > maxBombs -> _bombs.value = maxBombs
                else -> _bombs.value = bombs
            }
    }

    fun onLessCellsClicked() {
        val cells : Int = _cellsBySide.value ?: minCellsBySide + 1
        setCellsBySideSelected( if (cells > minCellsBySide) cells.minus(1) else cells )
    }

    fun onMoreCellsClicked() {
        val cells : Int = _cellsBySide.value ?: maxCellsBySide - 1
        setCellsBySideSelected( if (cells < maxCellsBySide) cells.plus(1) else cells )
    }

    fun onLessBombsClicked() {
        val bombs : Int = _bombs.value ?: minBombs + 1
        setBombs( if (bombs > minBombs) bombs.minus(1) else bombs )
    }

    fun onMoreBombsClicked() {
        val bombs : Int = _bombs.value ?: maxBombs - 1
        setBombs( if (bombs < maxBombs) bombs.plus(1) else bombs )
    }

    fun onGenerateBoardClicked() {
        viewModelScope.launch {
            val cellsBySide = _cellsBySide.value ?: minCellsBySide
            val bombs = _bombs.value ?: cellsBySide //Use cells by side as default bombs value
            createNewBoardUseCase(cellsBySide, bombs)

            _finishGenerateBoardEvent.value = true
        }
    }

    fun finishGenerateBoardStateConsumed() {
        _finishGenerateBoardEvent.value = false
    }

    class Factory (private val createNewBoardUseCase: CreateNewBoardUseCase,
                   private val getCellsBySideUseCase: GetCellsBySideUseCase,
                   private val getBombsInBoardUseCase: GetBombsInBoardUseCase,
                   private val getCellsBySideRangeUseCase: GetCellsBySideRangeUseCase,
                   private val getBombsRangeInBoardUseCase: GetBombsRangeInBoardUseCase) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewBoardViewModel::class.java)) {
                return NewBoardViewModel(
                    createNewBoardUseCase,
                    getCellsBySideUseCase,
                    getBombsInBoardUseCase,
                    getCellsBySideRangeUseCase,
                    getBombsRangeInBoardUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
