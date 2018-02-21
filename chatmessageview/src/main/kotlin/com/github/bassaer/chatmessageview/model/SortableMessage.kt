package com.github.bassaer.chatmessageview.model

import com.github.bassaer.chatmessageview.util.ITimeFormatter
import java.util.*

/**
 * Created by marcos-ambrosi on 2/21/18.
 */
abstract class SortableMessage {

    /**
     * Date separator text format.
     * This text is shown if the before or after message was sent different day
     */
    var mDateFormatter: ITimeFormatter? = null


    /**
     * The time message that was created
     */
    var createdAt = Calendar.getInstance()

    val dateSeparateText: String
        get() = mDateFormatter!!.getFormattedTimeText(createdAt!!)

    init {
        createdAt = Calendar.getInstance()
    }
}