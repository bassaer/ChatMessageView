package com.github.bassaer.chatmessageview.model

/**
 * Created by marcos-ambrosi on 2/13/18.
 */
class ChatActivityMessage {


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

        fun build(): ChatActivityMessage {
            return chatActivityMessage
        }
    }
}