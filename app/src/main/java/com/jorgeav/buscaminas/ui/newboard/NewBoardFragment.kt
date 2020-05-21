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

package com.jorgeav.buscaminas.ui.newboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jorgeav.buscaminas.MyApplication
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.databinding.NewBoardFragmentBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("ClickableViewAccessibility")
class NewBoardFragment : Fragment(), View.OnTouchListener {

    @Inject lateinit var mViewModelFactory: NewBoardViewModel.Factory

    private lateinit var binding : NewBoardFragmentBinding
    private lateinit var viewModel: NewBoardViewModel

    private var moreBombsButtonClickedJob : Job? = null
    private var lessBombsButtonClickedJob : Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.new_board_fragment, container, false)

        viewModel = ViewModelProvider(this, mViewModelFactory).get(NewBoardViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.cellsBySide.observe(viewLifecycleOwner, Observer { cellsBySide ->
            cellsBySide?.let {
                enabledCellsButtons(cellsBySide)
                enabledBombsButtons(viewModel.bombs.value?: viewModel.minBombs)
            }
        })
        viewModel.bombs.observe(viewLifecycleOwner, Observer { bombs ->
            bombs?.let {
                enabledBombsButtons(bombs)
            }
        })
        viewModel.finishGenerateBoardEvent.observe(viewLifecycleOwner, Observer { state ->
            if (state) {
                viewModel.finishGenerateBoardStateConsumed()
                findNavController().popBackStack()
            }
        })

        binding.moreBombsButton.setOnTouchListener(this)
        binding.lessBombsButton.setOnTouchListener(this)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MyApplication).appComponent.inject(this)
    }

    private fun enabledCellsButtons(cells: Int) {
        when {
            cells <= viewModel.minCellsBySide -> {
                binding.lessCellsButton.isEnabled = false
                binding.moreCellsButton.isEnabled = true
            }
            cells >= viewModel.maxCellsBySide -> {
                binding.lessCellsButton.isEnabled = true
                binding.moreCellsButton.isEnabled = false
            }
            else -> {
                binding.lessCellsButton.isEnabled = true
                binding.moreCellsButton.isEnabled = true
            }
        }
    }

    private fun enabledBombsButtons(bombs: Int) {
        when {
            bombs <= viewModel.minBombs -> {
                binding.lessBombsButton.isEnabled = false
                binding.moreBombsButton.isEnabled = true
                lessBombsButtonClickedJob?.cancel()
            }
            bombs >= viewModel.maxBombs -> {
                binding.lessBombsButton.isEnabled = true
                binding.moreBombsButton.isEnabled = false
                moreBombsButtonClickedJob?.cancel()
            }
            else -> {
                binding.lessBombsButton.isEnabled = true
                binding.moreBombsButton.isEnabled = true
            }
        }

    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param view The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     * the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (view != null && event != null) {
            when (view) {
                binding.moreBombsButton -> onTouchMoreBombsButton(view, event)
                binding.lessBombsButton -> onTouchLessBombsButton(view, event)
            }
            return true
        }
        return false
    }

    private fun onTouchMoreBombsButton(view: View, event: MotionEvent) {
        if (!isMotionEventInsideView(view, event)
            || event.action == MotionEvent.ACTION_UP
            || event.action == MotionEvent.ACTION_CANCEL)
            moreBombsButtonClickedJob?.cancel()

        else if (event.action == MotionEvent.ACTION_DOWN) {
            moreBombsButtonClickedJob = lifecycleScope.launch {
                var delayMillis = 400L
                while (true) {
                    viewModel.onMoreBombsClicked()
                    // This is a suspend function so 'isActive' is evaluated
                    delay(delayMillis)
                    if (delayMillis > 100) delayMillis -= 100
                }
            }
        }
    }

    private fun onTouchLessBombsButton(view: View, event: MotionEvent) {
        if (!isMotionEventInsideView(view, event)
            || event.action == MotionEvent.ACTION_UP
            || event.action == MotionEvent.ACTION_CANCEL)
            lessBombsButtonClickedJob?.cancel()

        else if (event.action == MotionEvent.ACTION_DOWN) {
            lessBombsButtonClickedJob = lifecycleScope.launch {
                var delayMillis = 400L
                while (true) {
                    viewModel.onLessBombsClicked()
                    // This is a suspend function so 'isActive' is evaluated
                    delay(delayMillis)
                    if (delayMillis > 100) delayMillis -= 100
                }
            }
        }
    }

    private fun isMotionEventInsideView(view: View, event: MotionEvent): Boolean {
        val viewRect = Rect(view.left, view.top, view.right, view.bottom)
        return viewRect.contains(view.left + event.x.toInt(), view.top + event.y.toInt())
    }
}
