package jp.bassaer.chatmessageview.models;

import android.graphics.Bitmap;

import java.util.Calendar;

import jp.bassaer.chatmessageview.utils.TimeUtils;

/**
 * Created by nakayama on 2016/08/08.
 */
public class Message {

    private String mUserName;
    private Bitmap mUserIcon;
    private boolean isRightMessage;
    private String mMessageText;
    private Calendar mCreatedAt;
    private String mTimeText;



    private String mDateSeparateText;

    private boolean isDateCell;

    public Message() {
        mCreatedAt = Calendar.getInstance();
        initDate();
    }

    public static class Builder {
        private Message message;

        public Builder() {
            message = new Message();
        }

        public Builder setUserName(String userName) {
            message.setUserName(userName);
            return this;
        }

        public Builder setUserIcon(Bitmap icon) {
            message.setUserIcon(icon);
            return this;
        }

        public Builder setRightMessage(boolean isRight) {
            message.setRightMessage(isRight);
            return this;
        }

        public Builder setMessageText(String messageText) {
            message.setMessageText(messageText);
            return this;
        }

        public Builder setCreatedAt(Calendar calendar) {
            message.setCreatedAt(calendar);
            return this;
        }

        public Builder setDateCell(boolean isDateCell) {
            message.setDateCell(isDateCell);
            return this;
        }

        public Message build() {
            return message;
        }

    }

    public void initDate(){
        //This is shown if the before or after message was send different day
        setDateSeparateText(TimeUtils.calendarToString(mCreatedAt, "yyyy/MM/dd"));
        //Time text under message
        setTimeText(TimeUtils.calendarToString(mCreatedAt, "HH:mm"));
    }


    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public boolean isRightMessage() {
        return isRightMessage;
    }

    public void setRightMessage(boolean isRightMessage) {
        this.isRightMessage = isRightMessage;
    }

    public Bitmap getUserIcon() {
        return mUserIcon;
    }

    public void setUserIcon(Bitmap userIcon) {
        mUserIcon = userIcon;
    }

    public String getMessageText() {
        return mMessageText;
    }

    public void setMessageText(String messageText) {
        mMessageText = messageText;
    }

    public Calendar getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Calendar calendar) {
        mCreatedAt = calendar;
        initDate();
    }

    public String getTimeText() {
        return mTimeText;

    }

    public void setTimeText(String timeText) {
        mTimeText = timeText;
    }

    public boolean isDateCell() {
        return isDateCell;
    }

    public void setDateCell(boolean isDateCell) {
        this.isDateCell = isDateCell;
    }

    public String getDateSeparateText() {
        return mDateSeparateText;
    }

    public void setDateSeparateText(String dateSeparateText) {
        mDateSeparateText = dateSeparateText;
    }

    /**
     * Return Calendar to compare the day <br>
     * Reset hour, min, sec, milli sec.<br>
     * @return formatted calendar object
     */
    public Calendar getCompareCalendar() {
        Calendar calendar = (Calendar) mCreatedAt.clone();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
