package com.salim.mypokedex.di

import com.salim.mypokedex.networking.PokedexApiService
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    private const val POKEDEX_URL = "https://pokeapi.co/api/v2/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder().add(Wrapped.ADAPTER_FACTORY).add(KotlinJsonAdapterFactory()).build()
        //val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(POKEDEX_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun getPokedexApiService(retrofit: Retrofit): PokedexApiService {
        return retrofit.create(PokedexApiService::class.java)
    }
}