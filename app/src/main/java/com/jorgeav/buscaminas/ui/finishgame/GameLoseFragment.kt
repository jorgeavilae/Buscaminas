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
import com.jorgeav.buscaminas.MainActivity
import com.jorgeav.buscaminas.R

class GameLoseFragment : DialogFragment() {

    private lateinit var message : CharSequence

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(
            resources.getDrawable(R.drawable.dialog_background_lose, null))

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_lose, container, false)

        constructMessageForDialog()

        view.findViewById<TextView>(R.id.dialog_lose_text).text = HtmlCompat.fromHtml(
            resources.getString(R.string.text_dialog_lose, message),
            HtmlCompat.FROM_HTML_MODE_LEGACY)

        view.findViewById<Button>(R.id.dialog_lose_share).setOnClickListener {
            shareMessage(HtmlCompat.fromHtml(
                resources.getString(R.string.share_message_dialog_lose, message),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
                .toString())
        }

        return view
    }

    private fun constructMessageForDialog() {
        val columns : Int = (activity as MainActivity).getCellsBySideUseCase()
        val bombs : Int = (activity as MainActivity).getBombsInBoardUseCase()
        val timeMillis : Long = (activity as MainActivity).getElapsedMillisInBoardUseCase()
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
