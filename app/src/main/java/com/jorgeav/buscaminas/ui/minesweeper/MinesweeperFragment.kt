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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jorgeav.buscaminas.MyApplication
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.databinding.MinesweeperFragmentBinding
import com.jorgeav.buscaminas.domain.Cell
import com.jorgeav.buscaminas.ui.minesweeper.board.CellItemClickListener
import com.jorgeav.buscaminas.ui.minesweeper.board.CellsBoardAdapter
import com.jorgeav.buscaminas.ui.minesweeper.board.CustomItemAnimator
import kotlinx.coroutines.launch
import javax.inject.Inject

class MinesweeperFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: MinesweeperViewModel.Factory

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
        viewModel = ViewModelProvider(this, mViewModelFactory).get(MinesweeperViewModel::class.java)
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
                if (cellsMap.isEmpty()) showEmptyView()
                else submitCellsInGrid(it)
            }
        })
        viewModel.newBoardButtonEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToNewBoardFragment()
                viewModel.onNewBoardButtonEventConsumed()
            }
        })
        viewModel.startChronoEvent.observe(viewLifecycleOwner, Observer { shouldStart ->
            // When chronoShouldStart is true, app must be in resume, then start chrono.
            // When app is in resume, chronoShouldStart must be true, then start chrono.
            if (shouldStart && isResumed) startChronometer()
        })
        viewModel.setChronoTimeEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.setElapsedMillisInBoardSinceStarted(binding.timeText.base)
                stopChronometer()
                viewModel.setChronoTimeEventConsumed()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MyApplication).appComponent.inject(this)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            viewModel.refreshBoard()
        }
    }

    override fun onResume() {
        super.onResume()
        // When chronoShouldStart is true, app must be in resume, then start chrono.
        // When app is in resume, chronoShouldStart must be true, then start chrono.
        if (viewModel.startChronoEvent.value == true) startChronometer()

        // Set time in chronometer to show time, but don't start it
        binding.timeText.base = viewModel.getBaseForChronometer()
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.gameFinishedState.value != null && viewModel.gameFinishedState.value == false)
            viewModel.setElapsedMillisInBoardSinceStarted(binding.timeText.base)
        stopChronometer()
    }

    private fun submitCellsInGrid(cellsMap: Map<Pair<Int, Int>, Cell>) {
        adapter.submitList(cellsMap.values.toList())
        showGridView()
    }

    private fun startChronometer() {
        binding.timeText.base = viewModel.getBaseForChronometer()
        binding.timeText.start()
        viewModel.startChronoEventConsumed()
    }

    private fun stopChronometer() {
        binding.timeText.stop()
    }

    private fun navigateToNewBoardFragment() {
        showProgressView()
        if (findNavController().currentDestination?.id == R.id.minesweeperFragment)
            findNavController().navigate(R.id.action_minesweeperFragment_to_newBoardFragment)
    }

    private fun navigateToGameWinFragment() {
        findNavController().navigate(R.id.action_minesweeperFragment_to_gameWinFragment)
    }

    private fun navigateToGameLoseFragment() {
        findNavController().navigate(R.id.action_minesweeperFragment_to_gameLoseFragment)
    }

    private fun showProgressView() {
        binding.minesweeperMotionContainer.transitionToState(R.id.constraint_set_start)
    }

    private fun showGridView() {
        binding.minesweeperMotionContainer.transitionToState(R.id.constraint_set_end)
    }

    private fun showEmptyView() {
        binding.minesweeperMotionContainer.transitionToState(R.id.constraint_set_no_board)
    }
}
