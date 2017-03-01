package com.github.bassaer.chatmessageview.utils;

import java.util.Calendar;

/**
 * Interface that allows custom formatting of time string
 * Created by nakayama on 2017/01/13.
 */
public interface ITimeFormatter {

    /**
     * Format the time text which is next to the chat bubble.
     * @param createdAt The time that message was created
     * @return Formatted time text
     */
    String getFormattedTimeText(Calendar createdAt);
}
