package com.github.bassaer.example.matchers;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Custom matcher to test TextView color
 * Created by nakayama on 2017/08/02.
 */

public class TextColorMatcher extends TypeSafeMatcher<View> {
    private int mExpectedColor = -1;

    public TextColorMatcher(int color) {
        mExpectedColor = color;
    }

    @Override
    protected boolean matchesSafely(View view) {
        TextView textView = (TextView)view;
        return mExpectedColor == textView.getCurrentTextColor();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with color value: ");
        description.appendValue(String.valueOf(mExpectedColor));
    }


}
