package com.github.bassaer.example;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;

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
import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * MainActivity Test
 * Created by nakayama on 2017/07/29.
 */
@RunWith(AndroidJUnit4.class)
public class MessengerActivityTest {
    private MessageList mMessageList;

    Context mContext;
    private List<User> mUsers;

    @Rule
    public ActivityTestRule<MessengerActivity> mActivityRule = new ActivityTestRule<>(MessengerActivity.class);

    @Before
    public void setUp() throws Exception {
        Message message = mock(Message.class);
        Calendar mockTime = Calendar.getInstance();
        //2017-08-01 10:00:00
        mockTime.setTimeInMillis(1501549200);
        when(message.getCreatedAt()).thenReturn(mockTime);
        mContext = InstrumentationRegistry.getContext();
        AppData.reset(mContext);
        mUsers = mActivityRule.getActivity().getUsers();
    }

    @After
    public void tearDown() throws Exception {
        mContext = InstrumentationRegistry.getContext();
        AppData.reset(mContext);
    }

    @Test
    public void testSendingMessage() throws Exception {
        AppData.reset(mContext);
        String message = "hello";
        inputText(message);
        User sendingUser = mUsers.get(0);
        //onRow(0).onChildView(withId(R.id.user_icon)).check(matches(withDrawable(R.drawable.face_2)));
        onRow(1).onChildView(withId(R.id.message_user_name)).check(matches(withText(sendingUser.getName())));
        onRow(1).onChildView(withId(R.id.message_text)).check(matches(withText(message)));
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