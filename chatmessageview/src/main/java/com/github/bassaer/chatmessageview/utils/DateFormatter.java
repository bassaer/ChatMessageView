package com.github.bassaer.chatmessageview.utils;

import java.util.Calendar;

/**
 * Date formatter of chat timeline separator.
 * Created by nakayama on 2017/01/13.
 */
public class DateFormatter implements ITimeFormatter {
    @Override
    public String getFormattedTimeText(Calendar createdAt) {
        return TimeUtils.calendarToString(createdAt, "MMM. dd, yyyy");
    }
}
