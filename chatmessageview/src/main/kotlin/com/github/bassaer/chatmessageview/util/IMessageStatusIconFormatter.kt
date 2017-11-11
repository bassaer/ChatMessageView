package com.github.bassaer.chatmessageview.util

import android.graphics.drawable.Drawable

/**
 * Interface for status icon format
 * Created by nakayama on 2017/02/16.
 */
interface IMessageStatusIconFormatter {
    /**
     * Return icon depend on message status and sender
     * @param status message status
     * @param isRightMessage Whether sender is right or not
     * @return status icon image
     */
    fun getStatusIcon(status: Int, isRightMessage: Boolean): Drawable
}
