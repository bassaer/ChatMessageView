package com.github.bassaer.example.matchers;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Custom matcher to test icon color of ImageView
 * Created by nakayama on 2017/08/03.
 */

public class IconColorMatcher extends TypeSafeMatcher<View> {
    private int mExpectedColor;

    public IconColorMatcher(int expectedColor) {
        mExpectedColor = expectedColor;
    }
    @Override
    protected boolean matchesSafely(View view) {
        if (view instanceof ImageView) {
            Drawable wrappedDrawable = ((ImageView) view).getDrawable();
            Drawable drawable = DrawableCompat.unwrap(wrappedDrawable);

        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with color value: ");
        description.appendValue(String.valueOf(mExpectedColor));
    }
}
