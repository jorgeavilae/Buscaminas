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
import androidx.recyclerview.widget.GridLayoutManager
import com.jorgeav.buscaminas.MainActivity
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.databinding.MinesweeperFragmentBinding
import com.jorgeav.buscaminas.domain.Cell

class MinesweeperFragment : Fragment() {

    private lateinit var binding: MinesweeperFragmentBinding
    private lateinit var viewModel: MinesweeperViewModel
    private lateinit var adapter: CellsBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.minesweeper_fragment, container, false)

        val viewModelFactory = MinesweeperViewModel
            .Factory((activity as MainActivity).loadBoardUseCase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MinesweeperViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.cells.observe(viewLifecycleOwner, Observer { cellsMap ->
            cellsMap?.let {
                showCellsInGrid(it)
            }
        })

        return binding.root
    }

    private fun showCellsInGrid(cellsMap: Map<Pair<Int, Int>, Cell>) {
        val columns = numOfColumnsInBoard(cellsMap.values)

        val manager = GridLayoutManager(this.activity, columns)
        binding.cellsBoardView.layoutManager = manager

        adapter = CellsBoardAdapter( object : CellItemClickListener {
            override fun onClick(cellId: Pair<Int, Int>) {
                Toast.makeText(context, cellId.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onLongClick(cellId: Pair<Int, Int>) {
                Toast.makeText(context, cellsMap[cellId].toString(), Toast.LENGTH_SHORT).show()
            }}
        )
        binding.cellsBoardView.adapter = adapter

        adapter.submitList(cellsMap.values.toList())
    }

    private fun numOfColumnsInBoard(cellsMap: Collection<Cell>): Int {
        var columns : Int = 0
        cellsMap.maxBy { it.y }?.let{
            columns = it.y + 1
        }
        return columns
    }

}
