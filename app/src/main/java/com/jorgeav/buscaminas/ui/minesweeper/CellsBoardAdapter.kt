package com.jorgeav.buscaminas.ui.minesweeper

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jorgeav.buscaminas.databinding.GridItemCellBinding
import com.jorgeav.buscaminas.domain.Cell

/**
 * Created by Jorge Avila on 08/05/2020.
 */
class CellsBoardAdapter(private val clickListener: CellItemClickListener) :
    ListAdapter<Cell, RecyclerView.ViewHolder>(CellDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CellViewHolder.from(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CellViewHolder)
            holder.bind(clickListener, getItem(position))
    }

    class CellViewHolder private constructor(private val binding: GridItemCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): CellViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridItemCellBinding.inflate(layoutInflater, parent, false)

                return CellViewHolder(binding)
            }
        }

        fun bind(clickListener: CellItemClickListener, item: Cell) {
            binding.cell = item

            binding.cellGridTextView.setOnClickListener {
                clickListener.onClick(item)
            }

            binding.cellGridTextView.setOnLongClickListener {
                clickListener.onLongClick(item)
                return@setOnLongClickListener true
            }

            binding.executePendingBindings()
        }
    }
}

interface CellItemClickListener {
    fun onClick(cell: Cell)
    fun onLongClick(cell: Cell)
}

object CellDiffCallback : DiffUtil.ItemCallback<Cell>() {
    override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("cellContent")
fun TextView.setCellContent(item: Cell?) {
    item?.let {
        // TODO 1 completar apariencia de cada list item
        text =
            if (!it.isShowing)
                if (it.isMarked) "?" else ""
            else
                if (it.isBomb) "X"
                else
                    if (it.numberOfBombsInBounds == 0) "0" else it.numberOfBombsInBounds.toString()
    }
}