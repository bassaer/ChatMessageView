package com.github.bassaer.example.matchers;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Custom matcher to test background color of view
 * Created by nakayama on 2017/08/02.
 */

public class BackgroundColorMatcher extends TypeSafeMatcher<View> {
    private int mExpectedColor;

    public BackgroundColorMatcher(int expectedColor) {
        mExpectedColor = expectedColor;
    }

    @Override
    protected boolean matchesSafely(View view) {
        Drawable drawable = view.getBackground().getCurrent();
        TextView textView = (TextView)view;
        if (drawable instanceof ColorDrawable) {
            return mExpectedColor == ((ColorDrawable)drawable).getColor();
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with color value: ");
        description.appendValue(String.valueOf(mExpectedColor));
    }
}
