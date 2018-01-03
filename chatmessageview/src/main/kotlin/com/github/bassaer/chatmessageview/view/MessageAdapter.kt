package com.github.bassaer.chatmessageview.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.github.bassaer.chatmessageview.R
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.models.Attribute
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


/**
 * Custom list adapter for the chat timeline
 * Created by nakayama on 2016/08/08.
 */
class MessageAdapter(context: Context, resource: Int, private val mObjects: List<Any>, private var mAttribute: Attribute?) : ArrayAdapter<Any>(context, resource, mObjects) {

    private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val mViewTypes = ArrayList<Any>()

    private var mOnIconClickListener: Message.OnIconClickListener? = null
    private var mOnBubbleClickListener: Message.OnBubbleClickListener? = null
    private var mOnIconLongClickListener: Message.OnIconLongClickListener? = null
    private var mOnBubbleLongClickListener: Message.OnBubbleLongClickListener? = null

    private var mUsernameTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500)
    private var mSendTimeTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500)
    private var mDateSeparatorColor = ContextCompat.getColor(getContext(), R.color.blueGray500)
    private var mRightMessageTextColor = Color.WHITE
    private var mLeftMessageTextColor = Color.BLACK
    private var mLeftBubbleColor: Int = 0
    private var mRightBubbleColor: Int = 0
    private var mStatusColor = ContextCompat.getColor(getContext(), R.color.blueGray500)
    /**
     * Default message item margin top
     */
    private var mMessageTopMargin = 5
    /**
     * Default message item margin bottom
     */
    private var mMessageBottomMargin = 5

    init {
        mViewTypes.add(String::class.java)
        mViewTypes.add(Message::class.java)
        mLeftBubbleColor = ContextCompat.getColor(context, R.color.default_left_bubble_color)
        mRightBubbleColor = ContextCompat.getColor(context, R.color.default_right_bubble_color)
    }

    override fun getItemViewType(position: Int): Int {
        val item = mObjects[position]
        return mViewTypes.indexOf(item)
    }

    override fun getViewTypeCount(): Int {
        return mViewTypes.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView)
    }

    fun getView(position: Int, convertView: View?): View {
        var convertView = convertView
        val item = getItem(position)

        if (item is String) {
            // item is Date label
            val dateViewHolder: DateViewHolder
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.date_cell, null)
                dateViewHolder = DateViewHolder()
                dateViewHolder.dateSeparatorText = convertView!!.findViewById(R.id.date_separate_text)
                convertView.tag = dateViewHolder
            } else {
                dateViewHolder = convertView.tag as DateViewHolder
            }
            dateViewHolder.dateSeparatorText!!.text = item
            dateViewHolder.dateSeparatorText!!.setTextColor(mDateSeparatorColor)
            dateViewHolder.dateSeparatorText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttribute!!.dateSeparatorFontSize)
        } else {
            //Item is a message
            val holder: MessageViewHolder
            val message = item as Message
            if (position > 0) {
                val prevItem = getItem(position - 1)
                if (prevItem is Message) {
                    if (prevItem.user!!.getId() == message.user!!.getId()) {
                        //If send same person, hide username and icon.
                        message.iconVisibility = false
                        message.usernameVisibility = false
                    }
                }
            }

            val user = message.user

            if (message.isRightMessage) {
                //Right message
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(R.layout.message_view_right, null)
                    holder = MessageViewHolder()
                    holder.iconContainer = convertView!!.findViewById(R.id.user_icon_container)
                    holder.mainMessageContainer = convertView.findViewById(R.id.main_message_container)
                    holder.timeText = convertView.findViewById(R.id.time_label_text)
                    holder.usernameContainer = convertView.findViewById(R.id.message_user_name_container)
                    holder.statusContainer = convertView.findViewById(R.id.message_status_container)
                    convertView.tag = holder
                } else {
                    holder = convertView.tag as MessageViewHolder
                }

                //Remove view in each container
                holder.iconContainer!!.removeAllViews()
                holder.usernameContainer!!.removeAllViews()
                holder.statusContainer!!.removeAllViews()
                holder.mainMessageContainer!!.removeAllViews()

                if (user!!.getName() != null && message.usernameVisibility) {
                    val usernameView = mLayoutInflater.inflate(R.layout.user_name_right, holder.usernameContainer)
                    holder.username = usernameView.findViewById(R.id.message_user_name)
                    holder.username!!.text = user.getName()
                    holder.username!!.setTextColor(mUsernameTextColor)
                    holder.username!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttribute!!.usernameFontSize)
                }

                // if false, icon is not shown.
                if (!message.isIconHided) {
                    val iconView = mLayoutInflater.inflate(R.layout.user_icon_right, holder.iconContainer)
                    holder.icon = iconView.findViewById(R.id.user_icon)
                    if (message.iconVisibility) {
                        //if false, set default icon.
                        if (user.getIcon() != null) {
                            holder.icon!!.setImageBitmap(user.getIcon())
                        }

                    } else {
                        //Show nothing
                        holder.icon!!.visibility = View.INVISIBLE
                    }
                }


                //Show message status
                if (message.messageStatusType == Message.MESSAGE_STATUS_ICON || message.messageStatusType == Message.MESSAGE_STATUS_ICON_RIGHT_ONLY) {
                    //Show message status icon
                    val statusIcon = mLayoutInflater.inflate(R.layout.message_status_icon, holder.statusContainer)
                    holder.statusIcon = statusIcon.findViewById(R.id.status_icon_image_view)
                    holder.statusIcon!!.setImageDrawable(message.statusIcon)
                    setColorDrawable(mStatusColor, holder.statusIcon!!.drawable)
                } else if (message.messageStatusType == Message.MESSAGE_STATUS_TEXT || message.messageStatusType == Message.MESSAGE_STATUS_TEXT_RIGHT_ONLY) {
                    //Show message status text
                    val statusText = mLayoutInflater.inflate(R.layout.message_status_text, holder.statusContainer)
                    holder.statusText = statusText.findViewById(R.id.status_text_view)
                    holder.statusText!!.text = message.statusText
                    holder.statusText!!.setTextColor(mStatusColor)
                }

                //Set text or picture on message bubble
                when (message.type) {
                    Message.Type.PICTURE -> {
                        //Set picture
                        val pictureBubble = mLayoutInflater.inflate(R.layout.message_picture_right, holder.mainMessageContainer)
                        holder.messagePicture = pictureBubble.findViewById(R.id.message_picture)
                        holder.messagePicture!!.setImageBitmap(message.picture)
                    }
                    Message.Type.LINK -> {
                        //Set text
                        val linkBubble = mLayoutInflater.inflate(R.layout.message_link_right, holder.mainMessageContainer)
                        holder.messageLink = linkBubble.findViewById(R.id.message_link)
                        holder.messageLink!!.text = message.messageText
                        //Set bubble color
                        setColorDrawable(mRightBubbleColor, holder.messageLink!!.background)
                        //Set message text color
                        holder.messageLink!!.setTextColor(mRightMessageTextColor)
                    }
                    Message.Type.TEXT -> {
                        //Set text
                        val textBubble = mLayoutInflater.inflate(R.layout.message_text_right, holder.mainMessageContainer)
                        holder.messageText = textBubble.findViewById(R.id.message_text)
                        holder.messageText!!.text = message.messageText
                        //Set bubble color
                        setColorDrawable(mRightBubbleColor, holder.messageText!!.background)
                        //Set message text color
                        holder.messageText!!.setTextColor(mRightMessageTextColor)
                    }
                    else -> {
                        val textBubble = mLayoutInflater.inflate(R.layout.message_text_right, holder.mainMessageContainer)
                        holder.messageText = textBubble.findViewById(R.id.message_text)
                        holder.messageText!!.text = message.messageText
                        setColorDrawable(mRightBubbleColor, holder.messageText!!.background)
                        holder.messageText!!.setTextColor(mRightMessageTextColor)
                    }
                }

                holder.timeText!!.text = message.timeText

                holder.timeText!!.setTextColor(mSendTimeTextColor)

                //Set Padding
                convertView.setPadding(0, mMessageTopMargin, 0, mMessageBottomMargin)

            } else {
                //Left message
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(R.layout.message_view_left, null)
                    holder = MessageViewHolder()
                    holder.iconContainer = convertView!!.findViewById(R.id.user_icon_container)
                    holder.mainMessageContainer = convertView.findViewById(R.id.main_message_container)
                    holder.timeText = convertView.findViewById(R.id.time_label_text)
                    holder.usernameContainer = convertView.findViewById(R.id.message_user_name_container)
                    holder.statusContainer = convertView.findViewById(R.id.message_status_container)
                    convertView.tag = holder
                } else {
                    holder = convertView.tag as MessageViewHolder
                }


                //Remove view in each container
                holder.iconContainer!!.removeAllViews()
                holder.usernameContainer!!.removeAllViews()
                holder.statusContainer!!.removeAllViews()
                holder.mainMessageContainer!!.removeAllViews()


                if (user!!.getName() != null && message.usernameVisibility) {
                    val usernameView = mLayoutInflater.inflate(R.layout.user_name_left, holder.usernameContainer)
                    holder.username = usernameView.findViewById(R.id.message_user_name)
                    holder.username!!.text = user.getName()
                    holder.username!!.setTextColor(mUsernameTextColor)
                    holder.username!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttribute!!.usernameFontSize)
                }

                // if false, icon is not shown.
                if (!message.isIconHided) {
                    val iconView = mLayoutInflater.inflate(R.layout.user_icon_left, holder.iconContainer)
                    holder.icon = iconView.findViewById(R.id.user_icon)
                    if (message.iconVisibility) {
                        //if false, set default icon.
                        if (user.getIcon() != null) {
                            holder.icon!!.setImageBitmap(user.getIcon())
                        }
                    } else {
                        //Show nothing
                        holder.icon!!.setImageBitmap(null)
                    }

                }

                //Show message status
                if (message.messageStatusType == Message.MESSAGE_STATUS_ICON || message.messageStatusType == Message.MESSAGE_STATUS_ICON_LEFT_ONLY) {
                    //Show message status icon
                    val statusIcon = mLayoutInflater.inflate(R.layout.message_status_icon, holder.statusContainer)
                    holder.statusIcon = statusIcon.findViewById(R.id.status_icon_image_view)
                    holder.statusIcon!!.setImageDrawable(message.statusIcon)
                    setColorDrawable(mStatusColor, holder.statusIcon!!.drawable)
                } else if (message.messageStatusType == Message.MESSAGE_STATUS_TEXT || message.messageStatusType == Message.MESSAGE_STATUS_TEXT_LEFT_ONLY) {
                    //Show message status text
                    val statusText = mLayoutInflater.inflate(R.layout.message_status_text, holder.statusContainer)
                    holder.statusText = statusText.findViewById(R.id.status_text_view)
                    holder.statusText!!.text = message.statusText
                    holder.statusText!!.setTextColor(mStatusColor)
                }

                //Set text or picture on message bubble
                when (message.type) {
                    Message.Type.PICTURE -> {
                        //Set picture
                        val pictureBubble = mLayoutInflater.inflate(R.layout.message_picture_left, holder.mainMessageContainer)
                        holder.messagePicture = pictureBubble.findViewById(R.id.message_picture)
                        holder.messagePicture!!.setImageBitmap(message.picture)
                    }
                    Message.Type.LINK -> {
                        //Set link
                        val linkBubble = mLayoutInflater.inflate(R.layout.message_link_left, holder.mainMessageContainer)
                        holder.messageLink = linkBubble.findViewById(R.id.message_link)
                        holder.messageLink!!.text = message.messageText
                        //Set bubble color
                        setColorDrawable(mLeftBubbleColor, holder.messageLink!!.background)
                        //Set message text color
                        holder.messageLink!!.setTextColor(mLeftMessageTextColor)
                    }
                    Message.Type.TEXT -> {
                        //Set text
                        val textBubble = mLayoutInflater.inflate(R.layout.message_text_left, holder.mainMessageContainer)
                        holder.messageText = textBubble.findViewById(R.id.message_text)
                        holder.messageText!!.text = message.messageText
                        //Set bubble color
                        setColorDrawable(mLeftBubbleColor, holder.messageText!!.background)
                        //Set message text color
                        holder.messageText!!.setTextColor(mLeftMessageTextColor)
                    }
                    else -> {
                        val textBubble = mLayoutInflater.inflate(R.layout.message_text_left, holder.mainMessageContainer)
                        holder.messageText = textBubble.findViewById(R.id.message_text)
                        holder.messageText!!.text = message.messageText
                        setColorDrawable(mLeftBubbleColor, holder.messageText!!.background)
                        holder.messageText!!.setTextColor(mLeftMessageTextColor)
                    }
                }

                holder.timeText!!.text = message.timeText
                holder.timeText!!.setTextColor(mSendTimeTextColor)

                //Set Padding
                convertView.setPadding(0, mMessageTopMargin, 0, mMessageBottomMargin)

            }

            if (holder.mainMessageContainer != null) {
                //Set bubble click listener
                if (mOnBubbleClickListener != null) {
                    holder.mainMessageContainer!!.setOnClickListener { mOnBubbleClickListener!!.onClick(message) }
                }

                //Set bubble long click listener
                if (mOnBubbleLongClickListener != null) {
                    holder.mainMessageContainer!!.setOnLongClickListener {
                        mOnBubbleLongClickListener!!.onLongClick(message)
                        true//ignore onclick event
                    }
                }
            }

            //Set icon events if icon is shown
            if (message.iconVisibility && holder.icon != null) {
                //Set icon click listener
                if (mOnIconClickListener != null) {
                    holder.icon!!.setOnClickListener { mOnIconClickListener!!.onIconClick(message) }
                }

                if (mOnIconLongClickListener != null) {
                    holder.icon!!.setOnLongClickListener {
                        mOnIconLongClickListener!!.onIconLongClick(message)
                        true
                    }
                }
            }

            if (null != holder.messageText) {
                holder.messageText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttribute!!.messageFontSize)
                holder.messageText!!.maxWidth = mAttribute!!.messageMaxWidth
            }
            holder.timeText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttribute!!.timeLabelFontSize)
        }

        return convertView
    }

    /**
     * Add color to drawable
     * @param color setting color
     * @param drawable which be set color
     */
    fun setColorDrawable(color: Int, drawable: Drawable?) {
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
        mLeftBubbleColor = color
        notifyDataSetChanged()
    }

    /**
     * Set right bubble background color
     * @param color right bubble color
     */
    fun setRightBubbleColor(color: Int) {
        mRightBubbleColor = color
        notifyDataSetChanged()
    }

    fun setOnIconClickListener(onIconClickListener: Message.OnIconClickListener) {
        mOnIconClickListener = onIconClickListener
    }

    fun setOnBubbleClickListener(onBubbleClickListener: Message.OnBubbleClickListener) {
        mOnBubbleClickListener = onBubbleClickListener
    }

    fun setOnIconLongClickListener(onIconLongClickListener: Message.OnIconLongClickListener) {
        mOnIconLongClickListener = onIconLongClickListener
    }

    fun setOnBubbleLongClickListener(onBubbleLongClickListener: Message.OnBubbleLongClickListener) {
        mOnBubbleLongClickListener = onBubbleLongClickListener
    }

    fun setUsernameTextColor(usernameTextColor: Int) {
        mUsernameTextColor = usernameTextColor
        notifyDataSetChanged()
    }

    fun setSendTimeTextColor(sendTimeTextColor: Int) {
        mSendTimeTextColor = sendTimeTextColor
        notifyDataSetChanged()
    }

    fun setDateSeparatorColor(dateSeparatorColor: Int) {
        mDateSeparatorColor = dateSeparatorColor
        notifyDataSetChanged()
    }

    fun setRightMessageTextColor(rightMessageTextColor: Int) {
        mRightMessageTextColor = rightMessageTextColor
        notifyDataSetChanged()
    }

    fun setLeftMessageTextColor(leftMessageTextColor: Int) {
        mLeftMessageTextColor = leftMessageTextColor
        notifyDataSetChanged()
    }

    fun setMessageTopMargin(messageTopMargin: Int) {
        mMessageTopMargin = messageTopMargin
    }

    fun setMessageBottomMargin(messageBottomMargin: Int) {
        mMessageBottomMargin = messageBottomMargin
    }

    fun setStatusColor(statusTextColor: Int) {
        mStatusColor = statusTextColor
        notifyDataSetChanged()
    }

    fun setAttribute(attribute: Attribute) {
        mAttribute = attribute
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
        var dateSeparatorText: TextView? = null
    }


}
