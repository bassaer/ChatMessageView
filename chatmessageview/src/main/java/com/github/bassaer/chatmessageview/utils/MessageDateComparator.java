package com.github.bassaer.chatmessageview.utils;

import com.github.bassaer.chatmessageview.models.Message;

import java.util.Comparator;

public class MessageDateComparator implements Comparator<Message> {
    @Override
    public int compare(Message first, Message second) {
        if (first.getCreatedAt().before(second.getCreatedAt())) {
            return -1;
        }
        if (first.getCreatedAt().after(second.getCreatedAt())) {
            return 1;
        }
        return 0;
    }
}
