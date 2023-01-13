package com.salim.mypokedex.pokemonDetails

import dagger.assisted.AssistedFactory

@AssistedFactory
interface GetPokemonDetailsUseCaseFactory {

    fun create(pokemonName: String): GetPokemonDetailsUseCase
}