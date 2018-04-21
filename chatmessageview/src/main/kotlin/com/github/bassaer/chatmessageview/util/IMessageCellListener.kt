package com.github.bassaer.chatmessageview.util

import com.github.bassaer.chatmessageview.model.Message

interface IMessageCellListener {
    fun onCellLoaded(index:Int, message: Message)
}
