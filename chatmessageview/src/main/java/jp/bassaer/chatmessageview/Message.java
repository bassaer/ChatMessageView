package jp.bassaer.chatmessageview;

import android.graphics.Bitmap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nakayama on 2016/08/08.
 */
public class Message {

    private String mUserName;
    private Bitmap mUserIcon;
    private boolean isRightMessage;
    private String mMessageText;
    private Date mCreatedDate;
    private String mTimeText;



    private String mDateSeparateText;

    private boolean isDateCell;

    public Message() {
        mCreatedDate = Calendar.getInstance().getTime();
        initDate();
    }

    public void initDate(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        setDateSeparateText(simpleDateFormat.format(mCreatedDate));
        simpleDateFormat.applyPattern("HH:mm");
        setTimeText(simpleDateFormat.format(mCreatedDate));
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

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(Calendar calendar) {

        mCreatedDate = calendar.getTime();
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

    public Date getCompareDate(){
        try {
            return DateFormat.getDateInstance().parse(getDateSeparateText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mCreatedDate;
    }

}
