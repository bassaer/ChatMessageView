package com.github.bassaer.example.matcher

import android.view.View
import android.widget.TextView

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Custom matcher to test TextView color
 * Created by nakayama on 2017/08/02.
 */

class TextColorMatcher(color: Int) : TypeSafeMatcher<View>() {
    private var mExpectedColor = -1

    init {
        mExpectedColor = color
    }

    override fun matchesSafely(view: View): Boolean {
        val textView = view as TextView
        return mExpectedColor == textView.currentTextColor
    }

    override fun describeTo(description: Description) {
        description.appendText("with color value: ")
        description.appendValue(mExpectedColor.toString())
    }
}
