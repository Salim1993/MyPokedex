package com.salim.mypokedex.utilities

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

object ImageViewHasDrawableMatcher {

    fun hasDrawable(): BoundedMatcher<View, AppCompatImageView> {

        return object : BoundedMatcher<View, AppCompatImageView>(AppCompatImageView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("has drawable")
            }

            override fun matchesSafely(item: AppCompatImageView?): Boolean {
                return item?.drawable != null
            }
        }
    }
}