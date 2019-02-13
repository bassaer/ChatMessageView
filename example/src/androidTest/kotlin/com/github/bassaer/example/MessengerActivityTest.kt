package com.github.bassaer.example

import android.content.Context
import android.content.Intent

import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule

import com.github.bassaer.chatmessageview.util.TimeUtils
import com.github.bassaer.example.matcher.ImageViewDrawableMatcher.withDrawable
import com.github.bassaer.example.matcher.MessageListMatcher
import com.github.bassaer.example.util.ElapsedTimeIdlingResource

import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.containsString

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.util.*

/**
 * MainActivity Test
 * Created by nakayama on 2017/07/29.
 */
@RunWith(AndroidJUnit4::class)
class MessengerActivityTest {
    private var mContext: Context? = null
    private var mUsers: List<User>? = null

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MessengerActivity::class.java, true, false)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mContext = InstrumentationRegistry.getInstrumentation().targetContext
        AppData.reset(mContext)
        activityTestRule.launchActivity(Intent())
        mUsers = activityTestRule.activity.users
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        AppData.reset(mContext)
    }

    @Test
    @Throws(Exception::class)
    fun testSendingMessage() {
        val message = "testing"
        inputText(message)
        val sendingUser = mUsers!![0]
        val now = Calendar.getInstance()
        val expectingDate = TimeUtils.calendarToString(now, "MMM. dd, yyyy")
        onRow(0).onChildView(withId(R.id.dateLabelText)).check(matches(withText(expectingDate)))
        onRow(1).onChildView(withId(R.id.message_user_name)).check(matches(withText(sendingUser.getName())))
        onRow(1).onChildView(withId(R.id.message_text)).check(matches(withText(message)))
    }

    @Test
    @Throws(Exception::class)
    fun testReply() {
        val message = "Hello"
        inputText(message)
        val sendingUser = mUsers!![0]
        val replyingUser = mUsers!![1]
        val waitingTime: Long = 3000
        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        IdlingRegistry.getInstance().register(idlingResource)
        onRow(1).onChildView(withId(R.id.message_user_name)).check(matches(withText(sendingUser.getName())))
        onRow(1).onChildView(withId(R.id.message_text)).check(matches(withText(message)))
        onRow(2).onChildView(withId(R.id.user_icon)).check(matches(withDrawable(R.drawable.face_1)))
        onRow(2).onChildView(withId(R.id.message_user_name)).check(matches(withText(replyingUser.getName())))
        onRow(2).onChildView(withId(R.id.message_text)).check(matches(withText(containsString(sendingUser.getName()))))
        onRow(2).onChildView(withId(R.id.message_text)).check(matches(withText(containsString(message))))
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun checkSendingMessageInSequence() {
        activityTestRule.activity.setReplyDelay(0)
        val numOfMessages = 5
        for (i in 0 until numOfMessages) {
            inputText(i.toString())
        }

        var messageCounter = 0
        var i = 1
        while (i < numOfMessages) {
            onRow(i).onChildView(withId(R.id.message_text)).check(matches(withText(messageCounter.toString())))
            messageCounter++
            i += 2
        }
    }

    @Test
    fun checkDeleteMessage() {
        val messages = arrayOf("message1", "message2", "message3")
        inputText(messages[0])
        inputText(messages[1])
        inputText(messages[2])

        val waitingTime: Long = 3000
        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        IdlingRegistry.getInstance().register(idlingResource)
        // Remove reply for message1
        onRow(2).onChildView(withId(R.id.user_icon)).perform(longClick())
        // Remove reply for message2
        onRow(3).onChildView(withId(R.id.user_icon)).perform(longClick())
        // message3 should be shown at 3rd message
        onRow(3).onChildView(withId(R.id.message_text)).check(matches(withText(messages[2])))
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun checkDeleteAllMessages() {
        val messages = arrayOf("message1", "message2", "message3")
        inputText(messages[0])
        inputText(messages[1])
        inputText(messages[2])

        val waitingTime: Long = 3000
        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        IdlingRegistry.getInstance().register(idlingResource)
        onView(withId(R.id.optionButton)).perform(click())
        // Remove all messages
        onView(withText(R.string.clear_messages)).perform(click())
        onView(withId(R.id.messageView)).check(matches(MessageListMatcher.withListSize(0)))
        IdlingRegistry.getInstance().unregister(idlingResource)
    }


    /**
     * Type text and then tap send button
     * @param text Typing Text
     */
    private fun inputText(text: String) {
        //Reset input
        onView(withId(R.id.inputBox)).perform(replaceText(""))
        //Type text
        onView(withId(R.id.inputBox)).perform(typeText(text))
        //Close keyboard
        onView(withId(R.id.inputBox)).perform(closeSoftKeyboard())
        //Tap button
        onView(withId(R.id.sendButton)).perform(click())
    }

    private fun onRow(position: Int): DataInteraction {
        return onData(anything())
                .inAdapterView(withId(R.id.messageView)).atPosition(position)
    }

}