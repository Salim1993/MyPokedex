package com.salim.mypokedex.networking

import com.salim.mypokedex.pokemon.PokemonDetailSchema
import com.salim.mypokedex.pokemon.PokemonListItemSchema
import com.serjltt.moshi.adapters.Wrapped
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexApiService {

    @GET("pokemon")
    @Wrapped(path = ["results"])
    suspend fun getListOfPokemon(@Query("limit") number: Int, @Query("offset") offset: Int): List<PokemonListItemSchema>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): PokemonDetailSchema
}
