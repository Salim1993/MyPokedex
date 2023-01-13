package com.salim.mypokedex.pokemonDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokemonDetailsViewModelFactory constructor(
        private val pokemonName: String,
        private val getPokemonDetailsUseCaseFactory: GetPokemonDetailsUseCaseFactory
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonDetailsViewModel::class.java)) {
            val getPokemonDetailsUseCase = getPokemonDetailsUseCaseFactory.create(pokemonName)
            return PokemonDetailsViewModel(getPokemonDetailsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}