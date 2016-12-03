package jp.bassaer.chatmessageview.utils;

import java.util.Calendar;

/**
 * Chat Bot for demo
 * Created by nakayama on 2016/12/03.
 */
public class ChatBot {

    public static String talk(String username, String message) {
        String receive = message.toLowerCase();
        if (receive.indexOf("hello") != -1) {
            return "Hello " + username+"!";
        } else if (receive.indexOf("hey") != -1) {
            return "Hey " + username + "!";
        } else if (receive.startsWith("do ")) {
            return "Yes, I do.";
        } else if (receive.indexOf("time") != -1) {
            return "It's " + TimeUtils.calendarToString(Calendar.getInstance(), null) + ".";
        } else if (receive.indexOf("today") != -1) {
            return "It's " + TimeUtils.calendarToString(Calendar.getInstance(), "M/d(E)");

        } else {
            return "Lorem ipsum dolor sit amet, " +
                    "consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ";
        }
    }
}
