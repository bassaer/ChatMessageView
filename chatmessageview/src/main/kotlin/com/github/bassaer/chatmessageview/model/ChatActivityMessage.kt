package com.github.bassaer.chatmessageview.model

import java.util.*

/**
 * Created by marcos-ambrosi on 2/13/18.
 */
class ChatActivityMessage : SortableMessage() {

    /**
     * Message to be shown as part of the chat's activity.
     */
    var message = ""

    /**
     * ChatActivityMessage builder
     */
    class Builder {

        private val chatActivityMessage: ChatActivityMessage = ChatActivityMessage()

        fun setMessage(message: String): Builder {
            chatActivityMessage.message = message
            return this
        }

        fun setCreatedAt(calendar: Calendar): Builder {
            chatActivityMessage.createdAt = calendar
            return this
        }

        fun build(): ChatActivityMessage {
            return chatActivityMessage
        }
    }
}