package com.salim.mypokedex

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.salim.mypokedex.pokemonList.PokemonListAdapter
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
    fun clickBulbasaurAndCheckPokemonDetails() {
        onView(withId(R.id.pokemon_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PokemonListAdapter.PokemonItemViewHolder>(0, click()))

        onView(withId(R.id.text_name)).check(matches(withText(containsString("bulbasaur"))))
    }
}