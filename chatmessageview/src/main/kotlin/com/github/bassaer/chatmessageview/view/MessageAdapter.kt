package com.github.bassaer.chatmessageview.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import com.github.bassaer.chatmessageview.R
import com.github.bassaer.chatmessageview.model.Attribute
import com.github.bassaer.chatmessageview.model.Message

import de.hdodenhof.circleimageview.CircleImageView

import kotlinx.android.synthetic.main.date_cell.view.*

import java.util.*

/**
 * Custom list adapter for the chat timeline
 * Created by nakayama on 2016/08/08.
 */
class MessageAdapter(context: Context, resource: Int, private val objects: List<Any>, private var attribute: Attribute) : ArrayAdapter<Any>(context, resource, objects) {

    private val viewTypes = ArrayList<Any>()
    private var layoutInflater = LayoutInflater.from(context)
    private var iconClickListener: Message.OnIconClickListener? = null
    private var bubbleClickListener: Message.OnBubbleClickListener? = null
    private var iconLongClickListener: Message.OnIconLongClickListener? = null
    private var bubbleLongClickListener: Message.OnBubbleLongClickListener? = null

    private var usernameTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500)
    private var sendTimeTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500)
    private var dateLabelColor = ContextCompat.getColor(getContext(), R.color.blueGray500)
    private var rightMessageTextColor = Color.WHITE
    private var leftMessageTextColor = Color.BLACK
    private var leftBubbleColor: Int = 0
    private var rightBubbleColor: Int = 0
    private var statusColor = ContextCompat.getColor(getContext(), R.color.blueGray500)
    /**
     * Default message item margin top
     */
    private var messageTopMargin = 5
    /**
     * Default message item margin bottom
     */
    private var messageBottomMargin = 5

    init {
        viewTypes.add(String::class.java)
        viewTypes.add(Message::class.java)
        leftBubbleColor = ContextCompat.getColor(context, R.color.default_left_bubble_color)
        rightBubbleColor = ContextCompat.getColor(context, R.color.default_right_bubble_color)
    }

    override fun getItemViewType(position: Int): Int {
        val item = objects[position]
        return viewTypes.indexOf(item)
    }

    override fun getViewTypeCount(): Int {
        return viewTypes.size
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        var view = convertView

        if (item is String) {
            // item is Date label
            lateinit var dateViewHolder: DateViewHolder
            view?.let {
                dateViewHolder = it.tag as DateViewHolder
            } ?: let {
                view = layoutInflater.inflate(R.layout.date_cell, null)
                dateViewHolder = DateViewHolder()
                dateViewHolder.dateLabelText = view?.dateLabelText
                view?.tag = dateViewHolder
            }

            dateViewHolder.dateLabelText?.text = item
            dateViewHolder.dateLabelText?.setTextColor(dateLabelColor)
            dateViewHolder.dateLabelText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, attribute.dateSeparatorFontSize)

        } else {
            //Item is a message
            lateinit var messageViewHolder: MessageViewHolder
            val message: Message = item as Message
            if (position > 0) {
                getItem(position - 1).let {
                    if (it is Message && it.user.getId() == message.user.getId()) {
                        //If send same person, hide username and icon.
                        message.iconVisibility = false
                        message.usernameVisibility = false
                    }
                }
            }

            val user = message.user

            view?.let {
                messageViewHolder = it.tag as MessageViewHolder
            } ?: run {
                view = layoutInflater
                        .inflate(if (message.isRight) R.layout.message_view_right else R.layout.message_view_left,
                                null)
                messageViewHolder = MessageViewHolder()
                messageViewHolder.iconContainer = view?.findViewById(R.id.userIconContainer)
                messageViewHolder.mainMessageContainer = view?.findViewById(R.id.mainMessageContainer)
                messageViewHolder.timeText = view?.findViewById(R.id.timeLabelText)
                messageViewHolder.usernameContainer = view?.findViewById(R.id.usernameContainer)
                messageViewHolder.statusContainer = view?.findViewById(R.id.statusContainer)
                view?.tag = messageViewHolder
            }

            //Remove view in each container
            messageViewHolder.iconContainer?.removeAllViews()
            messageViewHolder.usernameContainer?.removeAllViews()
            messageViewHolder.statusContainer?.removeAllViews()
            messageViewHolder.mainMessageContainer?.removeAllViews()

            if (user.getName() != null && message.usernameVisibility) {
                layoutInflater.inflate(
                        if (message.isRight) R.layout.user_name_right else R.layout.user_name_left,
                        messageViewHolder.usernameContainer).let {
                    messageViewHolder.username = it.findViewById(R.id.message_user_name)
                    messageViewHolder.username?.text = user.getName()
                    messageViewHolder.username?.setTextColor(usernameTextColor)
                    messageViewHolder.username?.setTextSize(TypedValue.COMPLEX_UNIT_PX, attribute.usernameFontSize)
                }

            }

            // if false, icon is not shown.
            if (!message.isIconHided) {
                layoutInflater.inflate(if (message.isRight) R.layout.user_icon_right else R.layout.user_icon_left,
                        messageViewHolder.iconContainer).let {
                    messageViewHolder.icon = it.findViewById(R.id.user_icon)
                }

                if (message.iconVisibility) {
                    //if false, set default icon.
                    if (user.getIcon() != null) {
                        messageViewHolder.icon?.setImageBitmap(user.getIcon())
                    }

                } else {
                    //Show nothing
                    messageViewHolder.icon?.visibility = View.INVISIBLE
                }
            }


            //Show message status
            if (message.statusStyle == Message.STATUS_ICON || message.statusStyle == Message.STATUS_ICON_RIGHT_ONLY) {
                //Show message status icon
                layoutInflater.inflate(R.layout.message_status_icon, messageViewHolder.statusContainer).let {
                    messageViewHolder.statusIcon = it.findViewById(R.id.status_icon_image_view)
                    messageViewHolder.statusIcon?.setImageDrawable(message.statusIcon)
                    setColorDrawable(statusColor, messageViewHolder.statusIcon?.drawable)
                }

            } else if (message.statusStyle == Message.STATUS_TEXT || message.statusStyle == Message.STATUS_TEXT_RIGHT_ONLY) {
                //Show message status text
                layoutInflater.inflate(R.layout.message_status_text, messageViewHolder.statusContainer).let {
                    messageViewHolder.statusText = it.findViewById(R.id.status_text_view)
                    messageViewHolder.statusText?.text = message.statusText
                    messageViewHolder.statusText?.setTextColor(statusColor)
                }
            }

            //Set text or picture on message bubble
            when (message.type) {
                Message.Type.PICTURE -> {
                    //Set picture
                    layoutInflater.inflate(
                            if (message.isRight) R.layout.message_picture_right else R.layout.message_picture_left,
                            messageViewHolder.mainMessageContainer).let {
                        messageViewHolder.messagePicture = it.findViewById(R.id.message_picture)
                        messageViewHolder.messagePicture?.setImageBitmap(message.picture)
                    }

                }
                Message.Type.LINK -> {
                    //Set text
                    layoutInflater.inflate(
                            if (message.isRight) R.layout.message_link_right else R.layout.message_link_left,
                            messageViewHolder.mainMessageContainer).let {
                        messageViewHolder.messageLink = it.findViewById(R.id.message_link)
                        messageViewHolder.messageLink?.text = message.text
                        //Set bubble color
                        setColorDrawable(
                                if (message.isRight) rightBubbleColor else leftBubbleColor,
                                messageViewHolder.messageLink?.background
                        )
                        //Set message text color
                        messageViewHolder.messageLink?.setTextColor(
                                if (message.isRight) rightMessageTextColor else leftMessageTextColor
                        )
                    }

                } else -> {
                    layoutInflater.inflate(
                            if (message.isRight) R.layout.message_text_right else R.layout.message_text_left,
                            messageViewHolder.mainMessageContainer).let {
                        messageViewHolder.messageText = it.findViewById(R.id.message_text)
                        messageViewHolder.messageText?.setTextIsSelectable(attribute.isTextSelectable)
                        messageViewHolder.messageText?.text = message.text
                        setColorDrawable(
                                if (message.isRight) rightBubbleColor else leftBubbleColor,
                                messageViewHolder.messageText?.background
                        )
                        messageViewHolder.messageText?.setTextColor(
                                if (message.isRight) rightMessageTextColor else leftMessageTextColor
                        )
                    }
                }
            }

            messageViewHolder.timeText?.text = message.timeText

            messageViewHolder.timeText?.setTextColor(sendTimeTextColor)

            //Set Padding
            view?.setPadding(0, messageTopMargin, 0, messageBottomMargin)

            if (messageViewHolder.mainMessageContainer != null) {
                //Set bubble click listener
                messageViewHolder.mainMessageContainer?.setOnClickListener { bubbleClickListener?.onClick(message) }


                //Set bubble long click listener
                messageViewHolder.mainMessageContainer?.setOnLongClickListener {
                    bubbleLongClickListener?.onLongClick(message)
                    true//ignore onclick event
                }
            }

            //Set icon events if icon is shown
            if (message.iconVisibility && messageViewHolder.icon != null) {
                //Set icon click listener
                messageViewHolder.icon?.setOnClickListener { iconClickListener?.onIconClick(message) }

                messageViewHolder.icon?.setOnLongClickListener {
                    iconLongClickListener?.onIconLongClick(message)
                    true
                }

            }

            messageViewHolder.messageText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, attribute.messageFontSize)
            messageViewHolder.messageText?.maxWidth = attribute.messageMaxWidth
            messageViewHolder.timeText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, attribute.timeLabelFontSize)
        }

        return view!!
    }

    /**
     * Add color to drawable
     * @param color setting color
     * @param drawable which be set color
     */
    private fun setColorDrawable(color: Int, drawable: Drawable?) {
        if (drawable == null) {
            return
        }
        val colorStateList = ColorStateList.valueOf(color)
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(wrappedDrawable, colorStateList)
    }

    /**
     * Set left bubble background color
     * @param color left bubble color
     */
    fun setLeftBubbleColor(color: Int) {
        leftBubbleColor = color
        notifyDataSetChanged()
    }

    /**
     * Set right bubble background color
     * @param color right bubble color
     */
    fun setRightBubbleColor(color: Int) {
        rightBubbleColor = color
        notifyDataSetChanged()
    }

    fun setOnIconClickListener(onIconClickListener: Message.OnIconClickListener) {
        iconClickListener = onIconClickListener
    }

    fun setOnBubbleClickListener(onBubbleClickListener: Message.OnBubbleClickListener) {
        bubbleClickListener = onBubbleClickListener
    }

    fun setOnIconLongClickListener(onIconLongClickListener: Message.OnIconLongClickListener) {
        iconLongClickListener = onIconLongClickListener
    }

    fun setOnBubbleLongClickListener(onBubbleLongClickListener: Message.OnBubbleLongClickListener) {
        bubbleLongClickListener = onBubbleLongClickListener
    }

    fun setUsernameTextColor(usernameTextColor: Int) {
        this.usernameTextColor = usernameTextColor
        notifyDataSetChanged()
    }

    fun setSendTimeTextColor(sendTimeTextColor: Int) {
        this.sendTimeTextColor = sendTimeTextColor
        notifyDataSetChanged()
    }

    fun setDateSeparatorColor(dateSeparatorColor: Int) {
        this.dateLabelColor = dateSeparatorColor
        notifyDataSetChanged()
    }

    fun setRightMessageTextColor(rightMessageTextColor: Int) {
        this.rightMessageTextColor = rightMessageTextColor
        notifyDataSetChanged()
    }

    fun setLeftMessageTextColor(leftMessageTextColor: Int) {
        this.leftMessageTextColor = leftMessageTextColor
        notifyDataSetChanged()
    }

    fun setMessageTopMargin(messageTopMargin: Int) {
        this.messageTopMargin = messageTopMargin
    }

    fun setMessageBottomMargin(messageBottomMargin: Int) {
        this.messageBottomMargin = messageBottomMargin
    }

    fun setStatusColor(statusTextColor: Int) {
        statusColor = statusTextColor
        notifyDataSetChanged()
    }

    fun setAttribute(attribute: Attribute) {
        this.attribute = attribute
        notifyDataSetChanged()
    }

    internal inner class MessageViewHolder {
        var icon: CircleImageView? = null
        var iconContainer: FrameLayout? = null
        var messagePicture: RoundImageView? = null
        var messageLink: TextView? = null
        var messageText: TextView? = null
        var timeText: TextView? = null
        var username: TextView? = null
        var mainMessageContainer: FrameLayout? = null
        var usernameContainer: FrameLayout? = null
        var statusContainer: FrameLayout? = null
        var statusIcon: ImageView? = null
        var statusText: TextView? = null

    }

    internal inner class DateViewHolder {
        var dateLabelText: TextView? = null
    }


}
