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

package com.jorgeav.buscaminas.ui.finishgame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.jorgeav.buscaminas.MyApplication
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.usecases.GetBombsInBoardUseCase
import com.jorgeav.buscaminas.usecases.GetCellsBySideUseCase
import com.jorgeav.buscaminas.usecases.GetElapsedMillisInBoardUseCase
import javax.inject.Inject

class GameWinFragment : DialogFragment() {

    @Inject lateinit var getCellsBySideUseCase: GetCellsBySideUseCase
    @Inject lateinit var getBombsInBoardUseCase: GetBombsInBoardUseCase
    @Inject lateinit var getElapsedMillisInBoardUseCase: GetElapsedMillisInBoardUseCase

    private lateinit var message : CharSequence

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(
            resources.getDrawable(R.drawable.dialog_background_win, null))

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_win, container, false)

        constructMessageForDialog()

        view.findViewById<TextView>(R.id.dialog_win_text).text = HtmlCompat.fromHtml(
            resources.getString(R.string.text_dialog_win, message),
            HtmlCompat.FROM_HTML_MODE_LEGACY)

        view.findViewById<Button>(R.id.dialog_win_share).setOnClickListener {
            shareMessage(HtmlCompat.fromHtml(
                resources.getString(R.string.share_message_dialog_win, message),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
                .toString())
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MyApplication).appComponent.inject(this)
    }

    private fun constructMessageForDialog() {
        val columns : Int = getCellsBySideUseCase()
        val bombs : Int = getBombsInBoardUseCase()
        val timeMillis : Long = getElapsedMillisInBoardUseCase()
        val timeStr : String = DateUtils.formatElapsedTime(timeMillis / 1000)

        message = resources.getString(R.string.text_dialog, columns, bombs, timeStr)
    }

    private fun shareMessage(msg: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, msg)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
