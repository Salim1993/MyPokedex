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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import retrofit2.HttpException
import java.net.UnknownHostException

class GetPokemonListUseCaseTest {

    @MockK lateinit var pokedexApiService: PokedexApiService
    //Relaxing function that return unit cause i don't care about there results, only that they are called
    @MockK(relaxUnitFun = true) lateinit var sharedPreferencesWrapper: SharedPreferencesWrapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getOriginal151List is success and return list of bulbasaur`() {
        //arrange
        val bulbasaurList = listOf(PokemonListItemSchema("bulbasaur", "NA"))
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } returns bulbasaurList
        every { sharedPreferencesWrapper.getString(any()) } returns ""

        val testSubject =  GetPokemonListUseCase(pokedexApiService, sharedPreferencesWrapper)

        //act
        runBlocking {
            testSubject.getOriginal151List()
        }

        //assert
        runBlocking {
            val result = testSubject.pokemonListFlow.first()

            assertEquals(listOf("bulbasaur"), result)
        }
        verify { sharedPreferencesWrapper.saveString(any(), any()) }
    }

    @Test
    fun `getOriginal151List receives HttpException and return empty list of pokemon`() = runBlocking {
        val mockedHttpException = mockk<HttpException>(relaxed = true)
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws mockedHttpException
        every { sharedPreferencesWrapper.getString(any()) } returns ""

        val testSubject =  GetPokemonListUseCase(pokedexApiService, sharedPreferencesWrapper)

        testSubject.getOriginal151List()

        assertTrue(testSubject.pokemonListFlow.value.isEmpty())
    }

    @Test
    fun `getOriginal151List receives HttpException and return bulbasaur in cache`() = runBlocking {
        val mockedHttpException = mockk<HttpException>(relaxed = true)
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws mockedHttpException
        every { sharedPreferencesWrapper.getString(any()) } returns "bulbasaur"

        val testSubject =  GetPokemonListUseCase(pokedexApiService, sharedPreferencesWrapper)

        testSubject.getOriginal151List()

        //assert
        val result = testSubject.pokemonListFlow.first()

        assertEquals(listOf("bulbasaur"), result)
        verify(exactly = 0) { sharedPreferencesWrapper.saveString(any(), any()) }
    }

    @Test
    fun `getOriginal151List receives UnknownHostException and return empty list of pokemon`() {
        //arrange
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws UnknownHostException()
        every { sharedPreferencesWrapper.getString(any()) } returns "bulbasaur"

        val testSubject =  GetPokemonListUseCase(pokedexApiService, sharedPreferencesWrapper)

        //act
        runBlocking {
            testSubject.getOriginal151List()
        }

        //assert
        runBlocking {
            val result = testSubject.pokemonListFlow.first()

            assertEquals(listOf("bulbasaur"), result)
        }
        verify(exactly = 0) { sharedPreferencesWrapper.saveString(any(), any()) }
    }

    @Test
    fun `getOriginal151List receives UnknownHostException and return bulbasaur in cache`() {
        //arrange
        coEvery { pokedexApiService.getListOfPokemon(any(), any()) } throws UnknownHostException()
        every { sharedPreferencesWrapper.getString(any()) } returns ""

        val testSubject =  GetPokemonListUseCase(pokedexApiService, sharedPreferencesWrapper)

        //act
        runBlocking {
            testSubject.getOriginal151List()
        }

        //assert
        runBlocking {
            val result = testSubject.pokemonListFlow.first()

            assertTrue(result.isEmpty())
        }
        verify(exactly = 0) { sharedPreferencesWrapper.saveString(any(), any()) }
    }
}