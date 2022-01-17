package com.salim.mypokedex.di

import com.salim.mypokedex.networking.PokedexApiService
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object NetworkingModule {

    private const val POKEDEX_URL = "https://pokeapi.co/api/v2/"

    @Provides
    fun provideRetrofit(): PokedexApiService {
        val moshi = Moshi.Builder().add(Wrapped.ADAPTER_FACTORY).build()

        return Retrofit.Builder()
            .baseUrl(POKEDEX_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PokedexApiService::class.java)
    }
}