package com.salim.mypokedex.pokemon

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT name FROM pokemon")
    fun getPokemonList(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(list: List<Pokemon>)

    @Query("SELECT * FROM pokemon WHERE name = :pokemonName")
    fun getSpecificPokemon(pokemonName: String): Flow<Pokemon?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePokemonDetail(pokemon: Pokemon)
}
