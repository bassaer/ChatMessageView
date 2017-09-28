package com.github.bassaer.chatmessageview.utils;

import com.github.bassaer.chatmessageview.models.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MessageDateComparator implements Comparator<Object> {
    @Override
    public int compare(Object object1, Object object2) {
        SimpleDateFormat format = new SimpleDateFormat("MMM. dd, yyyy", Locale.getDefault());
        Date date1 = new Date();
        Date date2 = new Date();

        if (object1 instanceof String) {
            try {
                date1 = format.parse((String)object1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            final Message message1 = (Message) object1;
            date1 = message1.getCreatedAt().getTime();
        }

        if (object2 instanceof String) {
            try {
                date2 = format.parse((String)object2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            final Message message2 = (Message) object2;
            date2 = message2.getCreatedAt().getTime();
        }

        if (date1.equals(date2)) {
            if (object1 instanceof String) {
                return -1;
            }
            if (object2 instanceof String) {
                return 1;
            }
        } else {
            return date1.compareTo(date2);
        }
        return 0;
    }
}
