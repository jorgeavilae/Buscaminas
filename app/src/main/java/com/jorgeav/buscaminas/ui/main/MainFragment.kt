package com.jorgeav.buscaminas.ui.main

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
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.data.Repository
import com.jorgeav.buscaminas.databinding.MainFragmentBinding
import com.jorgeav.buscaminas.db.PersistenceDataSource
import com.jorgeav.buscaminas.usecases.CreateNewBoardUseCase
import com.jorgeav.buscaminas.usecases.GenerateBoardUseCase
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding : MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var createNewBoardUseCase: CreateNewBoardUseCase
    init {
        // This would be done by a dependency injector in a complex App
        //
        val persistence = PersistenceDataSource()
        val repository = Repository(persistence)

        val generateBoardUseCase = GenerateBoardUseCase()
        createNewBoardUseCase = CreateNewBoardUseCase(generateBoardUseCase, repository)
    }

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
        lifecycleScope.launch {
            val rows = viewModel.rows.value ?: MIN_ROWS
            val columns = viewModel.columns.value ?: MIN_COLUMNS
            val bombs = ((rows * columns) / 4).toInt()
            createNewBoardUseCase(rows, columns, bombs)
        }
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
