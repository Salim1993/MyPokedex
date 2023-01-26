package com.salim.mypokedex.pokemonList

import com.salim.mypokedex.CustomMainDispatcherRule
import com.salim.mypokedex.pokemon.PokemonNameAndId
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test



@OptIn(ExperimentalCoroutinesApi::class)
internal class PokemonListViewModelTest {

    @get:Rule
    val customMainDispatcherRule = CustomMainDispatcherRule()

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
        viewModel.getPokemonList()

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
        viewModel.getPokemonList()

        //assert
        assertEquals(expectedList, viewModel.pokemonList.value)
    }

    // Running into issues with this test becuase it seems coEvery is broken
    // when using certain version of coroutines library.
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
    fun `setNewPokemonLimit, sets incorrect upper limits, make sure upper limit error events triggered and no new call to api`() = runTest {
        // assert (Needed so object we testing don't crash)
        val expectedList = emptyList<String>()
        every { getPokemonListUseCase.getPokemonListFlow() } returns MutableStateFlow(expectedList)

        // act
        val fakeGetPokemonListUseCase = FakeGetPokemonListUseCase()
        val viewModel = PokemonListViewModel(fakeGetPokemonListUseCase)
        viewModel.setNewPokemonLimit(PokemonListViewModel.ABSOLUTE_LOWER_LIMIT, PokemonListViewModel.ABOVE_RANGE)

        // assert
        // make sure error events are fired
        val upperLimitEventResult = viewModel.isUpperLimitToHighFlow.first()

        assertTrue(upperLimitEventResult)

        // make api is not called
        assertFalse(fakeGetPokemonListUseCase.isGetPokemonListCalled)
    }

    @Test
    fun `setNewPokemonLimit, sets incorrect lower limits, make sure lower limit error events triggered and no new call to api`() = runTest {
        // assert (Needed so object we testing don't crash)
        val expectedList = emptyList<String>()
        every { getPokemonListUseCase.getPokemonListFlow() } returns MutableStateFlow(expectedList)

        // act
        val fakeGetPokemonListUseCase = FakeGetPokemonListUseCase()
        val viewModel = PokemonListViewModel(fakeGetPokemonListUseCase)
        viewModel.setNewPokemonLimit(PokemonListViewModel.BELOW_RANGE, PokemonListViewModel.ABSOLUTE_UPPER_LIMIT)

        // assert
        // make sure error events are fired
        val lowerLimitEventResult = viewModel.isLowerLimitToLowFlow.first()

        assertTrue(lowerLimitEventResult)

        // make api is not called
        assertFalse(fakeGetPokemonListUseCase.isGetPokemonListCalled)
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

    @Test
    fun `setNewPokemonLimit with offset, make sure pokemon returned have correct id due to offset`() = runTest {
        // arrange
        val pokemonList = listOf("Bulbasaur", "Ivysaur", "Venusaur")
        val newPokemonList = listOf("charmeleon", "charizard", "squirtle")
        val mutableStateFlow = MutableStateFlow(pokemonList)
        every { getPokemonListUseCase.getPokemonListFlow() } returns mutableStateFlow

        //act
        val viewModel = PokemonListViewModel(getPokemonListUseCase)
        val lowerLimit = 5
        val job = viewModel.setNewPokemonLimit(lowerLimit, 7)
        job.join()

        //refresh flow to trigger new list
        val job2 = launch {
            mutableStateFlow.emit(newPokemonList)
        }
        job2.join()

        //assert
        val expectedList = newPokemonList.mapIndexed { index, name ->
            PokemonNameAndId(index + lowerLimit, name)
        }
        val actual = viewModel.pokemonList.value
        assertEquals(expectedList, actual)
    }
}
