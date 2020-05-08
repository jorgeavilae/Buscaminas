package com.jorgeav.buscaminas.ui.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jorgeav.buscaminas.MainActivity
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.domain.Cell

class MinesweeperFragment : Fragment() {

    private lateinit var viewModel: MinesweeperViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.minesweeper_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = MinesweeperViewModel
            .Factory((activity as MainActivity).loadBoardUseCase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MinesweeperViewModel::class.java)

        viewModel.cells.observe(viewLifecycleOwner, Observer { cellsMap ->
            cellsMap?.let {
                processCells(it)
            }
        })
    }

    private fun processCells(it: Map<Pair<Int, Int>, Cell>) {
        var str = ""
        for (i in it.values)
            str += "${i.x},${i.y} show(${i.isShowing}) mark(${i.isMarked}) num(${i.numberOfBombsInBounds}) bomb(${i.isBomb}) \n"
        view?.findViewById<TextView>(R.id.minesweeperFragment_textView)?.text = str
    }

}
