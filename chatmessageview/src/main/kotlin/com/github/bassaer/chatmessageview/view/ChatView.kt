package com.github.bassaer.chatmessageview.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.LinearLayout
import com.github.bassaer.chatmessageview.R
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.models.Attribute
import kotlinx.android.synthetic.main.chat_view.view.*
import kotlinx.android.synthetic.main.option_button.view.*

/**
 * Chat view with edit view and send button
 * Created by nakayama on 2016/08/08.
 */
class ChatView : LinearLayout {
    private lateinit var inputMethodManager: InputMethodManager
    private var sendIconId = R.drawable.ic_action_send
    private var optionIconId = R.drawable.ic_action_add
    private var sendIconColor = ContextCompat.getColor(context, R.color.lightBlue500)
    private var optionIconColor = ContextCompat.getColor(context, R.color.lightBlue500)
    private var isEnableAutoScroll = true
    private var isEnableAutoHidingKeyboard = true
    private var attribute: Attribute

    var inputText: String
        get() = inputBox.text.toString()
        set(input) = inputBox.setText(input)

    val isEnabledSendButton: Boolean
        get() = sendButton.isEnabled

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        attribute = Attribute(context, attrs)
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attribute = Attribute(context, attrs)
        init(context)
    }

    private fun init(context: Context) {
        inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        LayoutInflater.from(context).inflate(R.layout.chat_view, this)
        chatContainer.isEnabled = false
        if (attribute.isOptionButtonEnable) {
            LayoutInflater.from(context).inflate(R.layout.option_button, optionButtonContainer)
        }

        messageView.init(attribute)

        messageView.isFocusableInTouchMode = true
        //if touched Chat screen
        messageView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ -> hideKeyboard() }

        //if touched empty space
        chatContainer.setOnClickListener { hideKeyboard() }


        messageView.setOnKeyboardAppearListener(object : MessageView.OnKeyboardAppearListener {
            override fun onKeyboardAppeared(hasChanged: Boolean) {
                //Appeared keyboard
                if (hasChanged) {
                    Handler().postDelayed({
                        //Scroll to end
                        messageView.scrollToEnd()
                    }, 500)
                }
            }
        })

    }

    /**
     * Hide software keyboard
     */
    private fun hideKeyboard() {
        //Hide keyboard
        inputMethodManager.hideSoftInputFromWindow(
                messageView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        //Move focus to background
        messageView.requestFocus()
    }

    fun setLeftBubbleColor(color: Int) {
        messageView.setLeftBubbleColor(color)
    }

    fun setRightBubbleColor(color: Int) {
        messageView.setRightBubbleColor(color)
    }


    /**
     * Set message to right side
     * @param message Sent message
     */
    fun send(message: Message) {
        messageView.setMessage(message)

        //Hide keyboard after post
        if (isEnableAutoHidingKeyboard) {
            hideKeyboard()
        }
        //Scroll to bottom after post
        if (isEnableAutoScroll) {
            messageView.scrollToEnd()
        }
    }

    /**
     * Set message to left side
     * @param message Received message
     */
    fun receive(message: Message) {
        messageView.setMessage(message)
        if (isEnableAutoScroll) {
            messageView.scrollToEnd()
        }
    }

    fun setOnClickSendButtonListener(listener: View.OnClickListener) {
        sendButton.setOnClickListener(listener)
    }

    fun setOnClickOptionButtonListener(listener: View.OnClickListener) {
        optionButton?.setOnClickListener(listener)
    }

    var inputType: Int
        get() = inputBox.inputType
        set(type) {
            inputBox.inputType = type
        }


    var inputTextColor: Int
        get() = inputBox.currentTextColor
        set(color) {
            inputBox.setTextColor(color)
        }

    fun setInputTextSize(unit: Int, size: Float) {
        inputBox.setTextSize(unit, size)
    }

    override fun setBackgroundColor(color: Int) {
        messageView.setBackgroundColor(color)
        chatContainer.setBackgroundColor(color)
    }

    fun setSendButtonColor(color: Int) {
        sendIconColor = color
        sendButton.setImageDrawable(getColoredDrawable(color, sendIconId))
    }

    fun setOptionButtonColor(color: Int) {
            optionIconColor = color
            optionButton.setImageDrawable(getColoredDrawable(color, optionIconId))
    }

    private fun getColoredDrawable(color: Int, iconId: Int): Drawable {
        val colorStateList = ColorStateList.valueOf(color)
        val icon = ContextCompat.getDrawable(context, iconId)
        val wrappedDrawable = DrawableCompat.wrap(icon)
        DrawableCompat.setTintList(wrappedDrawable, colorStateList)
        return wrappedDrawable
    }

    fun setSendIcon(resId: Int) {
        sendIconId = resId
        setSendButtonColor(sendIconColor)
    }

    fun setOptionIcon(resId: Int) {
        optionIconId = resId
        setOptionButtonColor(optionIconColor)
    }

    fun setInputTextHint(hint: String) {
        inputBox.hint = hint
    }

    fun setUsernameTextColor(color: Int) {
        messageView.setUsernameTextColor(color)
    }

    fun setSendTimeTextColor(color: Int) {
        messageView.setSendTimeTextColor(color)
    }

    fun setDateSeparatorColor(color: Int) {
        messageView.setDateSeparatorTextColor(color)
    }

    fun setRightMessageTextColor(color: Int) {
        messageView.setRightMessageTextColor(color)
    }

    fun setMessageStatusTextColor(color: Int) {
        messageView.setMessageStatusColor(color)
    }

    fun setMessageStatusColor(color: Int){
        messageView.setMessageStatusColor(color)
    }

    fun updateMessageStatus(message: Message, status: Int) {
        messageView.updateMessageStatus(message, status)
    }

    fun setOnBubbleClickListener(listener: Message.OnBubbleClickListener) {
        messageView.setOnBubbleClickListener(listener)
    }

    fun setOnBubbleLongClickListener(listener: Message.OnBubbleLongClickListener) {
        messageView.setOnBubbleLongClickListener(listener)
    }

    fun setOnIconClickListener(listener: Message.OnIconClickListener) {
        messageView.setOnIconClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: Message.OnIconLongClickListener) {
        messageView.setOnIconLongClickListener(listener)
    }

    fun setLeftMessageTextColor(color: Int) {
        messageView.setLeftMessageTextColor(color)
    }

    /**
     * Auto Scroll when message received.
     * @param enable Whether auto scroll is enable or not
     */
    fun setAutoScroll(enable: Boolean) {
        isEnableAutoScroll = enable
    }

    fun setMessageMarginTop(px: Int) {
        messageView.setMessageMarginTop(px)
    }

    fun setMessageMarginBottom(px: Int) {
        messageView.setMessageMarginBottom(px)
    }

    /**
     * Add TEXT watcher
     * @param watcher behavior when text view status is changed
     */
    fun addTextWatcher(watcher: TextWatcher) {
        inputBox.addTextChangedListener(watcher)
    }

    fun setEnableSendButton(enable: Boolean) {
        sendButton.isEnabled = enable
    }

    /**
     * Auto hiding keyboard after post
     * @param autoHidingKeyboard if true, keyboard will be hided after post
     */
    fun setAutoHidingKeyboard(autoHidingKeyboard: Boolean) {
        this.isEnableAutoHidingKeyboard = autoHidingKeyboard
    }

    fun setOnRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener) {
        chatContainer.setOnRefreshListener(listener)
    }

    fun setRefreshing(refreshing: Boolean) {
        chatContainer.isRefreshing = refreshing
    }

    fun setEnableSwipeRefresh(enable: Boolean) {
        chatContainer.isEnabled = enable
    }

    fun addInputChangedListener(watcher: TextWatcher) {
        inputBox.addTextChangedListener(watcher)
    }

    fun scrollToEnd() {
        messageView.scrollToEnd()
    }

    fun setMaxInputLine(lines: Int) {
        inputBox.maxLines = lines
    }

    fun setMessageFontSize(size: Float) {
        messageView.setMessageFontSize(size)
    }

    fun setUsernameFontSize(size: Float) {
        messageView.setUsernameFontSize(size)
    }

    fun setTimeLabelFontSize(size: Float) {
        messageView.setTimeLabelFontSize(size)
    }

    fun setMessageMaxWidth(width: Int) {
        messageView.setMessageMaxWidth(width)
    }

    fun setDateSeparatorFontSize(size: Float) {
        messageView.setDateSeparatorFontSize(size)
    }

    fun getMessageView(): MessageView = messageView
}
