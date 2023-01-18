package com.salim.mypokedex.pokemonList

import kotlinx.coroutines.flow.StateFlow

interface GetPokemonListUseCase {

    fun getPokemonListFlow(): StateFlow<List<String>>
    suspend fun getPokemonList(number: Int, offset: Int)
}