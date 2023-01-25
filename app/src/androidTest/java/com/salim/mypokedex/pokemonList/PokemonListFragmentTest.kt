package com.salim.mypokedex.pokemonList

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.salim.mypokedex.MainActivity
import com.salim.mypokedex.R
import com.salim.mypokedex.testHelpers.EspressoUtilities.atPosition
import com.salim.mypokedex.testHelpers.EspressoUtilities.typeSearchViewText
import com.salim.mypokedex.utilities.EspressoIdlingResourceRule
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
class PokemonListFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

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

    @Test
    fun testRangeDialog_OpeningUpRangeDialog_makeSureCurrentMinAndMaxOnRangeIsCorrect() {
        val context = getInstrumentation().targetContext
        openActionBarOverflowOrOptionsMenu(context)
        val changeRangeMenuString = context.getString(R.string.action_change_pokemon_range)
        onView(withText(changeRangeMenuString)).perform(click())

        onView(withId(R.id.lower_limit_edit_text)).check(matches(withText(containsString("1"))))
        onView(withId(R.id.upper_limit_edit_text)).check(matches(withText(containsString("151"))))
    }

    @Test
    fun testChangeRange_OpeningUpRangeDialogAndEnterNewRange_makeSureAdapterHasOnlyCertainPokemon() {
        val context = getInstrumentation().targetContext
        openActionBarOverflowOrOptionsMenu(context)
        val changeRangeMenuString = context.getString(R.string.action_change_pokemon_range)
        onView(withText(changeRangeMenuString)).perform(click())

        onView(withId(R.id.lower_limit_edit_text)).perform(replaceText("5"))
        onView(withId(R.id.upper_limit_edit_text)).perform(replaceText("7"))

        val okString = context.getString(R.string.ok)
        onView(withText(okString)).perform(click())

        onView(withId(R.id.pokemon_list)).check(matches(atPosition(0, hasDescendant(withText("charmeleon")))))
        onView(withId(R.id.pokemon_list)).check(matches(atPosition(2, hasDescendant(withText("squirtle")))))

        onView(withText("charmander")).check(doesNotExist())
        onView(withText("wartortle")).check(doesNotExist())
    }

    @Test
    fun testChangeRange_OpeningUpRangeDialogAndEnterIncorrectLowerLimitOnRange_lowerLimitErrorDialogPopsUp() {
        val context = getInstrumentation().targetContext
        openActionBarOverflowOrOptionsMenu(context)
        val changeRangeMenuString = context.getString(R.string.action_change_pokemon_range)
        onView(withText(changeRangeMenuString)).perform(click())

        onView(withId(R.id.lower_limit_edit_text)).perform(replaceText(PokemonListViewModel.BELOW_RANGE.toString()))

        val okString = context.getString(R.string.ok)
        onView(withText(okString)).perform(click())

        val errorMessageString = context.getString(R.string.pokemon_list_lower_limit_error)
        onView(withText(errorMessageString)).check(matches(isDisplayed()))
    }

    @Test
    fun testChangeRange_OpeningUpRangeDialogAndEnterIncorrectUpperLimitOnRange_upperLimitErrorDialogPopsUp() {
        val context = getInstrumentation().targetContext
        openActionBarOverflowOrOptionsMenu(context)
        val changeRangeMenuString = context.getString(R.string.action_change_pokemon_range)
        onView(withText(changeRangeMenuString)).perform(click())

        onView(withId(R.id.upper_limit_edit_text)).perform(replaceText(PokemonListViewModel.ABOVE_RANGE.toString()))

        val okString = context.getString(R.string.ok)
        onView(withText(okString)).perform(click())

        val errorMessageString = context.getString(R.string.pokemon_list_upper_limit_error)
        onView(withText(errorMessageString)).check(matches(isDisplayed()))
    }
}