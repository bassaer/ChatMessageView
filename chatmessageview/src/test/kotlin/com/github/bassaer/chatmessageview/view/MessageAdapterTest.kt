package com.github.bassaer.chatmessageview.view

import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.github.bassaer.chatmessageview.R
import com.github.bassaer.chatmessageview.model.Attribute
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

/**
 * MessageAdapter Unit Test
 * Created by nakayama on 2018/01/03.
 */
@RunWith(RobolectricTestRunner::class)
internal class MessageAdapterTest {
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Any>
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        messageList = ArrayList()
        val senderIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_account_circle)
        val receiverIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_action_user)
        val sender = ChatUser(0, "User1", senderIcon)
        val receiver = ChatUser(1, "User2", receiverIcon)
        val message1 = Message.Builder()
                .setRight(true)
                .setText("message1")
                .setUser(sender)
                .build()
        val message2 = Message.Builder()
                .setRight(false)
                .setText("message2")
                .setUser(receiver)
                .build()

        messageList.add(message1.dateSeparateText)
        messageList.add(message1)
        messageList.add(message2)

        val attribute = Attribute(context, null)
        messageAdapter = MessageAdapter(context, 0, messageList, attribute)
    }

    @Test
    fun getView() {
        val messageArray: Array<View?> = arrayOfNulls(messageList.size)

        for (i in 0 until messageList.size) {
            messageArray[i] = messageAdapter.getView(i, null, null)
        }

        // Date label
        val dateLabel = messageArray[0]?.findViewById<TextView>(R.id.dateLabelText)
        var expectingText: String? = messageList[0] as String
        assertEquals(expectingText, dateLabel?.text)

        // Check Messages
        for (i in 1 until messageArray.size) {
            val messageText = messageArray[i]?.findViewById<TextView>(R.id.message_text)
            expectingText = (messageList[i] as Message).text
            assertEquals(expectingText, messageText?.text)
        }
    }

}