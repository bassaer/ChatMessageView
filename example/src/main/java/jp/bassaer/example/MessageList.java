package jp.bassaer.example;

import java.util.ArrayList;
import java.util.Calendar;

import jp.bassaer.chatmessageview.models.Message;
import jp.bassaer.chatmessageview.models.User;

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
        return new SaveMessage(
                message.getUser().getId(),
                message.getUser().getName(),
                message.getMessageText(),
                message.getCreatedAt(),
                message.isRightMessage());
    }

    private Message convertMessage(SaveMessage saveMessage) {
        User user = new User(saveMessage.getId(), saveMessage.getUsername(), null);
        Message message = new Message.Builder()
                .setUser(user)
                .setMessageText(saveMessage.getContent())
                .setRightMessage(saveMessage.isRightMessage())
                .setCreatedAt(saveMessage.getCreatedAt())
                .build();
        return message;
    }

    private class SaveMessage {
        private int mId;
        private String mUsername;
        private String mContent;
        private Calendar mCreatedAt;
        private boolean mRightMessage;

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
    }
}
