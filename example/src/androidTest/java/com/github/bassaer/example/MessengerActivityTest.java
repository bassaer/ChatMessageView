package com.github.bassaer.example;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.utils.TimeUtils;
import com.github.bassaer.example.util.ElapsedTimeIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.bassaer.example.matchers.ColorMatcher.withTextColor;
import static com.github.bassaer.example.matchers.ImageViewDrawableMatcher.withDrawable;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

/**
 * MainActivity Test
 * Created by nakayama on 2017/07/29.
 */
@RunWith(AndroidJUnit4.class)
public class MessengerActivityTest {
    private MessageList mMessageList;

    private Context mContext;
    private List<User> mUsers;
    private Intent mIntent;

    @Rule
    public ActivityTestRule<MessengerActivity> mActivityRule
            = new ActivityTestRule<>(MessengerActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        mIntent = new Intent();
        mContext = InstrumentationRegistry.getTargetContext();
        AppData.reset(mContext);
        mActivityRule.launchActivity(mIntent);
        mUsers = mActivityRule.getActivity().getUsers();
    }

    @After
    public void tearDown() throws Exception {
        AppData.reset(mContext);
    }

    @Test
    public void testSendingMessage() throws Exception {
        String message = "testing";
        inputText(message);
        User sendingUser = mUsers.get(0);
        Calendar now = Calendar.getInstance();
        String expectingDate = TimeUtils.calendarToString(now, "MMM. dd, yyyy");
        onRow(0).onChildView(withId(R.id.date_separate_text)).check(matches(withText(expectingDate)));
        onRow(1).onChildView(withId(R.id.message_user_name)).check(matches(withText(sendingUser.getName())));
        onRow(1).onChildView(withId(R.id.message_text)).check(matches(withText(message)));
    }

    @Test
    public void testReply() throws Exception {
        String message = "Hello";
        inputText(message);
        User sendingUser = mUsers.get(0);
        User replyingUser = mUsers.get(1);
        long waitingTime = 3000;
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(waitingTime);
        Espresso.registerIdlingResources(idlingResource);
        onRow(1).onChildView(withId(R.id.message_user_name)).check(matches(withText(sendingUser.getName())));
        onRow(1).onChildView(withId(R.id.message_text)).check(matches(withText(message)));
        onRow(2).onChildView(withId(R.id.user_icon)).check(matches(withDrawable(R.drawable.face_1)));
        onRow(2).onChildView(withId(R.id.message_user_name)).check(matches(withText(replyingUser.getName())));
        onRow(2).onChildView(withId(R.id.message_text)).check(matches(withText(containsString(sendingUser.getName()))));
        onRow(2).onChildView(withId(R.id.message_text)).check(matches(withText(containsString(message))));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void checkViewColors() throws Exception {
        String message = "Hello";
        inputText(message);
        long waitingTime = 3000;
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(waitingTime);
        Espresso.registerIdlingResources(idlingResource);
        onRow(0).onChildView(withId(R.id.date_separate_text)).check(matches(withTextColor(MessengerActivity.DATA_SEPARATOR_COLOR)));
        for (int i = 1; i <=2; i++) {
            onRow(i).onChildView(withId(R.id.message_user_name))
                    .check(matches(withTextColor(MessengerActivity.USERNAME_TEXT_COLOR)));
            onRow(i).onChildView(withId(R.id.time_display_text))
                    .check(matches(withTextColor(MessengerActivity.SEND_TIME_TEXT_COLOR)));
        }
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void checkSendingMessageInSequence() {

        mActivityRule.getActivity().setReplyDelay(0);
        int numOfMessages = 5;
        for(int i = 0; i < numOfMessages; i++) {
            inputText(String.valueOf(i));
        }
        //Scroll to top of the chat

        int messageCounter = 0;
        for (int i = 1; i < numOfMessages; i+=2) {
            onRow(i).onChildView(withId(R.id.message_text)).check(matches(withText(String.valueOf(messageCounter))));
            messageCounter++;
        }
    }


    /**
     * Type text and then tap send button
     * @param text Typing Text
     */
    private void inputText(String text) {
        //Reset input
        onView(withId(R.id.message_edit_text)).perform(replaceText(""));
        //Type text
        onView(withId(R.id.message_edit_text)).perform(typeText(text));
        //Close keyboard
        onView(withId(R.id.message_edit_text)).perform(closeSoftKeyboard());
        //Tap button
        onView(withId(R.id.send_button)).perform(click());
    }

    private static DataInteraction onRow(int position) {
        return onData(anything())
                .inAdapterView(withId(R.id.message_view)).atPosition(position);
    }

}