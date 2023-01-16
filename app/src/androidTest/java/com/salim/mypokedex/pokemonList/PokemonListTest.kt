package com.salim.mypokedex.pokemonList

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.salim.mypokedex.MainActivity
import com.salim.mypokedex.R
import com.salim.mypokedex.testHelpers.EspressoUtilities.atPosition
import com.salim.mypokedex.testHelpers.EspressoUtilities.typeSearchViewText
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PokemonListTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun clickBulbasaurInPokemonList_GoToPokemonDetails_CheckIfTextNameIsCorrect() {
        onView(withId(R.id.pokemon_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PokemonListAdapter.PokemonItemViewHolder>(0, click()))

        onView(withId(R.id.text_name)).check(matches(withText(containsString("bulbasaur"))))
    }

    @Test
    fun testSearchBarInPokemonList_EnterCaterpie_MakeSureCaterpieVisibleInList() {
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(R.id.action_search)).perform(typeSearchViewText("caterpie"))

        onView(withId(R.id.pokemon_list)).check(matches(atPosition(0, hasDescendant(withText("caterpie")))))
    }
}