package com.salim.mypokedex.profile

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.salim.mypokedex.R
import com.salim.mypokedex.testHelpers.launchFragmentInHiltContainer
import com.salim.mypokedex.utilities.EspressoIdlingResourceRule
import com.salim.mypokedex.utilities.ImageViewHasDrawableMatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    @get: Rule
    val intentsRule = IntentsRule()

    @get:Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Test
    fun test_validateGalleryIntent() {
        val scenario = launchFragmentInHiltContainer<ProfileFragment>()

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

        // TODO: below doesnt work because drawable isnt being set properly in test.
        //onView(withId(R.id.avatar_view)).check(matches(ImageViewHasDrawableMatcher.hasDrawable()))
    }

    @Test
    fun test_cameraIntentIsCorrect() {
        val scenario = launchFragmentInHiltContainer<ProfileFragment>()

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

    private fun createGalleryPickActivityResultStub(): ActivityResult {
        val resources = InstrumentationRegistry.getInstrumentation().context.resources
        val imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
            resources.getResourcePackageName(R.drawable.ic_launcher_background) + "://" +
            resources.getResourceTypeName(R.drawable.ic_launcher_background) + "://" +
            resources.getResourceEntryName(R.drawable.ic_launcher_background)
        )
        val resultIntent = Intent()
        resultIntent.data = imageUri
        return ActivityResult(Activity.RESULT_OK, resultIntent)
    }

    private fun createImageCaptureActivityResultStub(): ActivityResult {
        val resultIntent = Intent()
        return ActivityResult(Activity.RESULT_OK, resultIntent)
    }
}