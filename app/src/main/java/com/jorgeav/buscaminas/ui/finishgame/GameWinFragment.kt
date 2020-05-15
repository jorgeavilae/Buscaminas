package com.jorgeav.buscaminas.ui.finishgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jorgeav.buscaminas.R

class GameWinFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(
            resources.getDrawable(R.drawable.dialog_background_win, null))

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_win, container, false)
    }
}
