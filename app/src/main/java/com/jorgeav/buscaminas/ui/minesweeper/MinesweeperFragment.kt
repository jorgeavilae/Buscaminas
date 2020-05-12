package com.jorgeav.buscaminas.ui.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        // Init RecyclerView Adapter
        adapter =
            CellsBoardAdapter(object :
                CellItemClickListener {
                override fun onClick(cell: Cell) = viewModel.cellGridClicked(cell)
                override fun onLongClick(cell: Cell) = viewModel.cellGridLongClicked(cell)
            })
        binding.cellsBoardView.adapter = adapter

        // Observe viewModel
        viewModel.cells.observe(viewLifecycleOwner, Observer { cellsMap ->
            cellsMap?.let {
                showCellsInGrid(it)
            }
        })
        viewModel.newBoardButtonState.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToNewBoardFragment()
                viewModel.onNewBoardButtonStateConsumed()
            }
        })
        viewModel.chronoShouldStartState.observe(viewLifecycleOwner, Observer { shouldStart ->
            if (shouldStart && isResumed) startChronometer()
        })
        viewModel.chronoShouldStopState.observe(viewLifecycleOwner, Observer { shouldStop ->
            if (shouldStop) stopChronometer()
        })
        viewModel.isGameWinOrLose.observe(viewLifecycleOwner, Observer { isGameWin ->
            isGameWin?.let {
                if (it)
                    Toast.makeText(this.context, "YOU WIN", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this.context, "YOU LOSE", Toast.LENGTH_SHORT).show()

                viewModel.isGameWinOrLoseConsumed()
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadCellsData()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.chronoShouldStartState.value == true) startChronometer()
    }

    override fun onPause() {
        super.onPause()
        stopChronometer()
    }

    private fun showCellsInGrid(cellsMap: Map<Pair<Int, Int>, Cell>) {
        adapter.submitList(cellsMap.values.toList())
        shouldShowProgressView(false)
    }

    private fun navigateToNewBoardFragment() {
        shouldShowProgressView(true)
        findNavController().navigate(R.id.action_minesweeperFragment_to_newBoardFragment)
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

    private fun startChronometer() {
        binding.timeText.base = viewModel.getBaseForChronometer()
        binding.timeText.start()
        viewModel.chronoShouldStartStateConsumed()
    }

    private fun stopChronometer() {
        binding.timeText.stop()
        viewModel.setElapsedMillisInBoardSinceStarted(binding.timeText.base)
        viewModel.chronoShouldStopStateConsumed()
    }

}
