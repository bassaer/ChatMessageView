package com.github.bassaer.example.matchers;

import android.view.View;

import org.hamcrest.Matcher;

/**
 * A matcher for Espresso that checks if an ImageView has a specified drawable
 * Created by nakayama on 2017/07/02.
 */

public class ImageViewDrawableMatcher {
    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }
}
