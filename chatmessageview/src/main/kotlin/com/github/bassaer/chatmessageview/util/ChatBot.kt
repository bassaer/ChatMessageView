package com.github.bassaer.chatmessageview.util

import java.util.*

/**
 * Chat Bot for demo
 * Created by nakayama on 2016/12/03.
 */
object ChatBot {

    fun talk(username: String?, message: String): String {
        val receive = message.toLowerCase()
        when {
            receive.contains("hello") -> {
                var user = ""
                if (username != null) {
                    user = " " + username
                }
                return "Hello$user!"
            }
            receive.contains("hey") -> return "Hey $username!"
            receive.startsWith("do ") -> return "Yes, I do."
            receive.contains("time") -> return "It's " + TimeUtils.calendarToString(Calendar.getInstance(), null) + "."
            receive.contains("today") -> return "It's " + TimeUtils.calendarToString(Calendar.getInstance(), "M/d(E)")
            else -> {
                var reply = "Lorem ipsum dolor sit amet"
                if (receive.length > 7) {
                    reply += ", consectetur adipiscing elit, " + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
                }
                return reply
            }
        }
    }
}
