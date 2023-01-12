package com.salim.mypokedex.pokemonDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
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