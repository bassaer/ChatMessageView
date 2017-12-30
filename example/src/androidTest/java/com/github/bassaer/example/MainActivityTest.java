package com.github.bassaer.example;

import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * MainActivity Test
 * Created by nakayama on 2017/07/30.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkMenuList() {
        String[] menu = mActivityRule.getActivity().gettMenu();
        for(int i = 0; i < menu.length; i++) {
            onRow(i).check(matches(withText(menu[i])));
        }
    }

    private static DataInteraction onRow(int position) {
        return onData(anything())
                .inAdapterView(withId(R.id.menu_list)).atPosition(position);
    }
}