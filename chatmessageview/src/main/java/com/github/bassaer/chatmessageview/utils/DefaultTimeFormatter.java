package com.github.bassaer.chatmessageview.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Default Time format that show hour and minute
 * Created by nakayama on 2017/02/18.
 */
public class DefaultTimeFormatter implements ITimeFormatter {
    @Override
    public String getFormattedTimeText(Calendar createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(createdAt.getTime());
    }
}
