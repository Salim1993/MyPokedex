package com.salim.mypokedex.pokemonDetails

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.salim.mypokedex.R
import com.salim.mypokedex.testHelpers.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PokemonDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun test_checkIfPokemonDetailsHasCorrectInfoFromArguments() {
        val name = "bulbasaur"
        val bundle = bundleOf("pokemonName" to name)

        launchFragmentInHiltContainer<PokemonDetailsFragment>(bundle)

        Espresso.onView(ViewMatchers.withId(R.id.text_name))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("bulbasaur"))))
    }
}