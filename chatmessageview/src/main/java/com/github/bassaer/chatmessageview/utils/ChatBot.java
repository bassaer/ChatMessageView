package com.github.bassaer.chatmessageview.utils;

import java.util.Calendar;

/**
 * Chat Bot for demo
 * Created by nakayama on 2016/12/03.
 */
public class ChatBot {

    public static String talk(String username, String message) {
        String receive = message.toLowerCase();
        if (receive.contains("hello")) {
            String user = "";
            if (username != null) {
                user = " " + username;
            }
            return "Hello" + user + "!";
        } else if (receive.contains("hey")) {
            return "Hey " + username + "!";
        } else if (receive.startsWith("do ")) {
            return "Yes, I do.";
        } else if (receive.contains("time")) {
            return "It's " + TimeUtils.calendarToString(Calendar.getInstance(), null) + ".";
        } else if (receive.contains("today")) {
            return "It's " + TimeUtils.calendarToString(Calendar.getInstance(), "M/d(E)");

        } else {
            String reply = "Lorem ipsum dolor sit amet";
            if (receive.length() > 7) {
                reply += ", consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ";
            }
            return reply;
        }
    }
}
