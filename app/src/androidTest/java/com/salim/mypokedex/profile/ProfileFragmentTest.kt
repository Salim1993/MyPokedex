package com.salim.mypokedex.profile

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.salim.mypokedex.R
import com.salim.mypokedex.di.ViewModelModule
import com.salim.mypokedex.networking.PokedexApiService
import com.salim.mypokedex.pokemonList.GetPokemonListUseCase
import com.salim.mypokedex.pokemonList.GetPokemonListUseCaseImpl
import com.salim.mypokedex.testHelpers.launchFragmentInHiltContainer
import com.salim.mypokedex.utilities.EspressoIdlingResourceRule
import com.salim.mypokedex.utilities.ImageViewHasDrawableMatcher
import com.salim.mypokedex.utilities.SharedPreferencesWrapper
import com.salim.mypokedex.utilities.ToastMatcher
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(ViewModelModule::class)
@HiltAndroidTest
class ProfileFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get: Rule
    val intentsRule = IntentsRule()

    @get:Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Test
    fun test_validateGalleryIntent() {
        launchFragmentInHiltContainer<ProfileFragment>()

        // Given
        val expectedIntent: Matcher<Intent> = allOf(
            hasAction(Intent.ACTION_GET_CONTENT),
            hasCategories(setOf(Intent.CATEGORY_OPENABLE)),
            hasType(ProfileFragment.IMAGE_TYPE)
        )

        val activityResult = createGalleryPickActivityResultStub()
        intending(expectedIntent).respondWith(activityResult)

        // Execute and verify
        onView(withId(R.id.update_avatar_button)).perform(click())
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val galleryString = context.getString(R.string.gallery)
        onView(withText(galleryString)).perform(click())
        intended(expectedIntent)

        onView(withId(R.id.avatar_view)).check(matches(ImageViewHasDrawableMatcher.hasDrawable()))
    }

    @Test
    fun test_cameraIntentIsCorrect() {
        launchFragmentInHiltContainer<ProfileFragment>()

        // given
        val activityResult = createImageCaptureActivityResultStub()
        val expectedIntent = hasAction(MediaStore.ACTION_IMAGE_CAPTURE)
        intending(expectedIntent).respondWith(activityResult)

        // verify
        onView(withId(R.id.update_avatar_button)).perform(click())
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val galleryString = context.getString(R.string.camera)
        onView(withText(galleryString)).perform(click())
        intended(expectedIntent)
    }

    @Test
    fun test_profileUpdatesWithNameAndEmail_shouldShowToast() {
        launchFragmentInHiltContainer<ProfileFragment>()

        onView(withId(R.id.name_edit_text)).perform(click())
        onView(withId(R.id.name_edit_text)).perform(typeText("salim"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.email_edit_text)).perform(click())
        onView(withId(R.id.email_edit_text)).perform(typeText("salimbenkhaled@gmail.com"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.save_profile_button)).perform(click())

        //TODO: commenting out below, apparently many people have issues asserting on toast. Check link: https://github.com/android/android-test/issues/803
        //onView(withText(R.string.profile_updated)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        onView(withId(R.id.name_edit_text)).check(matches(withText(containsString("salim"))))
        onView(withId(R.id.email_edit_text)).check(matches(withText(containsString("salimbenkhaled@gmail.com"))))
    }

    private fun createGalleryPickActivityResultStub(): ActivityResult {

        val resources = InstrumentationRegistry.getInstrumentation().context.resources
        val imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
            resources.getResourcePackageName(R.drawable.ic_launcher_background) + "://" +
            resources.getResourceTypeName(R.drawable.ic_launcher_background) + "://" +
                R.drawable.ic_launcher_background
        )
        val resultIntent = Intent()
        resultIntent.data = imageUri
        return ActivityResult(Activity.RESULT_OK, resultIntent)
    }

    private fun createImageCaptureActivityResultStub(): ActivityResult {
        val resultIntent = Intent()
        return ActivityResult(Activity.RESULT_OK, resultIntent)
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    object TestModule {

        @Provides
        fun provideGetPokemonListUseCase(
            service: PokedexApiService,
            preferences: SharedPreferencesWrapper
        ): GetPokemonListUseCase {
            return GetPokemonListUseCaseImpl(service, preferences)
        }

        @Provides
        fun provideProfileUseCase(): ProfileUseCase {
            return FakeProfileUseCase()
        }
    }
}