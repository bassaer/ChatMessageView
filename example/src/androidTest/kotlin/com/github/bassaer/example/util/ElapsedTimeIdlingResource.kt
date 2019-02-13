package com.github.bassaer.example.util

import androidx.test.espresso.IdlingResource

/**
 * Implement to wait for a specific amount of time
 * Created by nakayama on 2017/08/01.
 */
class ElapsedTimeIdlingResource(private val mWaitingTime: Long) : IdlingResource {
    private val mStartTime: Long = System.currentTimeMillis()
    private var mResourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return ElapsedTimeIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        val elapsed = System.currentTimeMillis() - mStartTime
        val idle = elapsed >= mWaitingTime
        if (idle) {
            mResourceCallback!!.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        mResourceCallback = callback
    }
}
