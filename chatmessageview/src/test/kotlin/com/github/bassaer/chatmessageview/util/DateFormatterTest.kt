package com.github.bassaer.chatmessageview.util

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * <p>DateFormatter test</p>
 * Created by nakayama on 2017/11/11.
 */
internal class DateFormatterTest {

    @Test
    fun getFormattedTimeText() {
        val formatter = DateFormatter()
        val calendar = Calendar.getInstance()
        calendar.set(2017, 10, 11)
        assertEquals(formatter.getFormattedTimeText(calendar), "Nov. 11, 2017")
    }

}