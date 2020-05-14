package com.jorgeav.buscaminas.ui.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jorgeav.buscaminas.MainActivity
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.databinding.MinesweeperFragmentBinding
import com.jorgeav.buscaminas.domain.Cell
import com.jorgeav.buscaminas.ui.minesweeper.board.CellItemClickListener
import com.jorgeav.buscaminas.ui.minesweeper.board.CellsBoardAdapter
import com.jorgeav.buscaminas.ui.minesweeper.board.CustomItemAnimator

class MinesweeperFragment : Fragment() {

    private lateinit var binding: MinesweeperFragmentBinding
    private lateinit var viewModel: MinesweeperViewModel
    private lateinit var adapter: CellsBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.minesweeper_fragment, container, false)

        // Init viewModel
        val viewModelFactory = MinesweeperViewModel.Factory(
            (activity as MainActivity).loadBoardUseCase,
            (activity as MainActivity).changeMarkInCellUseCase,
            (activity as MainActivity).showCellUseCase,
            (activity as MainActivity).getElapsedMillisInBoardUseCase,
            (activity as MainActivity).setElapsedMillisInBoardUseCase,
            (activity as MainActivity).getCellsBySideUseCase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MinesweeperViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Init RecyclerView Layout Manager
        val manager = GridLayoutManager(this.activity, viewModel.getCellsBySide())
        binding.cellsBoardView.layoutManager = manager
        binding.cellsBoardView.itemAnimator = CustomItemAnimator()

        // Init RecyclerView Adapter
        adapter = CellsBoardAdapter(object :
                CellItemClickListener {
                override fun onClick(cell: Cell) = viewModel.cellGridClicked(cell)
                override fun onLongClick(cell: Cell) : Boolean = viewModel.cellGridLongClicked(cell)
            })
        binding.cellsBoardView.adapter = adapter

        // Observe viewModel
        viewModel.cells.observe(viewLifecycleOwner, Observer { cellsMap ->
            cellsMap?.let {
                showCellsInGrid(it)
            }
        })
        viewModel.newBoardButtonEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToNewBoardFragment()
                viewModel.onNewBoardButtonEventConsumed()
            }
        })
        viewModel.gameWonEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToGameWinFragment()
                viewModel.gameWonEventConsumed()
            }
        })
        viewModel.gameLoseEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToGameLoseFragment()
                viewModel.gameLoseEventConsumed()
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshBoard()
    }

    private fun showCellsInGrid(cellsMap: Map<Pair<Int, Int>, Cell>) {
        adapter.submitList(cellsMap.values.toList())
        shouldShowProgressView(false)
    }

    private fun navigateToNewBoardFragment() {
        shouldShowProgressView(true)
        findNavController().navigate(R.id.action_minesweeperFragment_to_newBoardFragment)
    }

    private fun navigateToGameWinFragment() {
        shouldShowProgressView(true)
        findNavController().navigate(R.id.action_minesweeperFragment_to_gameWinFragment)
    }

    private fun navigateToGameLoseFragment() {
        shouldShowProgressView(true)
        findNavController().navigate(R.id.action_minesweeperFragment_to_gameLoseFragment)
    }

    private fun shouldShowProgressView(showProgress : Boolean) {
        if (showProgress) {
            binding.progressCircularView.visibility = View.VISIBLE

            binding.cellsBoardView.visibility = View.INVISIBLE
            binding.timeImage.visibility = View.INVISIBLE
            binding.timeText.visibility = View.INVISIBLE
            binding.bombsImage.visibility = View.INVISIBLE
            binding.bombsText.visibility = View.INVISIBLE
        } else {
            binding.progressCircularView.visibility = View.INVISIBLE

            binding.cellsBoardView.visibility = View.VISIBLE
            binding.timeImage.visibility = View.VISIBLE
            binding.timeText.visibility = View.VISIBLE
            binding.bombsImage.visibility = View.VISIBLE
            binding.bombsText.visibility = View.VISIBLE
        }
    }
}
