package com.salim.mypokedex.pokemonDetails

import com.salim.mypokedex.networking.PokedexApiService
import com.salim.mypokedex.pokemon.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonDetailsUseCaseTest {

    @MockK
    lateinit var pokedexApiService: PokedexApiService

    @RelaxedMockK
    lateinit var dao: PokemonDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getPokemonDetails is successful and returns the pokemon details`() = runTest {
        // arrange
        val pokemonName = "bulbasaur"
        val pokemonDetailSchema = TEST_POKEMON_DETAIL_SCHEMA.copy(name = pokemonName)
        val pokemon = TEST_POKEMON.copy(name = pokemonName)
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(pokemon)
        coEvery { pokedexApiService.getPokemonDetails(any()) } returns pokemonDetailSchema

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(pokemon, result)

        coVerify { dao.updatePokemonDetail(pokemon) }
    }

    @Test
    fun `getPokemonDetails has receives HttpException and returns the pokemon details in dao`() = runTest {
        // arrange
        val pokemonName = "bulbasaur"
        val pokemon = TEST_POKEMON.copy(name = pokemonName)
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(pokemon)
        val mockedHttpException = mockk<HttpException>(relaxed = true)
        coEvery { pokedexApiService.getPokemonDetails(any()) } throws mockedHttpException

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(pokemon, result)

        // make sure nothing saved in dao when error occurs
        coVerify(exactly = 0) { dao.updatePokemonDetail(any()) }
    }

    @Test
    fun `getPokemonDetails has receives HttpException and returns no pokemon in dao`() = runTest {
        // arrange
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(null)
        val mockedHttpException = mockk<HttpException>(relaxed = true)
        coEvery { pokedexApiService.getPokemonDetails(any()) } throws mockedHttpException

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(null, result)

        // make sure nothing saved in dao when error occurs
        coVerify(exactly = 0) { dao.updatePokemonDetail(any()) }
    }

    @Test
    fun `getPokemonDetails has receives UnknownHostException and returns the pokemon details in dao`() = runTest {
        // arrange
        val pokemonName = "bulbasaur"
        val pokemon = TEST_POKEMON.copy(name = pokemonName)
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(pokemon)
        coEvery { pokedexApiService.getPokemonDetails(any()) } throws UnknownHostException()

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(pokemon, result)

        // make sure nothing saved in dao when error occurs
        coVerify(exactly = 0) { dao.updatePokemonDetail(any()) }
    }

    @Test
    fun `getPokemonDetails has receives UnknownHostException and returns no pokemon from dao`() = runTest {
        // arrange
        every { dao.getSpecificPokemon(any()) } returns MutableStateFlow(null)
        coEvery { pokedexApiService.getPokemonDetails(any()) } throws UnknownHostException()

        // act
        val unitToTest = GetPokemonDetailsUseCase(
            pokedexApiService,
            dao,
            POKEMON_NAME
        )
        unitToTest.getPokemonDetails()

        // assert
        val result = unitToTest.pokemonListFlow.first()
        assertEquals(null, result)

        // make sure nothing saved in dao when error occurs
        coVerify(exactly = 0) { dao.updatePokemonDetail(any()) }
    }

    companion object {
        private const val POKEMON_NAME = "bulbasaur"

        private val TEST_POKEMON = Pokemon(
            baseExperience = 0,
            height = 0,
            id = 0,
            isDefault = false,
            locationAreaEncounters = "",
            name = "",
            order = 0,
            weight = 0
        )

        private val TEST_POKEMON_DETAIL_SCHEMA: PokemonDetailSchema = PokemonDetailSchema(
            abilities = listOf(),
            baseExperience = 0,
            forms = listOf(),
            gameIndices = listOf(),
            height = 0,
            heldItems = listOf(),
            id = 0,
            isDefault = false,
            locationAreaEncounters = "",
            moves = listOf(),
            name = "",
            order = 0,
            pastTypes = listOf(),
            species = Species(name = "", url = ""),
            sprites = Sprites(
                backDefault = "",
                backFemale = null,
                backShiny = "",
                backShinyFemale = null,
                frontDefault = "",
                frontFemale = null,
                frontShiny = "",
                frontShinyFemale = null,
                other = Other(
                    dreamWorld = DreamWorld(
                        frontDefault = "",
                        frontFemale = null
                    ), home = Home(
                        frontDefault = "",
                        frontFemale = null,
                        frontShiny = "",
                        frontShinyFemale = null
                    ), officialArtwork = OfficialArtwork(frontDefault = "")
                ),
                versions = Versions(
                    generationI = GenerationI(
                        redBlue = RedBlue(
                            backDefault = "",
                            backGray = "",
                            frontDefault = "",
                            frontGray = ""
                        ), yellow = Yellow(
                            backDefault = "",
                            backGray = "",
                            frontDefault = "",
                            frontGray = ""
                        )
                    ), generationIi = GenerationIi(
                        crystal = Crystal(
                            backDefault = "",
                            backShiny = "",
                            frontDefault = "",
                            frontShiny = ""
                        ), gold = Gold(
                            backDefault = "",
                            backShiny = "",
                            frontDefault = "",
                            frontShiny = ""
                        ), silver = Silver(
                            backDefault = "",
                            backShiny = "",
                            frontDefault = "",
                            frontShiny = ""
                        )
                    ), generationIii = GenerationIii(
                        emerald = Emerald(frontDefault = "", frontShiny = ""),
                        fireredLeafgreen = FireredLeafgreen(
                            backDefault = "",
                            backShiny = "",
                            frontDefault = "",
                            frontShiny = ""
                        ),
                        rubySapphire = RubySapphire(
                            backDefault = "",
                            backShiny = "",
                            frontDefault = "",
                            frontShiny = ""
                        )
                    ), generationIv = GenerationIv(
                        diamondPearl = DiamondPearl(
                            backDefault = "",
                            backFemale = null,
                            backShiny = "",
                            backShinyFemale = null,
                            frontDefault = "",
                            frontFemale = null,
                            frontShiny = "",
                            frontShinyFemale = null
                        ), heartgoldSoulsilver = HeartgoldSoulsilver(
                            backDefault = "",
                            backFemale = null,
                            backShiny = "",
                            backShinyFemale = null,
                            frontDefault = "",
                            frontFemale = null,
                            frontShiny = "",
                            frontShinyFemale = null
                        ), platinum = Platinum(
                            backDefault = "",
                            backFemale = null,
                            backShiny = "",
                            backShinyFemale = null,
                            frontDefault = "",
                            frontFemale = null,
                            frontShiny = "",
                            frontShinyFemale = null
                        )
                    ), generationV = GenerationV(
                        blackWhite = BlackWhite(
                            animated = Animated(
                                backDefault = "",
                                backFemale = null,
                                backShiny = "",
                                backShinyFemale = null,
                                frontDefault = "",
                                frontFemale = null,
                                frontShiny = "",
                                frontShinyFemale = null
                            ),
                            backDefault = "",
                            backFemale = null,
                            backShiny = "",
                            backShinyFemale = null,
                            frontDefault = "",
                            frontFemale = null,
                            frontShiny = "",
                            frontShinyFemale = null
                        )
                    ), generationVi = GenerationVi(
                        omegarubyAlphasapphire = OmegarubyAlphasapphire(
                            frontDefault = "",
                            frontFemale = null,
                            frontShiny = "",
                            frontShinyFemale = null
                        ), xY = XY(
                            frontDefault = "",
                            frontFemale = null,
                            frontShiny = "",
                            frontShinyFemale = null
                        )
                    ), generationVii = GenerationVii(
                        icons = Icons(frontDefault = "", frontFemale = null),
                        ultraSunUltraMoon = UltraSunUltraMoon(
                            frontDefault = "",
                            frontFemale = null,
                            frontShiny = "",
                            frontShinyFemale = null
                        )
                    ), generationViii = GenerationViii(
                        icons = IconsX(
                            frontDefault = "",
                            frontFemale = null
                        )
                    )
                )
            ),
            stats = listOf(),
            types = listOf(),
            weight = 0
        )
    }
}