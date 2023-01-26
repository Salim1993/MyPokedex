package com.salim.mypokedex.pokemonDetails

import com.salim.mypokedex.PokemonTestData.TEST_POKEMON
import com.salim.mypokedex.PokemonTestData.TEST_POKEMON_DETAIL_SCHEMA
import com.salim.mypokedex.networking.PokedexApiService
import com.salim.mypokedex.pokemon.PokemonDao
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonDetailsUseCaseTest {

    @MockK
    lateinit var pokedexApiService: PokedexApiService

    @RelaxedMockK
    lateinit var dao: PokemonDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getPokemonDetails is successful and returns the pokemon details`() = runTest {
        // arrange
        val pokemonName = "bulbasaur"
        val pokemonDetailSchema = TEST_POKEMON_DETAIL_SCHEMA.copy(name = pokemonName)
        val pokemon = TEST_POKEMON.copy(name = pokemonName)
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(pokemon)
        coEvery { pokedexApiService.getPokemonDetails(any()) } returns pokemonDetailSchema

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(pokemon, result)

        coVerify { dao.updatePokemonDetail(pokemon) }
    }

    @Test
    fun `getPokemonDetails has receives HttpException and returns the pokemon details in dao`() = runTest {
        // arrange
        val pokemonName = "bulbasaur"
        val pokemon = TEST_POKEMON.copy(name = pokemonName)
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(pokemon)
        val mockedHttpException = mockk<HttpException>(relaxed = true)
        coEvery { pokedexApiService.getPokemonDetails(any()) } throws mockedHttpException

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(pokemon, result)

        // make sure nothing saved in dao when error occurs
        coVerify(exactly = 0) { dao.updatePokemonDetail(any()) }
    }

    @Test
    fun `getPokemonDetails has receives HttpException and returns no pokemon in dao`() = runTest {
        // arrange
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(null)
        val mockedHttpException = mockk<HttpException>(relaxed = true)
        coEvery { pokedexApiService.getPokemonDetails(any()) } throws mockedHttpException

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(null, result)

        // make sure nothing saved in dao when error occurs
        coVerify(exactly = 0) { dao.updatePokemonDetail(any()) }
    }

    @Test
    fun `getPokemonDetails has receives UnknownHostException and returns the pokemon details in dao`() = runTest {
        // arrange
        val pokemonName = "bulbasaur"
        val pokemon = TEST_POKEMON.copy(name = pokemonName)
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(pokemon)
        coEvery { pokedexApiService.getPokemonDetails(any()) } throws UnknownHostException()

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(pokemon, result)

        // make sure nothing saved in dao when error occurs
        coVerify(exactly = 0) { dao.updatePokemonDetail(any()) }
    }

    @Test
    fun `getPokemonDetails has receives UnknownHostException and returns no pokemon from dao`() = runTest {
        // arrange
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(null)
        coEvery { pokedexApiService.getPokemonDetails(any()) } throws UnknownHostException()

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(null, result)

        // make sure nothing saved in dao when error occurs
        coVerify(exactly = 0) { dao.updatePokemonDetail(any()) }
    }

    companion object {
        private const val POKEMON_NAME = "bulbasaur"

    }
}
