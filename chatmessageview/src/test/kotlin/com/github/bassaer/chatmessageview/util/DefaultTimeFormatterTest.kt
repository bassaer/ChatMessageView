package com.github.bassaer.chatmessageview.util

import org.junit.Assert.assertEquals
import org.junit.Test

import java.util.*

/**
 * Created by nakayama on 2017/11/11.
 */
internal class DefaultTimeFormatterTest {
    @Test
    fun getFormattedTimeText() {
        val formatter = DefaultTimeFormatter()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 22)
        calendar.set(Calendar.MINUTE, 10)
        assertEquals(formatter.getFormattedTimeText(calendar), "22:10")
    }

}