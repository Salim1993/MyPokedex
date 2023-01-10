package com.salim.mypokedex.pokemonDetails

import com.salim.mypokedex.networking.PokedexApiService
import com.salim.mypokedex.pokemon.PokemonDao
import com.salim.mypokedex.pokemon.PokemonDetailSchema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class GetPokemonDetailsUseCase @Inject constructor(
    private val service: PokedexApiService,
    private val dao: PokemonDao,
    private val pokemonName: String
) {

    private val pokemonListFlow = dao.getSpecificPokemon(pokemonName)

    suspend fun getPokemonDetails() {
        try {
            withContext(Dispatchers.IO) {
                val pokemonDetailSchema = service.getPokemonDetails(pokemonName)
                Timber.d("Pokemon got from api: $pokemonDetailSchema")

                dao.updatePokemonDetail(pokemon = PokemonDetailSchema.createPokemon(pokemonDetailSchema))
            }
        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: UnknownHostException) {
            //cant connect to host
            Timber.e("Got UnknownHostException. Could not connect to host.")
        }
    }
}