package com.salim.mypokedex.pokemonDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salim.mypokedex.pokemon.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
        private val pokemonDetailsUseCase: GetPokemonDetailsUseCase
    ): ViewModel() {

    private val _pokemonDetailsFlow = MutableSharedFlow<Pokemon>()
    val pokemonDetailsFlow = _pokemonDetailsFlow.asSharedFlow()

    init {
        getPokemonDetail()
    }

    private fun  getPokemonDetail() = viewModelScope.launch {
        pokemonDetailsUseCase.getPokemonDetails()
    }
}