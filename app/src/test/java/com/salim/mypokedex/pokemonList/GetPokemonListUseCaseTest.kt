package com.salim.mypokedex.pokemonList

import com.salim.mypokedex.networking.PokedexApiService
import com.salim.mypokedex.pokemon.PokemonListItemSchema
import com.salim.mypokedex.utilities.SharedPreferencesWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import retrofit2.HttpException
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonListUseCaseTest {

    @MockK
    lateinit var pokedexApiService: PokedexApiService
    //Relaxing function that return unit cause i don't care about there results, only that they are called
    @MockK(relaxUnitFun = true)
    lateinit var sharedPreferencesWrapper: SharedPreferencesWrapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getPokemonList is success and return list of bulbasaur`() = runTest {
        // arrange
        val bulbasaurList = listOf(PokemonListItemSchema("bulbasaur", "NA"))
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } returns bulbasaurList
        every { sharedPreferencesWrapper.getString(any()) } returns ""

        val testSubject =  GetPokemonListUseCaseImpl(pokedexApiService, sharedPreferencesWrapper)

        // act
        testSubject.getPokemonList(NUMBER, OFFSET)

        // assert
        val result = testSubject.getPokemonListFlow().first()

        assertEquals(listOf("bulbasaur"), result)
        verify { sharedPreferencesWrapper.saveString(any(), any()) }
    }

    @Test
    fun `getPokemonList receives HttpException and return empty list of pokemon`() = runTest {
        // arrange
        val mockedHttpException = mockk<HttpException>(relaxed = true)
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws mockedHttpException
        every { sharedPreferencesWrapper.getString(any()) } returns ""

        val testSubject =  GetPokemonListUseCaseImpl(pokedexApiService, sharedPreferencesWrapper)

        // act
        testSubject.getPokemonList(NUMBER, OFFSET)

        // assert
        assertTrue(testSubject.getPokemonListFlow().value.isEmpty())
    }

    @Test
    fun `getPokemonList receives HttpException and return bulbasaur in cache`() = runTest {
        // arrange
        val mockedHttpException = mockk<HttpException>(relaxed = true)
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws mockedHttpException
        every { sharedPreferencesWrapper.getString(any()) } returns "bulbasaur"

        val testSubject =  GetPokemonListUseCaseImpl(pokedexApiService, sharedPreferencesWrapper)

        // act
        testSubject.getPokemonList(NUMBER, OFFSET)

        // assert
        val result = testSubject.getPokemonListFlow().first()

        assertEquals(listOf("bulbasaur"), result)
        verify(exactly = 0) { sharedPreferencesWrapper.saveString(any(), any()) }
    }

    @Test
    fun `getPokemonList receives UnknownHostException and return empty list of pokemon`() = runTest {
        // arrange
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws UnknownHostException()
        every { sharedPreferencesWrapper.getString(any()) } returns "bulbasaur"

        val testSubject =  GetPokemonListUseCaseImpl(pokedexApiService, sharedPreferencesWrapper)

        // act
        testSubject.getPokemonList(NUMBER, OFFSET)

        // assert
        val result = testSubject.getPokemonListFlow().first()

        assertEquals(listOf("bulbasaur"), result)
        verify(exactly = 0) { sharedPreferencesWrapper.saveString(any(), any()) }
    }

    @Test
    fun `getPokemonList receives UnknownHostException and return bulbasaur in cache`() = runTest {
        // arrange
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws UnknownHostException()
        every { sharedPreferencesWrapper.getString(any()) } returns ""

        val testSubject =  GetPokemonListUseCaseImpl(pokedexApiService, sharedPreferencesWrapper)

        // act
        testSubject.getPokemonList(NUMBER, OFFSET)

        // assert
        val result = testSubject.getPokemonListFlow().first()

        assertTrue(result.isEmpty())
        verify(exactly = 0) { sharedPreferencesWrapper.saveString(any(), any()) }
    }

    @Test
    fun `getPokemonList is success and saved into cache with correct format`() = runTest {
        // arrange
        val pokemonList = listOf(
            PokemonListItemSchema("bulbasaur", "NA"),
            PokemonListItemSchema("ivysaur", "NA"),
            PokemonListItemSchema("venasaur", "NA"),
        )
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } returns pokemonList
        every { sharedPreferencesWrapper.getString(any()) } returns ""

        val testSubject =  GetPokemonListUseCaseImpl(pokedexApiService, sharedPreferencesWrapper)

        // act
        testSubject.getPokemonList(NUMBER, OFFSET)

        // assert
        val result = testSubject.getPokemonListFlow().first()

        assertEquals(listOf("bulbasaur", "ivysaur", "venasaur"), result)
        val expectedSavedString = "bulbasaur, ivysaur, venasaur"
        verify { sharedPreferencesWrapper.saveString(any(), expectedSavedString) }
    }

    @Test
    fun `getPokemonList is failure and but loads info from cache correctly`() = runTest {
        // arrange
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws UnknownHostException()
        every { sharedPreferencesWrapper.getString(any()) } returns "bulbasaur, ivysaur, venasaur"

        val testSubject =  GetPokemonListUseCaseImpl(pokedexApiService, sharedPreferencesWrapper)

        // act
        testSubject.getPokemonList(NUMBER, OFFSET)

        // assert
        val result = testSubject.getPokemonListFlow().first()

        assertEquals(listOf("bulbasaur", "ivysaur", "venasaur"), result)
        verify(exactly = 0) { sharedPreferencesWrapper.saveString(any(), any()) }
    }

    companion object {
        // These don't matter cause api being mocked, and these parameters only affect pokedex api
        private const val NUMBER = 151
        private const val OFFSET = 0
    }
}
