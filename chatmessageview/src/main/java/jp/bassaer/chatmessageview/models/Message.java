package jp.bassaer.chatmessageview.models;

import java.util.Calendar;

import jp.bassaer.chatmessageview.utils.DateFormatter;
import jp.bassaer.chatmessageview.utils.ITimeFormatter;
import jp.bassaer.chatmessageview.utils.SendTimeFormatter;

/**
 * Message object
 * Created by nakayama on 2016/08/08.
 */
public class Message {

    /**
     * Sender information
     */
    private User mUser;

    /**
     * Whether sender username is shown or not
     */
    private boolean mUsernameVisibility = true;
    /**
     * If true, there is the icon space but invisible.
     */
    private boolean mIconVisibility = true;
    /**
     * If true, there is no icon space.
     */
    private boolean mHideIcon = false;

    /**
     * Whether the message is shown right side or not.
     */
    private boolean isRightMessage;

    /**
     * Message content text
     */
    private String mMessageText;

    /**
     * The time message that was created
     */
    private Calendar mCreatedAt;

    /**
     * Whether cell of list view is date separator text or not.
     */
    private boolean isDateCell;

    /**
     * Text format of the send time that is next to the message
     */
    private ITimeFormatter mSendTimeFormatter;

    /**
     * Date separator text format.
     * This text is shown if the before or after message was sent different day
     */
    private ITimeFormatter mDateFormatter;

    public Message() {
        mCreatedAt = Calendar.getInstance();
        mSendTimeFormatter = new SendTimeFormatter();
        mDateFormatter = new DateFormatter();
    }

    /**
     * Message builder
     */
    public static class Builder {
        private Message message;

        public Builder() {
            message = new Message();
        }

        public Builder setUser(User user) {
            message.setUser(user);
            return this;
        }

        public Builder setUsernameVisibility(boolean visibility) {
            message.setUsernameVisibility(visibility);
            return this;
        }

        public Builder setUserIconVisibility(boolean visibility) {
            message.setUsernameVisibility(visibility);
            return this;
        }

        public Builder hideIcon(boolean hide) {
            message.hideIcon(hide);
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

        public Builder setSendTimeFormatter(ITimeFormatter sendTimeFormatter) {
            message.setSendTimeFormatter(sendTimeFormatter);
            return this;
        }

        public Builder setDateFormatter(ITimeFormatter dateFormatter) {
            message.setDateFormatter(dateFormatter);
            return this;
        }

        public Message build() {
            return message;
        }

    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public boolean getUsernameVisibility() {
        return mUsernameVisibility;
    }

    public void setUsernameVisibility(boolean usernameVisibility) {
        mUsernameVisibility = usernameVisibility;
    }

    public boolean isIconHided() {
        return mHideIcon;
    }

    public void hideIcon(boolean hideIcon) {
        mHideIcon = hideIcon;
    }

    public boolean isRightMessage() {
        return isRightMessage;
    }

    public boolean getIconVisibility() {
        return mIconVisibility;
    }

    public void setIconVisibility(boolean iconVisibility) {
        mIconVisibility = iconVisibility;
    }

    public void setRightMessage(boolean isRightMessage) {
        this.isRightMessage = isRightMessage;
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
    }

    public String getTimeText() {
        return mSendTimeFormatter.getFormattedTimeText(mCreatedAt);
    }

    public boolean isDateCell() {
        return isDateCell;
    }

    public void setDateCell(boolean isDateCell) {
        this.isDateCell = isDateCell;
    }

    public String getDateSeparateText() {
        return mDateFormatter.getFormattedTimeText(mCreatedAt);
    }

    /**
     * Set custom send time text formatter
     * @param sendTimeFormatter custom send time formatter
     */
    public void setSendTimeFormatter(ITimeFormatter sendTimeFormatter) {
        mSendTimeFormatter = sendTimeFormatter;
    }

    /**
     * Set custom date text formatter
     * @param dateFormatter custom date formatter
     */
    public void setDateFormatter(ITimeFormatter dateFormatter) {
        mDateFormatter = dateFormatter;
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
