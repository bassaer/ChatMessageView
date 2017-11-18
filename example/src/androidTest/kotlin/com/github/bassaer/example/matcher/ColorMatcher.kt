package com.github.bassaer.example.matcher

import android.view.View
import com.github.bassaer.example.matchers.TextColorMatcher
import org.hamcrest.Matcher

/**
 * A matcher for Espresso that checks if an TextView has a specified color
 * Created by nakayama on 2017/08/02.
 */

object ColorMatcher {
    @JvmStatic
    fun withTextColor(color: Int): Matcher<View> {
        return TextColorMatcher(color)
    }
}
