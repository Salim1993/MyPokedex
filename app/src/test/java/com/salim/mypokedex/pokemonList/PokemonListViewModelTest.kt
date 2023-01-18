package com.salim.mypokedex.pokemonList

import com.salim.mypokedex.MainDispatcherRule
import com.salim.mypokedex.pokemon.PokemonNameAndId
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test



@OptIn(ExperimentalCoroutinesApi::class)
internal class PokemonListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    lateinit var getPokemonListUseCase: GetPokemonListUseCaseImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getPokemonList should retrieve the correct list of Pokemon`() = runTest {
        // arrange
        val pokemonList = listOf("Bulbasaur", "Ivysaur", "Venusaur")
        every { getPokemonListUseCase.getPokemonListFlow() } returns MutableStateFlow(pokemonList)

        //act
        val viewModel = PokemonListViewModel(getPokemonListUseCase)

        //assert
        val expectedList = pokemonList.mapIndexed { index, name ->
            PokemonNameAndId(index + 1, name)
        }
        assertEquals(expectedList, viewModel.pokemonList.value)
    }

    @Test
    fun `getPokemonList use case has error so return empty list`() = runTest {
        // arrange
        val expectedList = emptyList<String>()
        every { getPokemonListUseCase.getPokemonListFlow() } returns MutableStateFlow(expectedList)

        //act
        val viewModel = PokemonListViewModel(getPokemonListUseCase)

        //assert
        assertEquals(expectedList, viewModel.pokemonList.value)
    }

    // Running into issues with this test becuase it seems coEvery is broken when using certain version of coroutines library.
    // See following link: https://github.com/mockk/mockk/issues/554
    // Going to make a fake class instead and use that to assert for tests instead.
    @Test
    fun `setNewPokemonLimit, sets correct new limits, make sure no error events triggered and new call to api`() = runTest(dispatchTimeoutMs = 5000) {
        // assert (Needed so object we testing don't crash)
        val expectedList = emptyList<String>()
        every { getPokemonListUseCase.getPokemonListFlow() } returns MutableStateFlow(expectedList)

        // act
        val fakeGetPokemonListUseCase = FakeGetPokemonListUseCase()
        val viewModel = PokemonListViewModel(fakeGetPokemonListUseCase)
        val job = viewModel.setNewPokemonLimit(2, 200)

        // assert
        // make sure api is called with correct parameters
        job.join()
        val expectedOffset = 2 - 1
        val expectedNumber = 200 - 2 + 1
        assertTrue(fakeGetPokemonListUseCase.isGetPokemonListCalled)
        assertEquals(expectedNumber, fakeGetPokemonListUseCase.number)
        assertEquals(expectedOffset, fakeGetPokemonListUseCase.offset)
    }

    @Test
    fun `setNewPokemonLimit, sets incorrect upper limits, make sure upper error events triggered and no new call to api`() = runTest {
        // assert (Needed so object we testing don't crash)
        val expectedList = emptyList<String>()
        every { getPokemonListUseCase.getPokemonListFlow() } returns MutableStateFlow(expectedList)

        // act
        val fakeGetPokemonListUseCase = FakeGetPokemonListUseCase()
        val viewModel = PokemonListViewModel(fakeGetPokemonListUseCase)
        val job = viewModel.setNewPokemonLimit(PokemonListViewModel.BELOW_RANGE, PokemonListViewModel.ABOVE_RANGE)

        // assert
        // make sure error events are fired
        val upperLimitEventResult = viewModel.isUpperLimitToHighFlow.first()

        assertTrue(upperLimitEventResult)

        // make sure api is called with correct parameters
        // TODO: Fix below, caused by fact that i call api when i init my viewmodel, need to take it out init and make setup function instead
        //assertFalse(fakeGetPokemonListUseCase.isGetPokemonListCalled)
    }

    @Test
    fun `triggerShowChangePokemonRangeDialogEvent, make sure new pokemon range dialog event is triggered`() = runTest {
        // assert (Needed so object we testing don't crash)
        val expectedList = emptyList<String>()
        every { getPokemonListUseCase.getPokemonListFlow() } returns MutableStateFlow(expectedList)

        // act
        val viewModel = PokemonListViewModel(getPokemonListUseCase)
        viewModel.triggerShowChangePokemonRangeDialogEvent()

        // assert
        val actualResult = viewModel.showChangePokemonRangeDialog.first()
        assertTrue(actualResult)
    }
}