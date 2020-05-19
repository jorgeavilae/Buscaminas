package com.jorgeav.buscaminas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.jorgeav.buscaminas.db.CellDBDatabase
import com.jorgeav.buscaminas.db.DATABASE_NAME
import com.wajahatkarim3.roomexplorer.RoomExplorer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.debug_ddbb_content, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
