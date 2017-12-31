package com.github.bassaer.example

import android.content.Intent
import android.support.test.espresso.DataInteraction
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.anything
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * MainActivity Test
 * Created by nakayama on 2017/07/30.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    private var mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        mActivityRule.launchActivity(Intent())
    }

    @Test
    fun checkMenuList() {
        val menu = mActivityRule.activity.gettMenu()
        for (i in menu.indices) {
            onRow(i).check(matches(withText(menu[i])))
        }
    }

    private fun onRow(position: Int): DataInteraction {
        return onData(anything())
                .inAdapterView(withId(R.id.menu_list)).atPosition(position)
    }
}