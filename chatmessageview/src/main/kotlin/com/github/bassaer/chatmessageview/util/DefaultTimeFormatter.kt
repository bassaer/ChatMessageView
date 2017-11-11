package com.github.bassaer.chatmessageview.util

import java.util.*

/**
 * Default Time format that show hour and minute
 * Created by nakayama on 2017/02/18.
 */
class DefaultTimeFormatter : ITimeFormatter {
    override fun getFormattedTimeText(createdAt: Calendar): String {
        return TimeUtils.calendarToString(createdAt, "HH:mm")
    }
}
