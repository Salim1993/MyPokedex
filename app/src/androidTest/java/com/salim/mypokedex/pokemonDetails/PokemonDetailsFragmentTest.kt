package com.salim.mypokedex.pokemonDetails

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.salim.mypokedex.R
import com.salim.mypokedex.testHelpers.launchFragmentInHiltContainer
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonDetailsFragmentTest {

    @Test
    fun test_checkIfPokemonDetailsHasCorrectInfoFromArguments() {
        val name = "bulbasaur"
        val bundle = bundleOf("pokemonName" to name)

        val scenario = launchFragmentInHiltContainer<PokemonDetailsFragment>(bundle)

        Espresso.onView(ViewMatchers.withId(R.id.text_name))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("bulbasaur"))))
    }
}