package com.github.bassaer.chatmessageview.util

import android.annotation.SuppressLint

import java.text.SimpleDateFormat
import java.util.*

/**
 * time utility class
 * Created by nakayama on 2016/12/02.
 */
object TimeUtils {

    /***
     * Return formatted text of calendar
     * @param calendar Calendar object to format
     * @param format format text
     * @return formatted text
     */
    @SuppressLint("SimpleDateFormat")
    fun calendarToString(calendar: Calendar, format: String?): String {
        val sdf = SimpleDateFormat( format ?: "HH:mm", Locale.ENGLISH)
        return sdf.format(calendar.time)
    }

    /**
     * Return time difference days
     * @param prev previous date
     * @param target target date
     * @return time difference days
     */
    @JvmStatic
    fun getDiffDays(prev: Calendar, target: Calendar): Int {
        val timeDiff = prev.timeInMillis - target.timeInMillis
        val millisOfDay = 1000 * 60 * 60 * 24
        return (timeDiff / millisOfDay).toInt()
    }

    @JvmStatic
    fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
