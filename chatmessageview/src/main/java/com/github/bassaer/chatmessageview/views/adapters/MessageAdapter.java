package com.github.bassaer.chatmessageview.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.bassaer.chatmessageview.R;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Custom list adapter for the chat timeline
 * Created by nakayama on 2016/08/08.
 */
public class MessageAdapter extends ArrayAdapter<Object> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Object> mObjects;
    private ArrayList<Object> mViewTypes = new ArrayList<>();

    private int mUsernameTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500);
    private int mSendTimeTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500);
    private int mDateSeparatorColor = ContextCompat.getColor(getContext(), R.color.blueGray500);
    private int mRightMessageTextColor = Color.WHITE;
    private int mLeftMessageTextColor = Color.BLACK;
    private int mLeftBubbleColor;
    private int mRightBubbleColor;
    private int mStatusTextColor = ContextCompat.getColor(getContext(), R.color.blueGray500);
    /**
     * Default message item margin top
     */
    private int mMessageTopMargin = 5;
    /**
     * Default message item margin bottom
     */
    private int mMessageBottomMargin = 5;

    public MessageAdapter(Context context, int resource, ArrayList<Object> objects) {
        super(context, resource, objects);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
        mViewTypes.add(String.class);
        mViewTypes.add(Message.class);
        mLeftBubbleColor = ContextCompat.getColor(context, R.color.default_left_bubble_color);
        mRightBubbleColor = ContextCompat.getColor(context, R.color.default_right_bubble_color);
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
            //Item is a message
            MessageViewHolder holder;
            Message message = (Message) item;
            User user = message.getUser();

            if (message.isRightMessage()) {
                //Right message
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(R.layout.message_view_right, null);
                    holder = new MessageViewHolder();
                    holder.iconContainer = (FrameLayout) convertView.findViewById(R.id.user_icon_container);
                    holder.messageText = (TextView) convertView.findViewById(R.id.message_text);
                    holder.timeText = (TextView) convertView.findViewById(R.id.time_display_text);
                    holder.usernameContainer = (FrameLayout) convertView.findViewById(R.id.message_user_name_container);
                    holder.statusContainer = (FrameLayout) convertView.findViewById(R.id.message_status_container);
                    convertView.setTag(holder);
                } else {
                    holder = (MessageViewHolder) convertView.getTag();
                }

                //Set bubble color
                Drawable drawable = holder.messageText.getBackground();
                Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                ColorStateList colorStateList = ColorStateList.valueOf(mRightBubbleColor);
                DrawableCompat.setTintList(wrappedDrawable, colorStateList);

                //Remove view in each container
                holder.iconContainer.removeAllViews();
                holder.usernameContainer.removeAllViews();
                holder.statusContainer.removeAllViews();

                if (user.getName() != null && message.getUsernameVisibility()) {
                    View usernameView = mLayoutInflater.inflate(R.layout.user_name_right, holder.usernameContainer);
                    holder.username = (TextView) usernameView.findViewById(R.id.message_user_name);
                    holder.username.setText(user.getName());
                    holder.username.setTextColor(mUsernameTextColor);
                }

                // if false, icon is not shown.
                if (!message.isIconHided()) {
                    View iconView = mLayoutInflater.inflate(R.layout.user_icon_right, holder.iconContainer);
                    holder.icon = (CircleImageView) iconView.findViewById(R.id.user_icon);
                    if (message.getIconVisibility()) {
                        //if false, set default icon.
                        if (user.getIcon() != null) {
                            holder.icon.setImageBitmap(user.getIcon());
                        }
                    } else {
                        //Show nothing
                        holder.icon.setVisibility(View.INVISIBLE);
                    }
                }


                //Show message status
                if (message.getMessageStatusType() == Message.MESSAGE_STATUS_ICON || message.getMessageStatusType() == Message.MESSAGE_STATUS_ICON_RIGHT_ONLY) {
                    //Show message status icon
                    View statusIcon = mLayoutInflater.inflate(R.layout.message_status_icon, holder.statusContainer);
                    holder.statusIcon = (ImageView)statusIcon.findViewById(R.id.status_icon_image_view);
                    holder.statusIcon.setImageDrawable(message.getStatusIcon());
                } else if (message.getMessageStatusType() == Message.MESSAGE_STATUS_TEXT || message.getMessageStatusType() == Message.MESSAGE_STATUS_TEXT_RIGHT_ONLY) {
                    //Show message status text
                    View statusText = mLayoutInflater.inflate(R.layout.message_status_text, holder.statusContainer);
                    holder.statusText = (TextView)statusText.findViewById(R.id.status_text_view);
                    holder.statusText.setText(message.getStatusText());
                    holder.statusText.setTextColor(mStatusTextColor);
                }

                holder.messageText.setText(message.getMessageText());
                holder.timeText.setText(message.getTimeText());
                holder.messageText.setTextColor(mRightMessageTextColor);
                holder.timeText.setTextColor(mSendTimeTextColor);

                //Set Padding
                convertView.setPadding(0, mMessageTopMargin, 0, mMessageBottomMargin);

            } else {
                //Left message
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(R.layout.message_view_left, null);
                    holder = new MessageViewHolder();
                    holder.iconContainer = (FrameLayout) convertView.findViewById(R.id.user_icon_container);
                    holder.messageText = (TextView) convertView.findViewById(R.id.message_text);
                    holder.timeText = (TextView) convertView.findViewById(R.id.time_display_text);
                    holder.usernameContainer = (FrameLayout) convertView.findViewById(R.id.message_user_name_container);
                    holder.statusContainer = (FrameLayout) convertView.findViewById(R.id.message_status_container);
                    convertView.setTag(holder);
                } else {
                    holder = (MessageViewHolder) convertView.getTag();
                }

                Drawable drawable = holder.messageText.getBackground();
                Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                ColorStateList colorStateList = ColorStateList.valueOf(mLeftBubbleColor);
                DrawableCompat.setTintList(wrappedDrawable, colorStateList);


                //Remove view in each container
                holder.iconContainer.removeAllViews();
                holder.usernameContainer.removeAllViews();
                holder.statusContainer.removeAllViews();


                if (user.getName() != null && message.getUsernameVisibility()) {
                    View usernameView = mLayoutInflater.inflate(R.layout.user_name_left, holder.usernameContainer);
                    holder.username = (TextView) usernameView.findViewById(R.id.message_user_name);
                    holder.username.setText(user.getName());
                    holder.username.setTextColor(mUsernameTextColor);
                }

                // if false, icon is not shown.
                if (!message.isIconHided()) {
                    View iconView = mLayoutInflater.inflate(R.layout.user_icon_left, holder.iconContainer);
                    holder.icon = (CircleImageView) iconView.findViewById(R.id.user_icon);
                    if (message.getIconVisibility()) {
                        //if false, set default icon.
                        if (user.getIcon() != null) {
                            holder.icon.setImageBitmap(user.getIcon());
                        }
                    } else {
                        //Show nothing
                        holder.icon.setImageBitmap(null);
                    }

                }

                //Show message status
                if (message.getMessageStatusType() == Message.MESSAGE_STATUS_ICON || message.getMessageStatusType() == Message.MESSAGE_STATUS_ICON_LEFT_ONLY) {
                    //Show message status icon
                    View statusIcon = mLayoutInflater.inflate(R.layout.message_status_icon, holder.statusContainer);
                    holder.statusIcon = (ImageView)statusIcon.findViewById(R.id.status_icon_image_view);
                    holder.statusIcon.setImageDrawable(message.getStatusIcon());
                } else if (message.getMessageStatusType() == Message.MESSAGE_STATUS_TEXT || message.getMessageStatusType() == Message.MESSAGE_STATUS_TEXT_LEFT_ONLY) {
                    //Show message status text
                    View statusText = mLayoutInflater.inflate(R.layout.message_status_text, holder.statusContainer);
                    holder.statusText = (TextView)statusText.findViewById(R.id.status_text_view);
                    holder.statusText.setText(message.getStatusText());
                    holder.statusText.setTextColor(mStatusTextColor);
                }

                holder.messageText.setText(message.getMessageText());
                holder.timeText.setText(message.getTimeText());
                holder.messageText.setTextColor(mLeftMessageTextColor);
                holder.timeText.setTextColor(mSendTimeTextColor);

                //Set Padding
                convertView.setPadding(0, mMessageTopMargin, 0, mMessageBottomMargin);

            }

        }

        return convertView;
    }

    /**
     * Set left bubble background color
     * @param color left bubble color
     */
    public void setLeftBubbleColor(int color) {
        mLeftBubbleColor = color;
        notifyDataSetChanged();
    }

    /**
     * Set right bubble background color
     * @param color right bubble color
     */
    public void setRightBubbleColor(int color) {
        mRightBubbleColor = color;
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

    public void setMessageTopMargin(int messageTopMargin) {
        mMessageTopMargin = messageTopMargin;
    }

    public void setMessageBottomMargin(int messageBottomMargin) {
        mMessageBottomMargin = messageBottomMargin;
    }

    public void setStatusTextColor(int statusTextColor) {
        mStatusTextColor = statusTextColor;
        notifyDataSetChanged();
    }

    class MessageViewHolder {
        CircleImageView icon;
        FrameLayout iconContainer;
        TextView messageText;
        TextView timeText;
        TextView username;
        FrameLayout usernameContainer;
        FrameLayout statusContainer;
        ImageView statusIcon;
        TextView statusText;
    }

    class DateViewHolder {
        TextView dateSeparatorText;
    }


}
