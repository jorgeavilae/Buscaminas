package com.jorgeav.buscaminas.ui.minesweeper

import androidx.lifecycle.*
import com.jorgeav.buscaminas.domain.Cell
import com.jorgeav.buscaminas.usecases.LoadBoardUseCase
import kotlinx.coroutines.launch

class MinesweeperViewModel(private val loadBoardUseCase: LoadBoardUseCase) : ViewModel() {
    private val _cells = MutableLiveData<Map<Pair<Int, Int>, Cell>>()
    val cells : LiveData<Map<Pair<Int, Int>, Cell>>
        get() = _cells

    init {
        loadCellsData()
    }

    private fun loadCellsData() {
        viewModelScope.launch {
            _cells.value = loadBoardUseCase()
        }
    }

    class Factory (private val loadBoardUseCase: LoadBoardUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MinesweeperViewModel::class.java)) {
                return MinesweeperViewModel(loadBoardUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
