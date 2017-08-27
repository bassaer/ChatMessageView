package com.github.bassaer.example.matchers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Custom matcher to test drawable resource
 * Created by nakayama on 2017/07/29.
 */
public class DrawableMatcher extends TypeSafeMatcher<View> {
    private final int mExpectedId;
    private String mResourceName;

    public DrawableMatcher(int expectedId) {
        super(View.class);
        mExpectedId = expectedId;
    }

    @Override
    protected boolean matchesSafely(View target) {
        if (!(target instanceof ImageView)) {
            return false;
        }
        ImageView imageView = (ImageView) target;
        if (mExpectedId < 0) {
            return imageView.getDrawable() == null;
        }
        Resources resources = target.getContext().getResources();
        Drawable expectedDrawable = ResourcesCompat.getDrawable(resources, mExpectedId, null);
        mResourceName = resources.getResourceEntryName(mExpectedId);
        if (expectedDrawable == null) {
            return false;
        }
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap otherBitmap = ((BitmapDrawable)expectedDrawable).getBitmap();

        return bitmap.sameAs(otherBitmap);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with drawable from resource id: ");
        description.appendValue(mExpectedId);
        if (mResourceName != null) {
            description.appendText("[");
            description.appendText(mResourceName);
            description.appendText("]");
        }
    }
}
