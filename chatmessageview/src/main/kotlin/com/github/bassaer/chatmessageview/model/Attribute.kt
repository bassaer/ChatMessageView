package com.github.bassaer.chatmessageview.models

import android.content.Context
import android.util.AttributeSet
import com.github.bassaer.chatmessageview.R

/**
 * View attribute
 * Created by nakayama on 2017/11/08.
 */
class Attribute(context: Context, attrs: AttributeSet?) {
    var messageFontSize: Float
    var usernameFontSize: Float
    var timeLabelFontSize: Float
    var messageMaxWidth: Int
    var dateSeparatorFontSize: Float
    var isOptionButtonEnable: Boolean

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MessageView)
        this.messageFontSize = typedArray.getDimension(
                R.styleable.MessageView_message_font_size,
                context.resources.getDimension(R.dimen.font_normal)
        )
        this.usernameFontSize = typedArray.getDimension(
                R.styleable.MessageView_username_font_size,
                context.resources.getDimension(R.dimen.font_small)
        )
        this.timeLabelFontSize = typedArray.getDimension(
                R.styleable.MessageView_time_label_font_size,
                context.resources.getDimension(R.dimen.font_small)
        )
        this.messageMaxWidth = typedArray.getDimensionPixelSize(
                R.styleable.MessageView_message_max_width,
                context.resources.getDimensionPixelSize(R.dimen.width_normal)
        )
        this.dateSeparatorFontSize = typedArray.getDimension(
                R.styleable.MessageView_date_separator_font_size,
                context.resources.getDimension(R.dimen.font_small)
        )
        this.isOptionButtonEnable = typedArray.getBoolean(
                R.styleable.MessageView_option_button_enable,
                false
        )
        typedArray.recycle()
    }
}