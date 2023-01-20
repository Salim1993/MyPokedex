package com.salim.mypokedex

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.salim.mypokedex.database.AppDatabase
import com.salim.mypokedex.pokemon.PokemonDao
import com.salim.mypokedex.testHelpers.PokemonTestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SimpleRoomTest {

    private lateinit var dao: PokemonDao
    private lateinit var database: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        dao = database.pokemonDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun writePokemonAndReadInList() = runTest {
        // Context of the app under test.
        val pokemon = PokemonTestData.TEST_POKEMON.copy(name = "bulbasaur")
        dao.updatePokemonDetail(pokemon)

        val pokemonByName = dao.getSpecificPokemon("bulbasaur")
        assertEquals(pokemonByName.first(), pokemon)
    }
}