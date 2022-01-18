package com.salim.mypokedex.networking

import com.salim.mypokedex.pokemon.PokemonDetailSchema
import com.salim.mypokedex.pokemon.PokemonListItemSchema
import com.serjltt.moshi.adapters.Wrapped
import retrofit2.http.GET

interface PokedexApiService {

    @GET("pokemon?limit={number}&offset={offset}")
    @Wrapped(path = ["results"])
    fun getListOfPokemon(number: Int, offset: Int): List<PokemonListItemSchema>

    @GET("pokemon/{name}")
    fun getPokemonDetails(name: String): PokemonDetailSchema
}