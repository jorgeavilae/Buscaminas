package com.jorgeav.buscaminas.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var binding : MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.rows.observe(viewLifecycleOwner, Observer { rows ->
            rows?.let {
                enabledRowsButtons(rows)
            }
        })
        viewModel.columns.observe(viewLifecycleOwner, Observer { columns ->
            columns?.let {
                enabledColumnsButtons(columns)
            }
        })
        viewModel.generateButtonClicked.observe(viewLifecycleOwner, Observer { isClicked ->
            if (isClicked) {
                generateMinesweeperBoard()
                viewModel.generateButtonClickedConsumed()
            }
        })

        return binding.root
    }

    private fun generateMinesweeperBoard() {
        findNavController().navigate(R.id.action_mainFragment_to_minesweeperFragment)
    }

    private fun enabledRowsButtons(rows: Int) {
        when {
            rows <= MIN_ROWS -> {
                binding.lessRowsButton.isEnabled = false
                binding.moreRowsButton.isEnabled = true
            }
            rows >= MAX_ROWS -> {
                binding.lessRowsButton.isEnabled = true
                binding.moreRowsButton.isEnabled = false
            }
            else -> {
                binding.lessRowsButton.isEnabled = true
                binding.moreRowsButton.isEnabled = true
            }
        }
    }

    private fun enabledColumnsButtons(columns: Int) {
        when {
            columns <= MIN_COLUMNS -> {
                binding.lessColumnsButton.isEnabled = false
                binding.moreColumnsButton.isEnabled = true
            }
            columns >= MAX_COLUMNS -> {
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
