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

import android.view.LayoutInflater
import android.view.ViewGroup
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
        CellViewHolder.from(
            parent
        )

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

                return CellViewHolder(
                    binding
                )
            }
        }

        fun bind(clickListener: CellItemClickListener, item: Cell) {
            binding.cell = item

            binding.cellGridView.setOnClickListener {
                clickListener.onClick(item)
            }

            binding.cellGridView.setOnLongClickListener {
                return@setOnLongClickListener clickListener.onLongClick(item)
            }

            binding.executePendingBindings()
        }
    }
}

interface CellItemClickListener {
    fun onClick(cell: Cell)
    fun onLongClick(cell: Cell) : Boolean
}

object CellDiffCallback : DiffUtil.ItemCallback<Cell>() {
    override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return oldItem == newItem
    }
}