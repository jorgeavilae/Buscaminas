package com.jorgeav.buscaminas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.jorgeav.buscaminas.data.Repository
import com.jorgeav.buscaminas.db.CellDBDatabase
import com.jorgeav.buscaminas.db.DATABASE_NAME
import com.jorgeav.buscaminas.db.StructuredDataSource
import com.jorgeav.buscaminas.usecases.*
import com.wajahatkarim3.roomexplorer.RoomExplorer

class MainActivity : AppCompatActivity() {

    lateinit var createNewBoardUseCase: CreateNewBoardUseCase
    lateinit var loadBoardUseCase: LoadBoardUseCase
    lateinit var changeMarkInCellUseCase: ChangeMarkInCellUseCase
    lateinit var showCellUseCase: ShowCellUseCase
    lateinit var getElapsedMillisInBoardUseCase: GetElapsedMillisInBoardUseCase
    lateinit var setElapsedMillisInBoardUseCase: SetElapsedMillisInBoardUseCase
    lateinit var getCellsBySideUseCase: GetCellsBySideUseCase
    lateinit var getBombsInBoardUseCase: GetBombsInBoardUseCase

    lateinit var countMarksUseCase: CountMarksUseCase
    lateinit var checkBoardWinOrLoseUseCase: CheckBoardWinOrLoseUseCase
    lateinit var getCellsBySideRangeUseCase: GetCellsBySideRangeUseCase
    lateinit var getBombsRangeInBoardUseCase: GetBombsRangeInBoardUseCase
    fun init() {
        // TODO This would be done by a dependency injector in a complex App
        val structuredDataSource = StructuredDataSource(this)
        val repository = Repository(structuredDataSource, structuredDataSource)

        getElapsedMillisInBoardUseCase = GetElapsedMillisInBoardUseCase(repository)
        setElapsedMillisInBoardUseCase = SetElapsedMillisInBoardUseCase(repository)
        createNewBoardUseCase = CreateNewBoardUseCase(repository)
        loadBoardUseCase = LoadBoardUseCase(repository)
        changeMarkInCellUseCase = ChangeMarkInCellUseCase(repository)
        showCellUseCase = ShowCellUseCase(repository)
        getCellsBySideUseCase = GetCellsBySideUseCase(repository)
        getBombsInBoardUseCase = GetBombsInBoardUseCase(repository)

        countMarksUseCase = CountMarksUseCase()
        checkBoardWinOrLoseUseCase = CheckBoardWinOrLoseUseCase()
        getCellsBySideRangeUseCase = GetCellsBySideRangeUseCase()
        getBombsRangeInBoardUseCase = GetBombsRangeInBoardUseCase()
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
