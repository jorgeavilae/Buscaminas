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

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jorgeav.buscaminas.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.about_developer_text).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.about_github_text).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.about_dagger).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.about_room_explorer).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.about_icons).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.about_eula_link).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.about_license_link).movementMethod = LinkMovementMethod.getInstance()
    }
}
