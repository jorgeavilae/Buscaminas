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

package com.jorgeav.buscaminas.ui.minesweeper.board

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.databinding.CellGridCompoundViewBinding
import com.jorgeav.buscaminas.domain.Cell
import com.jorgeav.buscaminas.domain.CellShowingState

/**
 * Created by Jorge Avila on 10/05/2020.
 */
class CellGridCompoundView(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding: CellGridCompoundViewBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context), R.layout.cell_grid_compound_view, this, true)


    fun showAsMarked() {
        binding.cellGridContainer.background = resources.getDrawable(R.drawable.cell_grid_background_hide, null)

        binding.cellGridImageView.visibility = View.VISIBLE
        binding.cellGridTextView.visibility = View.INVISIBLE

        binding.cellGridImageView.setImageResource(R.drawable.ic_mark)
    }

    fun showAsIdle() {
        binding.cellGridContainer.background = resources.getDrawable(R.drawable.cell_grid_background_hide, null)

        binding.cellGridImageView.visibility = View.INVISIBLE
        binding.cellGridTextView.visibility = View.VISIBLE

        binding.cellGridTextView.text = ""
    }

    fun showAsBomb() {
        binding.cellGridContainer.background = resources.getDrawable(R.drawable.cell_grid_background_show, null)

        binding.cellGridImageView.visibility = View.VISIBLE
        binding.cellGridTextView.visibility = View.INVISIBLE

        binding.cellGridImageView.setImageResource(R.drawable.ic_bomb_dark)
    }

    fun showAsBombHit() {
        binding.cellGridContainer.background = resources.getDrawable(R.drawable.cell_grid_background_bomb, null)

        binding.cellGridImageView.visibility = View.VISIBLE
        binding.cellGridTextView.visibility = View.INVISIBLE

        binding.cellGridImageView.setImageResource(R.drawable.ic_bomb_light)
    }

    fun showAsNumber(number : Int) {
        binding.cellGridContainer.background = resources.getDrawable(R.drawable.cell_grid_background_show, null)

        binding.cellGridImageView.visibility = View.INVISIBLE
        binding.cellGridTextView.visibility = View.VISIBLE

        binding.cellGridTextView.text = if (number != 0) number.toString() else ""
        binding.cellGridTextView.setTextColor(resources.getColor(
            when(number) {
                1 -> R.color.cell_number_1
                2 -> R.color.cell_number_2
                3 -> R.color.cell_number_3
                4 -> R.color.cell_number_4
                5 -> R.color.cell_number_5
                6 -> R.color.cell_number_6
                7 -> R.color.cell_number_7
                8 -> R.color.cell_number_8
                else -> R.color.primaryTextColor
        }, null))
    }
}

@BindingAdapter("cellState")
fun CellGridCompoundView.setCellState(item: Cell?) {
    item?.let {
        when(it.getStateOfCell()) {
            is CellShowingState.StateMark -> showAsMarked()
            is CellShowingState.StateIdle -> showAsIdle()
            is CellShowingState.StateBombHit -> showAsBombHit()
            is CellShowingState.StateNumber -> showAsNumber(it.numberOfBombsInBounds)
            is CellShowingState.StateBombRevealed -> showAsBomb()
        }
    }
}