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

package com.jorgeav.buscaminas.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jorgeav.buscaminas.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.about_dagger_link).apply {
            setOnClickListener {
                openLink(Uri.parse(text.toString()))
            }
        }

        findViewById<TextView>(R.id.about_room_explorer_link).apply {
            setOnClickListener {
                openLink(Uri.parse(text.toString()))
            }
        }

        findViewById<TextView>(R.id.about_icons_link).apply {
            setOnClickListener {
                openLink(Uri.parse(text.toString()))
            }
        }
    }

    private fun openLink(uri: Uri) {
        Toast.makeText(this, R.string.opening_link, Toast.LENGTH_SHORT).show()
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}
