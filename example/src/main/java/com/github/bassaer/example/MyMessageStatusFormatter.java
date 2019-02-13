package com.github.bassaer.example;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.github.bassaer.chatmessageview.util.IMessageStatusIconFormatter;
import com.github.bassaer.chatmessageview.util.IMessageStatusTextFormatter;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Message status format sample
 * Created by nakayama on 2017/02/16.
 */
public class MyMessageStatusFormatter implements IMessageStatusIconFormatter, IMessageStatusTextFormatter {
    private static final int STATUS_DELIVERING = 0;
    static final int STATUS_DELIVERED = 1;
    static final int STATUS_SEEN = 2;
    private static final int STATUS_ERROR = 3;
    private Drawable mDeliveringIcon;
    private Drawable mDeliveredIcon;
    private Drawable mSeenIcon;
    private Drawable mErrorIcon;
    private String mDeliveringText;
    private String mDeliveredText;
    private String mSeenText;
    private String mErrorText;

    MyMessageStatusFormatter(Context context) {
        //Init icons
        mDeliveringIcon = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_mail_outline));
        mDeliveredIcon = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_done));
        mSeenIcon = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_done_all));
        mErrorIcon = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_report));
        //Set colors
        ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blueGray500));
        DrawableCompat.setTintList(mErrorIcon, colorStateList);
        DrawableCompat.setTintList(mDeliveringIcon, colorStateList);
        DrawableCompat.setTintList(mDeliveredIcon, colorStateList);
        DrawableCompat.setTintList(mSeenIcon, colorStateList);

        //Init status labels
        mDeliveringText = context.getString(R.string.sending);
        mDeliveredText = context.getString(R.string.sent);
        mSeenText = context.getString(R.string.seen);
        mErrorText = context.getString(R.string.error);
    }

    @Override
    public Drawable getStatusIcon(int status, boolean isRightMessage) {
        if (!isRightMessage) {
            return null;
        }
        switch (status) {
            case STATUS_DELIVERING:
                return mDeliveringIcon;
            case STATUS_DELIVERED:
                return mDeliveredIcon;
            case STATUS_SEEN:
                return mSeenIcon;
            case STATUS_ERROR:
                return mErrorIcon;
        }
        return null;
    }

    @Override
    public String getStatusText(int status, boolean isRightMessage) {
        switch (status) {
            case STATUS_DELIVERING:
                return mDeliveringText;
            case STATUS_DELIVERED:
                return mDeliveredText;
            case STATUS_SEEN:
                return mSeenText;
            case STATUS_ERROR:
                return mErrorText;
        }
        return "";
    }
}
