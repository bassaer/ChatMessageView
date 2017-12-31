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
import android.widget.*
import com.github.bassaer.chatmessageview.R
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.models.Attribute

/**
 * Chat view with edit view and send button
 * Created by nakayama on 2016/08/08.
 */
class ChatView : LinearLayout {
    var messageView: MessageView? = null
        private set
    private var mInputText: EditText? = null
    private var mSendButton: ImageButton? = null
    private var mOptionButton: ImageButton? = null
    private var mChatContainer: SwipeRefreshLayout? = null
    private var mInputMethodManager: InputMethodManager? = null
    private var mSendIconId = R.drawable.ic_action_send
    private var mOptionIconId = R.drawable.ic_action_add
    private var mSendIconColor = ContextCompat.getColor(context, R.color.lightBlue500)
    private var mOptionIconColor = ContextCompat.getColor(context, R.color.lightBlue500)
    private var mAutoScroll = true
    private var mAutoHidingKeyboard = true
    private var mAttribute: Attribute? = null

    var inputText: String
        get() = mInputText!!.text.toString()
        set(input) = mInputText!!.setText(input)

    val isEnabledSendButton: Boolean
        get() = mSendButton!!.isEnabled

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mAttribute = Attribute(context, attrs)
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mAttribute = Attribute(context, attrs)
        init(context)
    }

    private fun init(context: Context) {
        mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val layout = LayoutInflater.from(context).inflate(R.layout.chat_view, this)
        messageView = layout.findViewById(R.id.message_view)
        mInputText = layout.findViewById(R.id.message_edit_text)
        mSendButton = layout.findViewById(R.id.send_button)
        mChatContainer = layout.findViewById(R.id.chat_container)
        mChatContainer!!.isEnabled = false
        if (mAttribute!!.isOptionButtonEnable) {
            val optionButtonContainer = layout.findViewById<FrameLayout>(R.id.option_button_container)
            val optionView = LayoutInflater.from(context).inflate(R.layout.option_button, optionButtonContainer)
            mOptionButton = optionView.findViewById(R.id.option_button)
        }

        messageView!!.init(mAttribute!!)

        messageView!!.isFocusableInTouchMode = true
        //if touched Chat screen
        messageView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l -> hideKeyboard() }

        //if touched empty space
        mChatContainer!!.setOnClickListener { hideKeyboard() }


        messageView!!.setOnKeyboardAppearListener(object : MessageView.OnKeyboardAppearListener {
            override fun onKeyboardAppeared(hasChanged: Boolean) {
                //Appeared keyboard
                if (hasChanged) {
                    Handler().postDelayed({
                        //Scroll to end
                        messageView!!.scrollToEnd()
                    }, 500)
                }
            }
        })

    }

    /**
     * Hide software keyboard
     */
    fun hideKeyboard() {
        //Hide keyboard
        mInputMethodManager!!.hideSoftInputFromWindow(
                messageView!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        //Move focus to background
        messageView!!.requestFocus()
    }

    fun setLeftBubbleColor(color: Int) {
        messageView!!.setLeftBubbleColor(color)
    }

    fun setRightBubbleColor(color: Int) {
        messageView!!.setRightBubbleColor(color)
    }


    /**
     * Set message to right side
     * @param message Sent message
     */
    fun send(message: Message) {
        messageView!!.setMessage(message)

        //Hide keyboard after post
        if (mAutoHidingKeyboard) {
            hideKeyboard()
        }
        //Scroll to bottom after post
        if (mAutoScroll) {
            messageView!!.scrollToEnd()
        }
    }

    /**
     * Set message to left side
     * @param message Received message
     */
    fun receive(message: Message) {
        messageView!!.setMessage(message)
        if (mAutoScroll) {
            messageView!!.scrollToEnd()
        }
    }

    fun setOnClickSendButtonListener(listener: View.OnClickListener) {
        mSendButton!!.setOnClickListener(listener)
    }

    fun setOnClickOptionButtonListener(listener: View.OnClickListener) {
        if (mOptionButton != null) {
            mOptionButton!!.setOnClickListener(listener)
        }
    }

    override fun setBackgroundColor(color: Int) {
        messageView!!.setBackgroundColor(color)
        mChatContainer!!.setBackgroundColor(color)
    }

    fun setSendButtonColor(color: Int) {
        mSendIconColor = color
        mSendButton!!.setImageDrawable(getColoredDrawable(color, mSendIconId))
    }

    fun setOptionButtonColor(color: Int) {
        if (mOptionButton != null) {
            mOptionIconColor = color
            mOptionButton!!.setImageDrawable(getColoredDrawable(color, mOptionIconId))
        }
    }

    fun getColoredDrawable(color: Int, iconId: Int): Drawable {
        val colorStateList = ColorStateList.valueOf(color)
        val icon = ContextCompat.getDrawable(context, iconId)
        val wrappedDrawable = DrawableCompat.wrap(icon)
        DrawableCompat.setTintList(wrappedDrawable, colorStateList)
        return wrappedDrawable
    }

    fun setSendIcon(resId: Int) {
        mSendIconId = resId
        setSendButtonColor(mSendIconColor)
    }

    fun setOptionIcon(resId: Int) {
        mOptionIconId = resId
        setOptionButtonColor(mOptionIconColor)
    }

    fun setInputTextHint(hint: String) {
        mInputText!!.hint = hint
    }

    fun setUsernameTextColor(color: Int) {
        messageView!!.setUsernameTextColor(color)
    }

    fun setSendTimeTextColor(color: Int) {
        messageView!!.setSendTimeTextColor(color)
    }

    fun setDateSeparatorColor(color: Int) {
        messageView!!.setDateSeparatorTextColor(color)
    }

    fun setRightMessageTextColor(color: Int) {
        messageView!!.setRightMessageTextColor(color)
    }

    fun setMessageStatusTextColor(color: Int) {
        messageView!!.setMessageStatusColor(color)
    }

    fun setOnBubbleClickListener(listener: Message.OnBubbleClickListener) {
        messageView!!.setOnBubbleClickListener(listener)
    }

    fun setOnBubbleLongClickListener(listener: Message.OnBubbleLongClickListener) {
        messageView!!.setOnBubbleLongClickListener(listener)
    }

    fun setOnIconClickListener(listener: Message.OnIconClickListener) {
        messageView!!.setOnIconClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: Message.OnIconLongClickListener) {
        messageView!!.setOnIconLongClickListener(listener)
    }

    fun setLeftMessageTextColor(color: Int) {
        messageView!!.setLeftMessageTextColor(color)
    }

    /**
     * Auto Scroll when message received.
     * @param enable Whether auto scroll is enable or not
     */
    fun setAutoScroll(enable: Boolean) {
        mAutoScroll = enable
    }

    fun setMessageMarginTop(px: Int) {
        messageView!!.setMessageMarginTop(px)
    }

    fun setMessageMarginBottom(px: Int) {
        messageView!!.setMessageMarginBottom(px)
    }

    /**
     * Add TEXT watcher
     * @param watcher behavior when text view status is changed
     */
    fun addTextWatcher(watcher: TextWatcher) {
        mInputText!!.addTextChangedListener(watcher)
    }

    fun setEnableSendButton(enable: Boolean) {
        mSendButton!!.isEnabled = enable
    }

    /**
     * Auto hiding keyboard after post
     * @param autoHidingKeyboard if true, keyboard will be hided after post
     */
    fun setAutoHidingKeyboard(autoHidingKeyboard: Boolean) {
        mAutoHidingKeyboard = autoHidingKeyboard
    }

    fun setOnRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener) {
        mChatContainer!!.setOnRefreshListener(listener)
    }

    fun setRefreshing(refreshing: Boolean) {
        mChatContainer!!.isRefreshing = refreshing
    }

    fun setEnableSwipeRefresh(enable: Boolean) {
        mChatContainer!!.isEnabled = enable
    }

    fun addInputChangedListener(watcher: TextWatcher) {
        mInputText!!.addTextChangedListener(watcher)
    }

    fun scrollToEnd() {
        messageView!!.scrollToEnd()
    }

    fun setMaxInputLine(lines: Int) {
        mInputText!!.maxLines = lines
    }

    fun setMessageFontSize(size: Float) {
        messageView!!.setMessageFontSize(size)
    }

    fun setUsernameFontSize(size: Float) {
        messageView!!.setUsernameFontSize(size)
    }

    fun setTimeLabelFontSize(size: Float) {
        messageView!!.setTimeLabelFontSize(size)
    }

    fun setMessageMaxWidth(width: Int) {
        messageView!!.setMessageMaxWidth(width)
    }

    fun setDateSeparatorFontSize(size: Float) {
        messageView!!.setDateSeparatorFontSize(size)
    }

}
