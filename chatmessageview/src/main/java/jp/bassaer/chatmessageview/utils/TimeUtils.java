package jp.bassaer.chatmessageview.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by nakayama on 2016/12/02.
 */
public class TimeUtils {

    /***
     * Return formatted text of calendar
    * @param calendar Calendar object to format
    * @param format format text
    * @return formatted text
    */
    public static String calendarToString(Calendar calendar, String format) {
        if (format == null) {
            format = "HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }
}
