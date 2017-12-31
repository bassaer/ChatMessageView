package com.github.bassaer.chatmessageview.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.ListView
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.models.Attribute
import com.github.bassaer.chatmessageview.util.MessageDateComparator
import com.github.bassaer.chatmessageview.util.TimeUtils
import com.github.bassaer.chatmessageview.views.adapters.MessageAdapter
import java.util.*

/**
 * Simple chat view
 * Created by nakayama on 2016/08/08.
 */
class MessageView : ListView, View.OnFocusChangeListener {

    /**
     * All contents such as right message, left message, date label
     */
    private var mChatList: MutableList<Any> = ArrayList()
    /**
     * Only messages
     */
    private val mMessageList = ArrayList<Message>()

    private var mMessageAdapter: MessageAdapter? = null

    private var mOnKeyboardAppearListener: OnKeyboardAppearListener? = null

    /**
     * MessageView is refreshed at this time
     */
    private var mRefreshInterval: Long = 60000
    /**
     * Refresh scheduler
     */
    private var mRefreshTimer: Timer? = null

    private var mHandler: Handler? = null

    private var mAttribute: Attribute? = null

    /**
     * Return last object (right message or left message or date text)
     * @return last object of chat
     */
    val lastChatObject: Any?
        get() = if (mChatList.size == 0) {
            null
        } else mChatList[mChatList.size - 1]

    val messageList: List<Message>
        get() = mMessageList

    interface OnKeyboardAppearListener {
        fun onKeyboardAppeared(hasChanged: Boolean)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mAttribute = Attribute(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mAttribute = Attribute(context, attrs)
        init()
    }


    fun init(list: List<Message>) {
        mChatList = ArrayList()
        choiceMode = ListView.CHOICE_MODE_NONE

        for (i in list.indices) {
            addMessage(list[i])
        }
        //sortMessages(mMessageList);
        refresh()
        init()
    }

    fun init(attribute: Attribute) {
        mAttribute = attribute
        init()
    }

    /**
     * Initialize list
     */
    fun init() {
        dividerHeight = 0
        mMessageAdapter = MessageAdapter(context, 0, mChatList, mAttribute)

        adapter = mMessageAdapter

        mHandler = Handler()
        mRefreshTimer = Timer(true)
        mRefreshTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mHandler!!.post { mMessageAdapter!!.notifyDataSetChanged() }
            }
        }, 1000, mRefreshInterval)
    }

    /**
     * Set new message and refresh
     * @param message new message
     */
    fun setMessage(message: Message) {
        addMessage(message)
        refresh()
        mMessageAdapter!!.notifyDataSetChanged()
    }

    /**
     * Add message to chat list and message list.
     * Set date text before set message if sent at the different day.
     * @param message new message
     */
    fun addMessage(message: Message) {
        mMessageList.add(message)
        if (mMessageList.size == 1) {
            mChatList.add(message.dateSeparateText)
            mChatList.add(message)
            return
        }
        val prevMessage = mMessageList[mMessageList.size - 2]
        if (!TimeUtils.isSameDay(prevMessage.createdAt, message.createdAt)) {
            mChatList.add(message.dateSeparateText)
        }
        mChatList.add(message)
    }

    fun refresh() {
        sortMessages(mMessageList)
        mChatList.clear()
        mChatList.addAll(insertDateSeparator(mMessageList))
        mMessageAdapter!!.notifyDataSetChanged()
    }

    fun remove(message: Message) {
        mMessageList.remove(message)
        refresh()
    }

    fun removeAll() {
        mMessageList.clear()
        refresh()
    }


    private fun insertDateSeparator(list: List<Message>): List<Any> {
        val result = ArrayList<Any>()
        if (list.size == 0) {
            return result
        }
        result.add(list[0].dateSeparateText)
        result.add(list[0])
        if (list.size < 2) {
            return result
        }
        for (i in 1 until list.size) {
            val prevMessage = list[i - 1]
            val currMessage = list[i]
            if (!TimeUtils.isSameDay(prevMessage.createdAt, currMessage.createdAt)) {
                result.add(currMessage.dateSeparateText)
            }
            result.add(currMessage)
        }
        return result
    }

    /**
     * Sort messages
     */
    fun sortMessages(list: List<Message>?) {
        val dateComparator = MessageDateComparator()
        if (list != null) {
            Collections.sort(list, dateComparator)
        }
    }


    fun setOnKeyboardAppearListener(listener: OnKeyboardAppearListener) {
        mOnKeyboardAppearListener = listener
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        // if ListView became smaller
        if (mOnKeyboardAppearListener != null && height < oldHeight) {
            mOnKeyboardAppearListener!!.onKeyboardAppeared(true)
        }
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            //Scroll to end
            scrollToEnd()
        }
    }

    fun scrollToEnd() {
        smoothScrollToPosition(count - 1)
    }

    fun setLeftBubbleColor(color: Int) {
        mMessageAdapter!!.setLeftBubbleColor(color)
    }

    fun setRightBubbleColor(color: Int) {
        mMessageAdapter!!.setRightBubbleColor(color)
    }

    fun setUsernameTextColor(color: Int) {
        mMessageAdapter!!.setUsernameTextColor(color)
    }

    fun setSendTimeTextColor(color: Int) {
        mMessageAdapter!!.setSendTimeTextColor(color)
    }

    fun setMessageStatusColor(color: Int) {
        mMessageAdapter!!.setStatusColor(color)
    }

    fun setDateSeparatorTextColor(color: Int) {
        mMessageAdapter!!.setDateSeparatorColor(color)
    }

    fun setRightMessageTextColor(color: Int) {
        mMessageAdapter!!.setRightMessageTextColor(color)
    }

    fun setLeftMessageTextColor(color: Int) {
        mMessageAdapter!!.setLeftMessageTextColor(color)
    }

    fun setOnBubbleClickListener(listener: Message.OnBubbleClickListener) {
        mMessageAdapter!!.setOnBubbleClickListener(listener)
    }

    fun setOnBubbleLongClickListener(listener: Message.OnBubbleLongClickListener) {
        mMessageAdapter!!.setOnBubbleLongClickListener(listener)
    }

    fun setOnIconClickListener(listener: Message.OnIconClickListener) {
        mMessageAdapter!!.setOnIconClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: Message.OnIconLongClickListener) {
        mMessageAdapter!!.setOnIconLongClickListener(listener)
    }

    fun setMessageMarginTop(px: Int) {
        mMessageAdapter!!.setMessageTopMargin(px)
    }

    fun setMessageMarginBottom(px: Int) {
        mMessageAdapter!!.setMessageBottomMargin(px)
    }

    fun setRefreshInterval(refreshInterval: Long) {
        mRefreshInterval = refreshInterval
    }

    fun setMessageFontSize(size: Float) {
        mAttribute!!.messageFontSize = size
        setAttribute()
    }

    fun setUsernameFontSize(size: Float) {
        mAttribute!!.usernameFontSize = size
        setAttribute()
    }

    fun setTimeLabelFontSize(size: Float) {
        mAttribute!!.timeLabelFontSize = size
        setAttribute()
    }

    fun setMessageMaxWidth(width: Int) {
        mAttribute!!.messageMaxWidth = width
        setAttribute()
    }

    fun setDateSeparatorFontSize(size: Float) {
        mAttribute!!.dateSeparatorFontSize = size
        setAttribute()
    }

    private fun setAttribute() {
        mMessageAdapter!!.setAttribute(mAttribute)
    }
}
