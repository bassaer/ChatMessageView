package com.github.bassaer.example

import android.content.Intent
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
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

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        activityRule.launchActivity(Intent())
    }

    @Test
    fun checkMenuList() {
        val menu = activityRule.activity.gettMenu()
        for (i in menu.indices) {
            onRow(i).check(matches(withText(menu[i])))
        }
    }

    private fun onRow(position: Int): DataInteraction {
        return onData(anything())
                .inAdapterView(withId(R.id.menu_list)).atPosition(position)
    }
}