package com.salim.mypokedex.pokemon

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

data class PokemonListItemSchema(
    val name: String,
    val url: String)


