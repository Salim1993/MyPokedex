package com.salim.mypokedex.pokemonList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {

    val pokemonList = pokemonListUseCase.pokemonListFlow

    init {
        getPokemonList()
    }

    private fun getPokemonList() = viewModelScope.launch {
        pokemonListUseCase.getOriginal151List()
    }
}