package com.github.bassaer.chatmessageview.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.github.bassaer.chatmessageview.models.Attribute;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.util.MessageDateComparator;
import com.github.bassaer.chatmessageview.util.TimeUtils;
import com.github.bassaer.chatmessageview.views.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Simple chat view
 * Created by nakayama on 2016/08/08.
 */
public class MessageView extends ListView implements View.OnFocusChangeListener{

    /**
     * All contents such as right message, left message, date label
     */
    private List<Object> mChatList = new ArrayList<>();
    /**
     * Only messages
     */
    private List<Message> mMessageList = new ArrayList<>();

    private MessageAdapter mMessageAdapter;

    private OnKeyboardAppearListener mOnKeyboardAppearListener;

    /**
     * MessageView is refreshed at this time
     */
    private long mRefreshInterval = 60000;
    /**
     * Refresh scheduler
     */
    private Timer mRefreshTimer;

    private Handler mHandler;

    private Attribute mAttribute;

    public interface OnKeyboardAppearListener {
        void onKeyboardAppeared(boolean hasChanged);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAttribute = new Attribute(context, attrs);
        init();
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttribute = new Attribute(context, attrs);
        init();
    }


    public void init(List<Message> list) {
        mChatList = new ArrayList<>();
        setChoiceMode(ListView.CHOICE_MODE_NONE);

        for(int i=0; i < list.size(); i++){
            addMessage(list.get(i));
        }
        //sortMessages(mMessageList);
        refresh();
        init();
    }

    public void init(Attribute attribute) {
        mAttribute = attribute;
        init();
    }

    /**
     * Initialize list
     */
    public void init() {
        setDividerHeight(0);
        mMessageAdapter = new MessageAdapter(getContext(), 0, mChatList, mAttribute);

        setAdapter(mMessageAdapter);

        mHandler = new Handler();
        mRefreshTimer = new Timer(true);
        mRefreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMessageAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 1000, mRefreshInterval);
    }

    /**
     * Set new message and refresh
     * @param message new message
     */
    public void setMessage(Message message) {
        addMessage(message);
        refresh();
        mMessageAdapter.notifyDataSetChanged();
    }

    /**
     * Add message to chat list and message list.
     * Set date text before set message if sent at the different day.
     * @param message new message
     */
    public void addMessage(Message message) {
        mMessageList.add(message);
        if (mMessageList.size() == 1) {
            mChatList.add(message.getDateSeparateText());
            mChatList.add(message);
            return;
        }
        Message prevMessage = mMessageList.get(mMessageList.size() - 2);
        if (!TimeUtils.INSTANCE.isSameDay(prevMessage.getCreatedAt(), message.getCreatedAt())) {
            mChatList.add(message.getDateSeparateText());
        }
        mChatList.add(message);
    }

    public void refresh() {
        sortMessages(mMessageList);
        mChatList.clear();
        mChatList.addAll(insertDateSeparator(mMessageList));
        mMessageAdapter.notifyDataSetChanged();
    }

    public void remove(Message message) {
        mMessageList.remove(message);
        refresh();
    }

    public void removeAll() {
        mMessageList.clear();
        refresh();
    }


    private List<Object> insertDateSeparator(List<Message> list) {
        List<Object> result = new ArrayList<>();
        if (list.size() == 0) {
            return result;
        }
        result.add(list.get(0).getDateSeparateText());
        result.add(list.get(0));
        if (list.size() < 2) {
            return result;
        }
        for (int i = 1; i < list.size(); i++) {
            Message prevMessage = list.get(i -1);
            Message currMessage = list.get(i);
            if (!TimeUtils.isSameDay(prevMessage.getCreatedAt(), currMessage.getCreatedAt())) {
                result.add(currMessage.getDateSeparateText());
            }
            result.add(currMessage);
        }
        return result;
    }

    /**
     * Sort messages
     */
    public void sortMessages(List<Message> list) {
        MessageDateComparator dateComparator = new MessageDateComparator();
        if (list != null) {
            Collections.sort(list, dateComparator);
        }
    }


    public void setOnKeyboardAppearListener(OnKeyboardAppearListener listener) {
        mOnKeyboardAppearListener = listener;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        // if ListView became smaller
        if (mOnKeyboardAppearListener != null
                && height < oldHeight) {
            mOnKeyboardAppearListener.onKeyboardAppeared(true);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            //Scroll to end
            scrollToEnd();
        }
    }

    public void scrollToEnd() {
        smoothScrollToPosition(getCount() - 1);
    }

    public void setLeftBubbleColor(int color) {
        mMessageAdapter.setLeftBubbleColor(color);
    }

    public void setRightBubbleColor(int color) {
        mMessageAdapter.setRightBubbleColor(color);
    }

    public void setUsernameTextColor(int color) {
        mMessageAdapter.setUsernameTextColor(color);
    }

    public void setSendTimeTextColor(int color) {
        mMessageAdapter.setSendTimeTextColor(color);
    }

    public void setMessageStatusColor(int color) {
        mMessageAdapter.setStatusColor(color);
    }

    public void setDateSeparatorTextColor(int color) {
        mMessageAdapter.setDateSeparatorColor(color);
    }

    public void setRightMessageTextColor(int color) {
        mMessageAdapter.setRightMessageTextColor(color);
    }

    public void setLeftMessageTextColor(int color) {
        mMessageAdapter.setLeftMessageTextColor(color);
    }

    public void setOnBubbleClickListener(Message.OnBubbleClickListener listener) {
        mMessageAdapter.setOnBubbleClickListener(listener);
    }

    public void setOnBubbleLongClickListener(Message.OnBubbleLongClickListener listener) {
        mMessageAdapter.setOnBubbleLongClickListener(listener);
    }

    public void setOnIconClickListener(Message.OnIconClickListener listener) {
        mMessageAdapter.setOnIconClickListener(listener);
    }

    public void setOnIconLongClickListener(Message.OnIconLongClickListener listener) {
        mMessageAdapter.setOnIconLongClickListener(listener);
    }

    public void setMessageMarginTop(int px) {
        mMessageAdapter.setMessageTopMargin(px);
    }

    public void setMessageMarginBottom(int px) {
        mMessageAdapter.setMessageBottomMargin(px);
    }

    /**
     * Return last object (right message or left message or date text)
     * @return last object of chat
     */
    public Object getLastChatObject() {
        if (mChatList.size() == 0) {
            return null;
        }
        return mChatList.get(mChatList.size() - 1);
    }

    public void setRefreshInterval(long refreshInterval) {
        mRefreshInterval = refreshInterval;
    }

    public void setMessageFontSize(float size) {
        mAttribute.setMessageFontSize(size);
        setAttribute();
    }

    public void setUsernameFontSize(float size) {
        mAttribute.setUsernameFontSize(size);
        setAttribute();
    }

    public void setTimeLabelFontSize(float size) {
        mAttribute.setTimeLabelFontSize(size);
        setAttribute();
    }

    public void setMessageMaxWidth(int width) {
        mAttribute.setMessageMaxWidth(width);
        setAttribute();
    }

    public void setDateSeparatorFontSize(float size) {
        mAttribute.setDateSeparatorFontSize(size);
        setAttribute();
    }

    private void setAttribute() {
        mMessageAdapter.setAttribute(mAttribute);
    }

    public List<Message> getMessageList() {
        return mMessageList;
    }
}
