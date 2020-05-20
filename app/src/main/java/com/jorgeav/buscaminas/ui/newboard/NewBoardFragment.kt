package com.jorgeav.buscaminas.ui.newboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jorgeav.buscaminas.MainActivity
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.databinding.NewBoardFragmentBinding

class NewBoardFragment : Fragment() {

    private lateinit var binding : NewBoardFragmentBinding
    private lateinit var viewModel: NewBoardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.new_board_fragment, container, false)

        val viewModelFactory = NewBoardViewModel.Factory(
            (activity as MainActivity).createNewBoardUseCase,
            (activity as MainActivity).getCellsBySideUseCase,
            (activity as MainActivity).getBombsInBoardUseCase,
            (activity as MainActivity).getCellsBySideRangeUseCase,
            (activity as MainActivity).getBombsRangeInBoardUseCase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewBoardViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.cellsBySide.observe(viewLifecycleOwner, Observer { cellsBySide ->
            cellsBySide?.let {
                enabledCellsButtons(cellsBySide)
                enabledBombsButtons(viewModel.bombs.value?: viewModel.minBombs)
            }
        })
        viewModel.bombs.observe(viewLifecycleOwner, Observer { bombs ->
            bombs?.let {
                enabledBombsButtons(bombs)
            }
        })
        viewModel.finishGenerateBoardEvent.observe(viewLifecycleOwner, Observer { state ->
            if (state) {
                viewModel.finishGenerateBoardStateConsumed()
                findNavController().popBackStack()
            }
        })

        return binding.root
    }

    private fun enabledCellsButtons(cells: Int) {
        when {
            cells <= viewModel.minCellsBySide -> {
                binding.lessCellsButton.isEnabled = false
                binding.moreCellsButton.isEnabled = true
            }
            cells >= viewModel.maxCellsBySide -> {
                binding.lessCellsButton.isEnabled = true
                binding.moreCellsButton.isEnabled = false
            }
            else -> {
                binding.lessCellsButton.isEnabled = true
                binding.moreCellsButton.isEnabled = true
            }
        }
    }

    private fun enabledBombsButtons(bombs: Int) {
        when {
            bombs <= viewModel.minBombs -> {
                binding.lessBombsButton.isEnabled = false
                binding.moreBombsButton.isEnabled = true
            }
            bombs >= viewModel.maxBombs -> {
                binding.lessBombsButton.isEnabled = true
                binding.moreBombsButton.isEnabled = false
            }
            else -> {
                binding.lessBombsButton.isEnabled = true
                binding.moreBombsButton.isEnabled = true
            }
        }

    }
}
