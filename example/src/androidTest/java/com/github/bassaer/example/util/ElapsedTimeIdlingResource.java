package com.github.bassaer.example.util;

import android.support.test.espresso.IdlingResource;

/**
 * Implement to wait for a specific amount of time
 * Created by nakayama on 2017/08/01.
 */

public class ElapsedTimeIdlingResource implements IdlingResource {
    private long mStartTime;
    private long mWaitingTime;
    private ResourceCallback mResourceCallback;

    public ElapsedTimeIdlingResource(long waitingTime) {
        mStartTime = System.currentTimeMillis();
        mWaitingTime = waitingTime;
    }
    @Override
    public String getName() {
        return ElapsedTimeIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        long elapsed = System.currentTimeMillis() - mStartTime;
        boolean idle = elapsed >= mWaitingTime;
        if (idle) {
            mResourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mResourceCallback = callback;
    }
}
