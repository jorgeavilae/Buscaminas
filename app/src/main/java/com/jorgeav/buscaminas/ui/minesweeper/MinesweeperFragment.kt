package com.jorgeav.buscaminas.ui.minesweeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jorgeav.buscaminas.R

class MinesweeperFragment : Fragment() {

    private lateinit var viewModel: MinesweeperViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MinesweeperViewModel::class.java)

        return inflater.inflate(R.layout.minesweeper_fragment, container, false)
    }

}
