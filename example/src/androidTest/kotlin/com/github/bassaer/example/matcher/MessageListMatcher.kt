package com.github.bassaer.example.matcher

import android.view.View
import android.widget.ListView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for checking Message List size
 * Created by nakayama on 2017/12/24.
 */
object MessageListMatcher {
    @JvmStatic
    fun withListSize(size: Int): TypeSafeMatcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resultSize = -1
            override fun describeTo(description: Description?) {
                description?.appendText("Message list size should be $size")
                if (resultSize > 0) {
                    description?.appendText(", but it was $resultSize")
                }
            }

            override fun matchesSafely(view: View?): Boolean {
                resultSize = (view as ListView).count
                return  resultSize == size
            }
        }
    }

}