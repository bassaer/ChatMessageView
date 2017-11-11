package com.github.bassaer.chatmessageview.util

/**
 * Interface for status text format
 * Created by nakayama on 2017/02/16.
 */
interface IMessageStatusTextFormatter {
    /**
     * Return status text depend on message status and sender
     * @param status message status
     * @param isRightMessage Whether sender is right or not
     * @return message status text
     */
    fun getStatusText(status: Int, isRightMessage: Boolean): String
}
