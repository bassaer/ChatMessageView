package com.github.bassaer.chatmessageview.util


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

/**
 * <p>TimeUtil test</p>
 * Created by nakayama on 2017/11/11.
 */
internal class TimeUtilsTest {
    @Test
    fun calendarToString() {
        val calendar = Calendar.getInstance()
        calendar.set(2017, 10, 11, 22,33)
        assertEquals(TimeUtils.calendarToString(calendar, null), "22:33")
        assertEquals(TimeUtils.calendarToString(calendar, "MMM. dd, yyyy HH:mm"), "Nov. 11, 2017 22:33")
    }

    @Test
    fun getDiffDays() {
        val firstDay = Calendar.getInstance()
        val secondDay = Calendar.getInstance()
        firstDay.set(2017, 10, 11, 22,33)
        secondDay.set(2017, 10, 13, 22,33)
        assertEquals(TimeUtils.getDiffDays(firstDay, secondDay), -2)
        assertEquals(TimeUtils.getDiffDays(secondDay, firstDay), 2)
        assertEquals(TimeUtils.getDiffDays(firstDay, firstDay), 0)
        firstDay.set(2017, 10, 11, 22,34)
        assertEquals(TimeUtils.getDiffDays(firstDay, secondDay), -1)
    }

}