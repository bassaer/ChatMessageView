package com.github.bassaer.chatmessageview.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.github.bassaer.chatmessageview.util.*
import java.util.*

/**
 * Message object
 * Created by nakayama on 2016/08/08.
 */
class Message {

    /**
     * Sender information
     */
    lateinit var user: IChatUser

    /**
     * Whether sender username is shown or not
     */
    var usernameVisibility = true
    /**
     * If true, there is the icon space but invisible.
     */
    var iconVisibility = true
    /**
     * If true, there is no icon space.
     */
    var isIconHided = false
        private set

    /**
     * Whether the message is shown right side or not.
     */
    var isRightMessage: Boolean = false

    /**
     * Message content text
     */
    var messageText: String? = null

    /**
     * The time message that was created
     */
    var createdAt = Calendar.getInstance()

    /**
     * Whether cell of list view is date separator text or not.
     */
    var isDateCell: Boolean = false

    /**
     * TEXT format of the send time that is next to the message
     */
    private var mSendTimeFormatter: ITimeFormatter? = null

    /**
     * Date separator text format.
     * This text is shown if the before or after message was sent different day
     */
    private var mDateFormatter: ITimeFormatter? = null

    /**
     * Message status
     * You can use to know the message status such as fail, delivered, seen.. etc.
     */
    var status: Int = 0

    /**
     * Message status type such as icon, text, or none.
     */
    var messageStatusType = MESSAGE_STATUS_NONE

    /**
     * Message status icon formatter
     */
    var statusIconFormatter: IMessageStatusIconFormatter? = null

    /**
     * Message status text formatter
     */
    var statusTextFormatter: IMessageStatusTextFormatter? = null

    /**
     * PICTURE message
     */
    var picture: Bitmap? = null

    /**
     * Message type
     */
    var type: Type? = null

    val timeText: String
        get() = mSendTimeFormatter!!.getFormattedTimeText(createdAt!!)

    val dateSeparateText: String
        get() = mDateFormatter!!.getFormattedTimeText(createdAt!!)

    val statusIcon: Drawable
        get() = statusIconFormatter!!.getStatusIcon(status, isRightMessage)

    val statusText: String
        get() = statusTextFormatter!!.getStatusText(status, isRightMessage)

    /**
     * Message Types
     *
     */
    enum class Type {
        TEXT,
        PICTURE,
        MAP,
        LINK
    }

    /**
     * Constructor
     */
    init {
        createdAt = Calendar.getInstance()
        mSendTimeFormatter = DefaultTimeFormatter()
        mDateFormatter = DateFormatter()
        mSendTimeFormatter = DefaultTimeFormatter()
        type = Type.TEXT
    }

    /**
     * Message builder
     */
    class Builder {
        private val message: Message = Message()

        fun setUser(user: IChatUser): Builder {
            message.user = user
            return this
        }

        fun setUsernameVisibility(visibility: Boolean): Builder {
            message.usernameVisibility = visibility
            return this
        }

        fun setUserIconVisibility(visibility: Boolean): Builder {
            message.iconVisibility = visibility
            return this
        }

        fun hideIcon(hide: Boolean): Builder {
            message.hideIcon(hide)
            return this
        }


        fun setRightMessage(isRight: Boolean): Builder {
            message.isRightMessage = isRight
            return this
        }

        fun setMessageText(messageText: String): Builder {
            message.messageText = messageText
            return this
        }

        fun setCreatedAt(calendar: Calendar): Builder {
            message.createdAt = calendar
            return this
        }

        fun setDateCell(isDateCell: Boolean): Builder {
            message.isDateCell = isDateCell
            return this
        }

        fun setSendTimeFormatter(sendTimeFormatter: ITimeFormatter): Builder {
            message.setSendTimeFormatter(sendTimeFormatter)
            return this
        }

        fun setDateFormatter(dateFormatter: ITimeFormatter): Builder {
            message.setDateFormatter(dateFormatter)
            return this
        }

        fun setStatus(status: Int): Builder {
            message.status = status
            return this
        }

        fun setMessageStatusType(messageStatusType: Int): Builder {
            message.messageStatusType = messageStatusType
            return this
        }

        fun setStatusIconFormatter(formatter: IMessageStatusIconFormatter): Builder {
            message.statusIconFormatter = formatter
            return this
        }

        fun setStatusTextFormatter(formatter: IMessageStatusTextFormatter): Builder {
            message.statusTextFormatter = formatter
            return this
        }

        fun setType(type: Type): Builder {
            message.type = type
            return this
        }

        fun setPicture(picture: Bitmap): Builder {
            message.picture = picture
            return this
        }

        fun build(): Message {
            return message
        }

    }

    fun hideIcon(hideIcon: Boolean) {
        isIconHided = hideIcon
    }

    /**
     * Set custom send time text formatter
     * @param sendTimeFormatter custom send time formatter
     */
    fun setSendTimeFormatter(sendTimeFormatter: ITimeFormatter) {
        mSendTimeFormatter = sendTimeFormatter
    }

    /**
     * Set custom date text formatter
     * @param dateFormatter custom date formatter
     */
    fun setDateFormatter(dateFormatter: ITimeFormatter) {
        mDateFormatter = dateFormatter
    }

    interface OnBubbleClickListener {
        fun onClick(message: Message)
    }

    interface OnBubbleLongClickListener {
        fun onLongClick(message: Message)
    }

    interface OnIconClickListener {
        fun onIconClick(message: Message)
    }

    interface OnIconLongClickListener {
        fun onIconLongClick(message: Message)
    }

    companion object {

        /**
         * Message status is not shown.
         */
        val MESSAGE_STATUS_NONE = 0

        /**
         * Show message status icon.
         */
        val MESSAGE_STATUS_ICON = 1

        /**
         * Show message status text.
         * ex. seen, fail, delivered
         */
        val MESSAGE_STATUS_TEXT = 2

        /**
         * Show message status icon only right message
         */
        val MESSAGE_STATUS_ICON_RIGHT_ONLY = 3

        /**
         * Show message status icon only left message
         */
        val MESSAGE_STATUS_ICON_LEFT_ONLY = 4

        /**
         * Show message status text only right message
         */
        val MESSAGE_STATUS_TEXT_RIGHT_ONLY = 5

        /**
         * Show message status text only left message
         */
        val MESSAGE_STATUS_TEXT_LEFT_ONLY = 6
    }
}
