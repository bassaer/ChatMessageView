package com.github.bassaer.example;

import com.github.bassaer.chatmessageview.util.ITimeFormatter;

import java.util.Calendar;

/**
 * Custom time formatter
 * Created by nakayama on 2017/01/13.
 */
public class MyTimeFormatter implements ITimeFormatter{
    @Override
    public String getFormattedTimeText(Calendar createdAt) {
        Calendar now = Calendar.getInstance();
        // Time difference [second]
        long timeDiff = (now.getTimeInMillis() - createdAt.getTimeInMillis()) / 1000;

        if (timeDiff < 3) {
            return "just now";
        } else if (timeDiff < 60) {
            return timeDiff + " second" + getPlural(timeDiff) + " ago";
        }

        long min = timeDiff / 60;
        if (min < 60) {
            return min + " minute" + getPlural(min) + " ago";
        }

        long hour = min / 60;
        if (hour < 24) {
            return hour + " hour" + getPlural(hour) + " ago";
        }

        long day = hour / 24;
        return day + " day" + getPlural(day) + " ago";
    }

    private String getPlural(long time) {
        return time > 1 ? "s" : "";
    }
}
