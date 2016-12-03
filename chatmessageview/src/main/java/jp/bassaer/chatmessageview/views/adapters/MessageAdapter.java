package jp.bassaer.chatmessageview.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bassaer.chatmessageview.R;
import jp.bassaer.chatmessageview.models.Message;

/**
 * Created by nakayama on 2016/08/08.
 */
public class MessageAdapter extends ArrayAdapter<Object> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Object> mObjects;
    private ArrayList<Object> mViewTypes = new ArrayList<>();

    private GradientDrawable mLeftBubble;
    private GradientDrawable mRightBubble;
    private int mUsernameTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500);
    private int mSendTimeTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500);
    private int mDateSeparatorColor = ContextCompat.getColor(getContext(), R.color.blueGray500);
    private int mRightMessageTextColor = Color.WHITE;
    private int mLeftMessageTextColor = Color.BLACK;

    public MessageAdapter(Context context, int resource, ArrayList<Object> objects) {
        super(context, resource, objects);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
        mViewTypes.add(String.class);
        mViewTypes.add(Message.class);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mObjects.get(position);
        return mViewTypes.indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return mViewTypes.size();
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = getItem(position);

        if (item instanceof String) {
            // item is Date label
            DateViewHolder dateViewHolder;
            String dateText = (String) item;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.date_cell, null);
                dateViewHolder = new DateViewHolder();
                dateViewHolder.dateSeparatorText = (TextView) convertView.findViewById(R.id.date_separate_text);
                convertView.setTag(dateViewHolder);
            } else {
                dateViewHolder = (DateViewHolder) convertView.getTag();
            }
            dateViewHolder.dateSeparatorText.setText(dateText);
            dateViewHolder.dateSeparatorText.setTextColor(mDateSeparatorColor);
        } else {
            //Item is message
            MessageViewHolder holder;
            Message message = (Message) item;

            if (message.isRightMessage()) {
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(R.layout.message_view_right, null);
                    holder = new MessageViewHolder();
                    holder.icon = (CircleImageView) convertView.findViewById(R.id.user_icon);
                    holder.messageText = (TextView) convertView.findViewById(R.id.message_text);
                    holder.timeText = (TextView) convertView.findViewById(R.id.time_display_text);
                    holder.userName = (TextView) convertView.findViewById(R.id.message_user_name);
                    convertView.setTag(holder);
                } else {
                    holder = (MessageViewHolder) convertView.getTag();
                }
                if (mRightBubble != null) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        holder.messageText.setBackgroundDrawable(mRightBubble);
                    } else {
                        holder.messageText.setBackground(mRightBubble);
                    }
                }
                holder.icon.setImageBitmap(message.getUserIcon());
                holder.messageText.setText(message.getMessageText());
                holder.timeText.setText(message.getTimeText());
                holder.userName.setText(message.getUserName());
                holder.messageText.setTextColor(mRightMessageTextColor);
                holder.timeText.setTextColor(mSendTimeTextColor);
                holder.userName.setTextColor(mUsernameTextColor);

            } else {
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(R.layout.message_view_left, null);
                    holder = new MessageViewHolder();
                    holder.icon = (CircleImageView) convertView.findViewById(R.id.user_icon);
                    holder.messageText = (TextView) convertView.findViewById(R.id.message_text);
                    holder.timeText = (TextView) convertView.findViewById(R.id.time_display_text);
                    holder.userName = (TextView) convertView.findViewById(R.id.message_user_name);
                    convertView.setTag(holder);
                } else {
                    holder = (MessageViewHolder) convertView.getTag();
                }

                if (mLeftBubble != null) {
                    //Set background color
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        //setBackgroundDrawable is deprecated after Jelly Bean
                        holder.messageText.setBackgroundDrawable(mLeftBubble);
                    } else {
                        holder.messageText.setBackground(mLeftBubble);
                    }
                }

                holder.icon.setImageBitmap(message.getUserIcon());
                holder.messageText.setText(message.getMessageText());
                holder.timeText.setText(message.getTimeText());
                holder.userName.setText(message.getUserName());
                holder.messageText.setTextColor(mLeftMessageTextColor);
                holder.timeText.setTextColor(mSendTimeTextColor);
                holder.userName.setTextColor(mUsernameTextColor);
            }

        }

        return convertView;
    }

    public void setLeftBubbleColor(GradientDrawable drawable) {
        mLeftBubble = drawable;
        notifyDataSetChanged();
    }

    public void setRightBubbleColor(GradientDrawable drawable) {
        mRightBubble = drawable;
        notifyDataSetChanged();
    }

    public void setUsernameTextColor(int usernameTextColor) {
        mUsernameTextColor = usernameTextColor;
        notifyDataSetChanged();
    }

    public void setSendTimeTextColor(int sendTimeTextColor) {
        mSendTimeTextColor = sendTimeTextColor;
        notifyDataSetChanged();
    }

    public void setDateSeparatorColor(int dateSeparatorColor) {
        mDateSeparatorColor = dateSeparatorColor;
        notifyDataSetChanged();
    }

    public void setRightMessageTextColor(int rightMessageTextColor) {
        mRightMessageTextColor = rightMessageTextColor;
        notifyDataSetChanged();
    }

    public void setLeftMessageTextColor(int leftMessageTextColor) {
        mLeftMessageTextColor = leftMessageTextColor;
        notifyDataSetChanged();
    }

    class MessageViewHolder {
        CircleImageView icon;
        TextView messageText;
        TextView timeText;
        TextView userName;
    }

    class DateViewHolder {
        TextView dateSeparatorText;
    }


}
