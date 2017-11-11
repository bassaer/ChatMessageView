package com.github.bassaer.chatmessageview.utils;

import com.github.bassaer.chatmessageview.util.ITimeFormatter;

import java.util.Calendar;

/**
 * Time formatter of the chat bubble
 * Created by nakayama on 2017/01/13.
 */
public class SendTimeFormatter implements ITimeFormatter {
    @Override
    public String getFormattedTimeText(Calendar createdAt) {
        return TimeUtils.calendarToString(createdAt, "HH:mm");
    }
}
