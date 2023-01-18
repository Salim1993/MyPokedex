package com.salim.mypokedex.pokemonList

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeGetPokemonListUseCase: GetPokemonListUseCase {

    var isGetPokemonListCalled = false
    var number: Int? = null
    var offset: Int? = null

    override fun getPokemonListFlow(): StateFlow<List<String>> {
        return MutableStateFlow(emptyList())
    }

    override suspend fun getPokemonList(number: Int, offset: Int) {
        isGetPokemonListCalled = true
        this.number = number
        this.offset = offset
    }
}