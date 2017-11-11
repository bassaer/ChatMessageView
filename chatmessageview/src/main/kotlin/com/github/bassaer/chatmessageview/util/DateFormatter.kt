package com.github.bassaer.chatmessageview.util

import com.github.bassaer.chatmessageview.utils.TimeUtils
import java.util.*

/**
 * Date formatter of chat timeline separator.
 * Created by nakayama on 2017/01/13.
 */
class DateFormatter : ITimeFormatter {
    override fun getFormattedTimeText(createdAt: Calendar): String {
        return TimeUtils.calendarToString(createdAt, "MMM. dd, yyyy")
    }
}
