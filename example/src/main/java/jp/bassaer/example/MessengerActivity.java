package jp.bassaer.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import jp.bassaer.chatmessageview.models.Message;
import jp.bassaer.chatmessageview.models.User;
import jp.bassaer.chatmessageview.utils.ChatBot;
import jp.bassaer.chatmessageview.views.ChatView;

/**
 * Simple chat example activity
 * Created by nakayama on 2016/12/03.
 */
public class MessengerActivity extends Activity {

    private ChatView mChatView;
    private MessageList mMessageList;
    private ArrayList<User> mUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        initUsers();

        mChatView = (ChatView) findViewById(R.id.chat_view);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan500));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUsers();
                //new message
                Message message = new Message.Builder()
                        .setUser(mUsers.get(0))
                        .setRightMessage(true)
                        .setMessageText(mChatView.getInputText())
                        .hideIcon(true)
                        .setSendTimeFormatter(new MyTimeFormatter())
                        .build();
                if (mUsers.get(0).getIcon() == null) {
                    Log.d(getClass().getName(), mUsers.get(0).getName() + "'s icon is null ");
                }
                //Set to chat view
                mChatView.send(message);
                //Add message list
                mMessageList.add(message);
                //Reset edit text
                mChatView.setInputText("");

                //Ignore hey
                if (!message.getMessageText().contains("hey")) {

                    //Receive message
                    final Message receivedMessage = new Message.Builder()
                            .setUser(mUsers.get(1))
                            .setRightMessage(false)
                            .setMessageText(ChatBot.talk(mUsers.get(0).getName(), message.getMessageText()))
                            .setSendTimeFormatter(new MyTimeFormatter())
                            .build();

                    // This is a demo bot
                    // Return within 3 seconds
                    int sendDelay = (new Random().nextInt(4) + 1) * 1000;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.receive(receivedMessage);
                            //Add message list
                            mMessageList.add(receivedMessage);
                        }
                    }, sendDelay);
                }
            }

        });

        //Load saved messages
        loadMessages();

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
        mMessageList = AppData.getMessageList(this);
        if (mMessageList == null) {
            mMessageList = new MessageList();
        } else {
            for (int i = 0; i < mMessageList.size(); i++) {
                Message message = mMessageList.get(i);
                //Set extra info because they were removed before save messages.
                for (User user : mUsers) {
                    if (message.getUser().getId() == user.getId()) {
                        message.getUser().setIcon(user.getIcon());
                    }
                }
                message.setSendTimeFormatter(new MyTimeFormatter());
                if (!message.isDateCell()) {
                    if (message.isRightMessage()) {
                        message.hideIcon(true);
                        mChatView.send(message);
                    } else {
                        mChatView.receive(message);
                    }

                }
            }
        }
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
        AppData.putMessageList(this, mMessageList);
    }

}
