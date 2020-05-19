package com.jorgeav.buscaminas.ui.newboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jorgeav.buscaminas.MyApplication
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.databinding.NewBoardFragmentBinding
import javax.inject.Inject

class NewBoardFragment : Fragment() {

    @Inject lateinit var mViewModelFactory: NewBoardViewModel.Factory

    private lateinit var binding : NewBoardFragmentBinding
    private lateinit var viewModel: NewBoardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.new_board_fragment, container, false)

//        val viewModelFactory = NewBoardViewModel.Factory(
//            (activity as MainActivity).createNewBoardUseCase,
//            (activity as MainActivity).getCellsBySideUseCase,
//            (activity as MainActivity).getBombsInBoardUseCase,
//            (activity as MainActivity).getCellsBySideRangeUseCase,
//            (activity as MainActivity).getBombsRangeInBoardUseCase)
        viewModel = ViewModelProvider(this, mViewModelFactory).get(NewBoardViewModel::class.java)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MyApplication).appComponent.inject(this)
    }

    private fun enabledCellsButtons(cells: Int) {
        when {
            cells <= viewModel.minCellsBySide -> {
                binding.lessRowsButton.isEnabled = false
                binding.moreRowsButton.isEnabled = true
            }
            cells >= viewModel.maxCellsBySide -> {
                binding.lessRowsButton.isEnabled = true
                binding.moreRowsButton.isEnabled = false
            }
            else -> {
                binding.lessRowsButton.isEnabled = true
                binding.moreRowsButton.isEnabled = true
            }
        }
    }

    private fun enabledBombsButtons(bombs: Int) {
        when {
            bombs <= viewModel.minBombs -> {
                binding.lessColumnsButton.isEnabled = false
                binding.moreColumnsButton.isEnabled = true
            }
            bombs >= viewModel.maxBombs -> {
                binding.lessColumnsButton.isEnabled = true
                binding.moreColumnsButton.isEnabled = false
            }
            else -> {
                binding.lessColumnsButton.isEnabled = true
                binding.moreColumnsButton.isEnabled = true
            }
        }

    }
}
