package com.github.bassaer.chatmessageview.utils;

import com.github.bassaer.chatmessageview.models.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MessageDateComparator implements Comparator<Object> {
    @Override
    public int compare(Object a, Object b) {
        SimpleDateFormat f = new SimpleDateFormat("MMM. dd, yyyy", Locale.US);
        Date d1 = new Date();
        Date d2 = new Date();

        if (a instanceof String) {
            try {
                d1 = f.parse((String)a);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            final Message m1 = (Message) a;
            d1 = m1.getCreatedAt().getTime();
        }

        if (b instanceof String) {
            try {
                d2 = f.parse((String)b);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            final Message m2 = (Message) b;
            d2 = m2.getCreatedAt().getTime();
        }

        if (d1.equals(d2)) {
            if (a instanceof String) {
                return -1;
            }
            if (b instanceof String) {
                return 1;
            }
        } else {
            return d1.compareTo(d2);
        }
        return 0;
    }
}
