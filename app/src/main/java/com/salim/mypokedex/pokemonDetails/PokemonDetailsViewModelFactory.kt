package com.salim.mypokedex.pokemonDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class PokemonDetailViewModelFactory constructor(
        private val pokemonName: String,
        private val getPokemonDetailsUseCaseFactory: GetPokemonDetailsUseCaseFactory
    ) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonDetailViewModelFactory::class.java)) {
            val getPokemonDetailsUseCase = getPokemonDetailsUseCaseFactory.create(pokemonName)
            return PokemonDetailsViewModel(getPokemonDetailsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}