package com.salim.mypokedex.pokemonList

import com.salim.mypokedex.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test



@OptIn(ExperimentalCoroutinesApi::class)
internal class PokemonListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getPokemonList should retrieve the correct list of Pokemon`() = runTest {
        // arrange
        val expectedList = listOf("Bulbasaur", "Ivysaur", "Venusaur")
        every { getPokemonListUseCase.pokemonListFlow } returns MutableStateFlow(expectedList)

        //act
        val viewModel = PokemonListViewModel(getPokemonListUseCase)

        //assert
        assertEquals(expectedList, viewModel.pokemonList.value)
    }

    @Test
    fun `getPokemonList use case has error so return empty list`() = runTest {
        // arrange
        val expectedList = emptyList<String>()
        every { getPokemonListUseCase.pokemonListFlow } returns MutableStateFlow(expectedList)

        //act
        val viewModel = PokemonListViewModel(getPokemonListUseCase)

        //assert
        assertEquals(expectedList, viewModel.pokemonList.value)
    }
}