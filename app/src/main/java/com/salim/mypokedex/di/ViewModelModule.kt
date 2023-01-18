package com.salim.mypokedex.di

import com.salim.mypokedex.networking.PokedexApiService
import com.salim.mypokedex.pokemonList.GetPokemonListUseCase
import com.salim.mypokedex.pokemonList.GetPokemonListUseCaseImpl
import com.salim.mypokedex.utilities.SharedPreferencesWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideGetPokemonListUseCase(
        service: PokedexApiService,
        preferences: SharedPreferencesWrapper
    ): GetPokemonListUseCase {
        return GetPokemonListUseCaseImpl(service, preferences)
    }
}