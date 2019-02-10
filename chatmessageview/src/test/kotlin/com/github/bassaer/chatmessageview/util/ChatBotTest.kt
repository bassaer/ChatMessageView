package com.github.bassaer.chatmessageview.util

import org.hamcrest.CoreMatchers.startsWith

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by nakayama on 2017/11/11.
 */
internal class ChatBotTest {

    private val username = "world"

    @Test
    fun replyGreeting() {
        assertEquals(ChatBot.talk(null, "hello"), "Hello!")
        assertEquals(ChatBot.talk(username, "Hello"), "Hello $username!")
        assertEquals(ChatBot.talk(username, "hey"), "Hey $username!")
    }

    @Test
    fun replyQuestions() {
        assertEquals(ChatBot.talk(username, "Do you know that?"), "Yes, I do.")
        assertThat(ChatBot.talk(username, "time"), startsWith("It's"))
        assertThat(ChatBot.talk(username, "today"), startsWith("It's"))
    }

    @Test
    fun replyDummyText() {
        assertEquals(ChatBot.talk(username, "ok"), "Lorem ipsum dolor sit amet")
        assertEquals(ChatBot.talk(username, "this is long message"),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ")
    }

}