package com.salim.mypokedex.testHelpers

import com.salim.mypokedex.pokemon.*

object PokemonTestData {

    val TEST_POKEMON = Pokemon(
        baseExperience = 0,
        height = 0,
        id = 0,
        isDefault = false,
        locationAreaEncounters = "",
        name = "",
        order = 0,
        weight = 0
    )

    val TEST_POKEMON_DETAIL_SCHEMA: PokemonDetailSchema = PokemonDetailSchema(
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