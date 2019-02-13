package com.github.bassaer.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.model.IChatUser;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.util.ChatBot;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.github.bassaer.chatmessageview.view.MessageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;

/**
 * Simple chat example activity
 * Created by nakayama on 2016/12/03.
 */
public class MessengerActivity extends Activity {

    @VisibleForTesting
    protected static final int RIGHT_BUBBLE_COLOR = R.color.colorPrimaryDark;
    @VisibleForTesting
    protected static final int LEFT_BUBBLE_COLOR = R.color.gray300;
    @VisibleForTesting
    protected static final int BACKGROUND_COLOR = R.color.blueGray400;
    @VisibleForTesting
    protected static final int SEND_BUTTON_COLOR = R.color.blueGray500;
    @VisibleForTesting
    protected static final int SEND_ICON = R.drawable.ic_action_send;
    @VisibleForTesting
    protected static final int OPTION_BUTTON_COLOR = R.color.teal500;
    @VisibleForTesting
    protected static final int RIGHT_MESSAGE_TEXT_COLOR = Color.WHITE;
    @VisibleForTesting
    protected static final int LEFT_MESSAGE_TEXT_COLOR = Color.BLACK;
    @VisibleForTesting
    protected static final int USERNAME_TEXT_COLOR = Color.WHITE;
    @VisibleForTesting
    protected static final int SEND_TIME_TEXT_COLOR = Color.WHITE;
    @VisibleForTesting
    protected static final int DATA_SEPARATOR_COLOR = Color.WHITE;
    @VisibleForTesting
    protected static final int MESSAGE_STATUS_TEXT_COLOR = Color.WHITE;
    @VisibleForTesting
    protected static final String INPUT_TEXT_HINT = "New message..";
    @VisibleForTesting
    protected static final int MESSAGE_MARGIN = 5;

    private ChatView mChatView;
    private MessageList mMessageList;
    private ArrayList<User> mUsers;

    private int mReplyDelay = -1;

    private static final int READ_REQUEST_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        initUsers();

        mChatView = findViewById(R.id.chat_view);

        //Load saved messages
        loadMessages();

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this,RIGHT_BUBBLE_COLOR));
        mChatView.setLeftBubbleColor(ContextCompat.getColor(this, LEFT_BUBBLE_COLOR));
        mChatView.setBackgroundColor(ContextCompat.getColor(this, BACKGROUND_COLOR));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, SEND_BUTTON_COLOR));
        mChatView.setSendIcon(SEND_ICON);
        mChatView.setOptionIcon(R.drawable.ic_account_circle);
        mChatView.setOptionButtonColor(OPTION_BUTTON_COLOR);
        mChatView.setRightMessageTextColor(RIGHT_MESSAGE_TEXT_COLOR);
        mChatView.setLeftMessageTextColor(LEFT_MESSAGE_TEXT_COLOR);
        mChatView.setUsernameTextColor(USERNAME_TEXT_COLOR);
        mChatView.setSendTimeTextColor(SEND_TIME_TEXT_COLOR);
        mChatView.setDateSeparatorColor(DATA_SEPARATOR_COLOR);
        mChatView.setMessageStatusTextColor(MESSAGE_STATUS_TEXT_COLOR);
        mChatView.setInputTextHint(INPUT_TEXT_HINT);
        mChatView.setMessageMarginTop(MESSAGE_MARGIN);
        mChatView.setMessageMarginBottom(MESSAGE_MARGIN);
        mChatView.setMaxInputLine(5);
        mChatView.setUsernameFontSize(getResources().getDimension(R.dimen.font_small));
        mChatView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mChatView.setInputTextColor(ContextCompat.getColor(this, R.color.red500));
        mChatView.setInputTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        mChatView.setOnBubbleClickListener(new Message.OnBubbleClickListener() {
            @Override
            public void onClick(Message message) {
                mChatView.updateMessageStatus(message, MyMessageStatusFormatter.STATUS_SEEN);
                Toast.makeText(
                        MessengerActivity.this,
                        "click : " + message.getUser().getName() + " - " + message.getText(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        mChatView.setOnIconClickListener(new Message.OnIconClickListener() {
            @Override
            public void onIconClick(Message message) {
                Toast.makeText(
                        MessengerActivity.this,
                        "click : icon " + message.getUser().getName(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        mChatView.setOnIconLongClickListener(new Message.OnIconLongClickListener() {
            @Override
            public void onIconLongClick(Message message) {
                Toast.makeText(
                        MessengerActivity.this,
                        "Removed this message \n" + message.getText(),
                        Toast.LENGTH_SHORT
                ).show();
                mChatView.getMessageView().remove(message);
            }
        });

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUsers();
                //new message
                Message message = new Message.Builder()
                        .setUser(mUsers.get(0))
                        .setRight(true)
                        .setText(mChatView.getInputText())
                        .hideIcon(true)
                        .setStatusIconFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                        .setStatusTextFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                        .setStatusStyle(Message.Companion.getSTATUS_ICON())
                        .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                        .build();

                //Set to chat view
                mChatView.send(message);
                //Add message list
                mMessageList.add(message);
                //Reset edit text
                mChatView.setInputText("");

                receiveMessage(message.getText());
            }

        });

        //Click option button
        mChatView.setOnClickOptionButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void openGallery() {
        Intent intent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private void receiveMessage(String sendText) {
        //Ignore hey
        if (!sendText.contains("hey")) {

            //Receive message
            final Message receivedMessage = new Message.Builder()
                    .setUser(mUsers.get(1))
                    .setRight(false)
                    .setText(ChatBot.INSTANCE.talk(mUsers.get(0).getName(), sendText))
                    .setStatusIconFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                    .setStatusTextFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                    .setStatusStyle(Message.Companion.getSTATUS_ICON())
                    .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                    .build();

            if (sendText.equals( Message.Type.PICTURE.name())) {
                receivedMessage.setText("Nice!");
            }

            // This is a demo bot
            // Return within 3 seconds
            if (mReplyDelay < 0) {
                mReplyDelay = (new Random().nextInt(4) + 1) * 1000;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChatView.receive(receivedMessage);
                    //Add message list
                    mMessageList.add(receivedMessage);
                }
            }, mReplyDelay);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != READ_REQUEST_CODE || resultCode != RESULT_OK || data == null) {
            return;
        }
        Uri uri = data.getData();
        try {
            Bitmap picture = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Message message = new Message.Builder()
                    .setRight(true)
                    .setText(Message.Type.PICTURE.name())
                    .setUser(mUsers.get(0))
                    .hideIcon(true)
                    .setPicture(picture)
                    .setType(Message.Type.PICTURE)
                    .setStatusIconFormatter(new MyMessageStatusFormatter(MessengerActivity.this))
                    .setStatusStyle(Message.Companion.getSTATUS_ICON())
                    .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                    .build();
            mChatView.send(message);
            //Add message list
            mMessageList.add(message);
            receiveMessage(Message.Type.PICTURE.name());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

    }

    private void initUsers() {
        mUsers = new ArrayList<>();
        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        final User me = new User(myId, myName, myIcon);
        final User you = new User(yourId, yourName, yourIcon);

        mUsers.add(me);
        mUsers.add(you);
    }

    /**
     * Load saved messages
     */
    private void loadMessages() {
        List<Message> messages = new ArrayList<>();
        mMessageList = AppData.getMessageList(this);
        if (mMessageList == null) {
            mMessageList = new MessageList();
        } else {
            for (int i = 0; i < mMessageList.size(); i++) {
                Message message = mMessageList.get(i);
                //Set extra info because they were removed before save messages.
                for (IChatUser user : mUsers) {
                    if (message.getUser().getId().equals(user.getId())) {
                        message.getUser().setIcon(user.getIcon());
                    }
                }
                if (!message.isDateCell() && message.isRight()) {
                    message.hideIcon(true);

                }
                message.setStatusStyle(Message.Companion.getSTATUS_ICON_RIGHT_ONLY());
                message.setStatusIconFormatter(new MyMessageStatusFormatter(this));
                message.setStatus(MyMessageStatusFormatter.STATUS_DELIVERED);
                messages.add(message);
            }
        }
        MessageView messageView = mChatView.getMessageView();
        messageView.init(messages);
        messageView.setSelection(messageView.getCount() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUsers();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Save message
        mMessageList = new MessageList();
        mMessageList.setMessages(mChatView.getMessageView().getMessageList());
        AppData.putMessageList(this, mMessageList);
    }

    @VisibleForTesting
    public ArrayList<User> getUsers() {
        return mUsers;
    }


    public void setReplyDelay(int replyDelay) {
        mReplyDelay = replyDelay;
    }

    private void showDialog() {
        final String[] items = {
                getString(R.string.send_picture),
                getString(R.string.clear_messages)
        };

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.options))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position) {
                            case 0 :
                                openGallery();
                                break;
                            case 1:
                                mChatView.getMessageView().removeAll();
                                break;
                        }
                    }
                })
                .show();
    }
}
