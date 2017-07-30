package com.github.bassaer.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Wrapper class {@link ArrayList} for save data
 * Created by nakayama on 2017/01/13.
 */
public class MessageList {

    private ArrayList<SaveMessage> mMessages;

    public MessageList() {
        mMessages = new ArrayList<>();
    }

    public ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        for (SaveMessage saveMessage : mMessages) {
            messages.add(convertMessage(saveMessage));
        }
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        for (Message message : messages) {
            mMessages.add(convertMessage(message));
        }
    }

    public void add(Message message) {
        mMessages.add(convertMessage(message));
    }

    public Message get(int index) {
        return convertMessage(mMessages.get(index));
    }

    public int size() {
        return mMessages.size();
    }

    private SaveMessage convertMessage(Message message) {
        SaveMessage saveMessage = new SaveMessage(
                message.getUser().getId(),
                message.getUser().getName(),
                message.getMessageText(),
                message.getCreatedAt(),
                message.isRightMessage());

        saveMessage.setType(message.getType());

        if (message.getType() == Message.Type.PICTURE
                && message.getPicture() != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.getPicture().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            saveMessage.setPictureString(Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));
        }

        return saveMessage;
    }

    private Message convertMessage(SaveMessage saveMessage) {
        User user = new User(saveMessage.getId(), saveMessage.getUsername(), null);

        Message message = new Message.Builder()
                .setUser(user)
                .setMessageText(saveMessage.getContent())
                .setRightMessage(saveMessage.isRightMessage())
                .setCreatedAt(saveMessage.getCreatedAt())
                .setType(saveMessage.getType())
                .build();

        if (saveMessage.getPictureString() != null) {
            byte[] bytes = Base64.decode(saveMessage.getPictureString().getBytes(), Base64.DEFAULT);
            message.setPicture(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
        return message;
    }

    private class SaveMessage {
        private int mId;
        private String mUsername;
        private String mContent;
        private Calendar mCreatedAt;
        private boolean mRightMessage;
        private String mPictureString;
        private Message.Type mType;

        public SaveMessage(int id, String username, String content, Calendar createdAt, boolean isRightMessage) {
            mId = id;
            mUsername = username;
            mContent = content;
            mCreatedAt = createdAt;
            mRightMessage = isRightMessage;
        }

        public int getId() {
            return mId;
        }

        public String getUsername() {
            return mUsername;
        }

        public String getContent() {
            return mContent;
        }

        public Calendar getCreatedAt() {
            return mCreatedAt;
        }

        public boolean isRightMessage() {
            return mRightMessage;
        }

        public String getPictureString() {
            return mPictureString;
        }

        public void setPictureString(String pictureString) {
            mPictureString = pictureString;
        }

        public Message.Type getType() {
            return mType;
        }

        public void setType(Message.Type type) {
            mType = type;
        }
    }
}
