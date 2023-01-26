package com.salim.mypokedex.pokemonDetails

import com.salim.mypokedex.CustomMainDispatcherRule
import com.salim.mypokedex.PokemonTestData.TEST_POKEMON
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailsViewModelTest {

    @get:Rule
    val customMainDispatcherRule = CustomMainDispatcherRule()

    @RelaxedMockK
    lateinit var getPokemonDetailsUseCase: GetPokemonDetailsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getPokemonDetailsFlow is successful and gets pokemon details in flow`() = runTest {
        // arrange
        val expected = TEST_POKEMON.copy(name = "bulbasaur")
        coEvery { getPokemonDetailsUseCase.pokemonListFlow } returns MutableStateFlow(expected)

        // act
        val unitToTest = PokemonDetailsViewModel(getPokemonDetailsUseCase)

        // assert
        val actualResult = unitToTest.pokemonDetailsFlow.first()
        assertEquals(expected, actualResult)
    }

    @Test
    fun `getPokemonDetailsFlow is unsuccessful and returns no pokemon`() = runTest {
        // arrange
        val expected = null
        coEvery { getPokemonDetailsUseCase.pokemonListFlow } returns MutableStateFlow(expected)

        // act
        val unitToTest = PokemonDetailsViewModel(getPokemonDetailsUseCase)

        // assert
        val actualResult = unitToTest.pokemonDetailsFlow.first()
        assertEquals(expected, actualResult)
    }
}
