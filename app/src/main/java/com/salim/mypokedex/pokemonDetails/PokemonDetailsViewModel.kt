package com.salim.mypokedex.pokemonDetails

import androidx.lifecycle.ViewModel
import com.salim.mypokedex.pokemon.Pokemon
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class PokemonDetailsViewModel(val pokemonName: String) : ViewModel() {

    private val _pokemonDetailsFlow = MutableSharedFlow<Pokemon>()
    val pokemonDetailsFlow = _pokemonDetailsFlow.asSharedFlow()

    val getPokemonDetailsUseCase = GetPokemonDetailsUseCase(pokemonName)

    init {

    }
}