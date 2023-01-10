package com.salim.mypokedex.pokemonDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokemonDetailViewModelFactory(private val pokemonName: String) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonDetailViewModelFactory::class.java)) {
            return PokemonDetailViewModelFactory(pokemonName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}