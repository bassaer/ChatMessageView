package com.github.bassaer.example.matchers;

import android.view.View;

import org.hamcrest.Matcher;

/**
 * A matcher for Espresso that checks if an TextView has a specified color
 * Created by nakayama on 2017/08/02.
 */

public class ColorMatcher {
    public static Matcher<View> withTextColor(final int color) {
        return new TextColorMatcher(color);
    }

    public static Matcher<View> withBackgroundColor(int color) {
        return new BackgroundColorMatcher(color);
    }

    public static Matcher<View> withIconColor(int color) {
        return new IconColorMatcher(color);
    }
}
