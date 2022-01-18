package com.salim.mypokedex.pokemonList

import android.content.SharedPreferences
import com.salim.mypokedex.networking.PokedexApiService
import com.salim.mypokedex.pokemon.PokemonDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    val service: PokedexApiService,
    val preferences: SharedPreferences
) {

    private val _pokemonListFlow = MutableStateFlow(
        convertStringToList(preferences.getString(POKEMON_LIST_PREF, "") ?: "")
    )
    val pokemonListFlow = _pokemonListFlow.asStateFlow()

    suspend fun getOriginal151List() {
        try {
            withContext(Dispatchers.IO) {
                val list = service.getListOfPokemon(151, 0).map { it.name }
                Timber.d("Pokemon got from api: ${list[0]}")

                _pokemonListFlow.emit(list)
                with(preferences.edit()) {
                    putString(POKEMON_LIST_PREF, convertListToString(list))
                    apply()
                }
            }
        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: UnknownHostException) {
            //cant connect to host
            e.printStackTrace()
        }
    }

    private fun convertStringToList(string: String): List<String> {
        return string.split(", ")
    }

    private fun convertListToString(list: List<String>): String {
        return list.joinToString()
    }

    companion object {
        private const val POKEMON_LIST_PREF = "POKEMON_LIST_PREF"
    }
}