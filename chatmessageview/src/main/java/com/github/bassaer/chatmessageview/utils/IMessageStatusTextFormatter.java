package com.github.bassaer.chatmessageview.utils;

/**
 * Interface for status text format
 * Created by nakayama on 2017/02/16.
 */
public interface IMessageStatusTextFormatter {
    /**
     * Return status text depend on message status and sender
     * @param status message status
     * @param isRightMessage Whether sender is right or not
     * @return message status text
     */
    String getStatusText(int status, boolean isRightMessage);
}
