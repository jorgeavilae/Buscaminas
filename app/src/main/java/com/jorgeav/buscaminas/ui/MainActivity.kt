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

package com.jorgeav.buscaminas.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.jorgeav.buscaminas.BuildConfig
import com.jorgeav.buscaminas.R
import com.jorgeav.buscaminas.db.CellDBDatabase
import com.jorgeav.buscaminas.db.DATABASE_NAME
import com.jorgeav.buscaminas.ui.about.AboutActivity
import com.wajahatkarim3.roomexplorer.RoomExplorer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about, menu)
        if (BuildConfig.DEBUG)
            menu?.add(Menu.NONE, R.id.ddbb_content, Menu.NONE, R.string.ddbb_content)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            R.id.ddbb_content -> {
                RoomExplorer.show(
                    this,
                    CellDBDatabase::class.java,
                    DATABASE_NAME)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
