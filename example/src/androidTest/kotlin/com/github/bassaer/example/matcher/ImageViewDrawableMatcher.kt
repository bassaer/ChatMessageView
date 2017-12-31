package com.github.bassaer.example.matcher

import android.view.View
import org.hamcrest.Matcher

/**
 * A matcher for Espresso that checks if an ImageView has a specified drawable
 * Created by nakayama on 2017/07/02.
 */

object ImageViewDrawableMatcher {
    @JvmStatic
    fun withDrawable(resourceId: Int): Matcher<View> {
        return DrawableMatcher(resourceId)
    }
}
