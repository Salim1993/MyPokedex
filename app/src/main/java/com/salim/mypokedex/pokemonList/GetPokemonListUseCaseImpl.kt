package com.salim.mypokedex.pokemonList

import com.salim.mypokedex.networking.PokedexApiService
import com.salim.mypokedex.utilities.SharedPreferencesWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class GetPokemonListUseCaseImpl @Inject constructor(
    private val service: PokedexApiService,
    private val preferences: SharedPreferencesWrapper
): GetPokemonListUseCase {

    private val _pokemonListFlow = MutableStateFlow(
        convertStringToList(preferences.getString(POKEMON_LIST_PREF))
    )

    override fun getPokemonListFlow(): StateFlow<List<String>> {
        return _pokemonListFlow.asStateFlow()
    }

    override suspend fun getPokemonList(number: Int, offset: Int) {
        try {
            withContext(Dispatchers.IO) {
                val result = service.getListOfPokemon(number, offset)
                val list = result.map { it.name }
                Timber.d("Pokemon got from api: $list")

                _pokemonListFlow.emit(list)
                preferences.saveString(POKEMON_LIST_PREF, convertListToString(list))
            }
        } catch (e: HttpException) {
            Timber.e("Got HttpException. Error in it was ${e.code()} : ${e.message()}")
        } catch (e: UnknownHostException) {
            //cant connect to host
            Timber.e("Got UnknownHostException. Could not connect to host.")
        }
    }

    private fun convertStringToList(string: String): List<String> {
        if(string.isEmpty())
            return emptyList()
        return string.split(", ")
    }

    private fun convertListToString(list: List<String>): String {
        return list.joinToString()
    }

    companion object {
        private const val POKEMON_LIST_PREF = "POKEMON_LIST_PREF"
    }
}
