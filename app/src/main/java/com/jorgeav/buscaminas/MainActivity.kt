package com.jorgeav.buscaminas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.jorgeav.buscaminas.data.Repository
import com.jorgeav.buscaminas.db.CellDBDatabase
import com.jorgeav.buscaminas.db.DATABASE_NAME
import com.jorgeav.buscaminas.db.PersistenceDataSource
import com.jorgeav.buscaminas.usecases.CreateNewBoardUseCase
import com.jorgeav.buscaminas.usecases.DeleteBoardUseCase
import com.jorgeav.buscaminas.usecases.GenerateBoardUseCase
import com.jorgeav.buscaminas.usecases.LoadBoardUseCase
import com.wajahatkarim3.roomexplorer.RoomExplorer

class MainActivity : AppCompatActivity() {

    lateinit var createNewBoardUseCase: CreateNewBoardUseCase
    lateinit var loadBoardUseCase: LoadBoardUseCase
    fun init() {
        // TODO This would be done by a dependency injector in a complex App
        val persistence = PersistenceDataSource(this)
        val repository = Repository(persistence)

        val deleteBoardUseCase = DeleteBoardUseCase(repository)
        val generateBoardUseCase = GenerateBoardUseCase()
        createNewBoardUseCase = CreateNewBoardUseCase(
            deleteBoardUseCase,
            generateBoardUseCase,
            repository)
        loadBoardUseCase = LoadBoardUseCase(repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        init()
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
