package com.salim.mypokedex.pokemon

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailSchema(
    @Json(name = "abilities")
    val abilities: List<Ability>,
    @Json(name = "base_experience")
    val baseExperience: Int,
    @Json(name = "forms")
    val forms: List<Form>,
    @Json(name = "game_indices")
    val gameIndices: List<GameIndex>,
    @Json(name = "height")
    val height: Int,
    @Json(name = "held_items")
    val heldItems: List<HeldItem>,
    @Json(name = "id")
    val id: Int,
    @Json(name = "is_default")
    val isDefault: Boolean,
    @Json(name = "location_area_encounters")
    val locationAreaEncounters: String,
    @Json(name = "moves")
    val moves: List<Move>,
    @Json(name = "name")
    val name: String,
    @Json(name = "order")
    val order: Int,
    @Json(name = "past_types")
    val pastTypes: List<PastType>,
    @Json(name = "species")
    val species: Species,
    @Json(name = "sprites")
    val sprites: Sprites,
    @Json(name = "stats")
    val stats: List<Stat>,
    @Json(name = "types")
    val types: List<TypeXX>,
    @Json(name = "weight")
    val weight: Int
) {
    companion object {
        fun createPokemon(detail: PokemonDetailSchema): Pokemon {
            return Pokemon(
                baseExperience = detail.baseExperience,
                height = detail.height,
                id = detail.id,
                isDefault = detail.isDefault,
                locationAreaEncounters = detail.locationAreaEncounters,
                name = detail.name,
                order = detail.order,
                weight = detail.weight
            )
        }
    }
}