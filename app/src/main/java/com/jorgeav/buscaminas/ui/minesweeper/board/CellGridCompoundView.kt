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

    fun showAsUnMarked() {
        binding.cellGridContainer.background = resources.getDrawable(R.drawable.cell_grid_background_hide, null)

        binding.cellGridImageView.visibility = View.INVISIBLE
        binding.cellGridTextView.visibility = View.VISIBLE

        binding.cellGridTextView.text = ""
    }

    fun showAsBomb() {
        binding.cellGridContainer.background = resources.getDrawable(R.drawable.cell_grid_background_bomb, null)

        binding.cellGridImageView.visibility = View.VISIBLE
        binding.cellGridTextView.visibility = View.INVISIBLE

        binding.cellGridImageView.setImageResource(R.drawable.ic_bomb)
    }

    fun showAsNumber(number : Int) {
        binding.cellGridContainer.background = resources.getDrawable(R.drawable.cell_grid_background_show, null)

        binding.cellGridImageView.visibility = View.INVISIBLE
        binding.cellGridTextView.visibility = View.VISIBLE

        binding.cellGridTextView.text = if (number != 0) number.toString() else ""
    }
}

@BindingAdapter("cellState")
fun CellGridCompoundView.setCellState(item: Cell?) {
    item?.let {
        when(val state = Cell.getStateOfCell(it)) {
            is CellShowingState.StateMark -> showAsMarked()
            is CellShowingState.StateUnMark -> showAsUnMarked()
            is CellShowingState.StateBomb -> showAsBomb()
            is CellShowingState.StateNumber -> showAsNumber(state.numberOfBombsInBounds)
        }
    }
}