package jp.bassaer.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.Random;

import jp.bassaer.chatmessageview.models.Message;
import jp.bassaer.chatmessageview.utils.ChatBot;
import jp.bassaer.chatmessageview.views.ChatView;

/**
 * Simple chat example activity
 * Created by nakayama on 2016/12/03.
 */
public class MessengerActivity extends Activity {

    private ChatView mChatView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        //User icon
        final Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        final String myName = "Michael";

        final Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        final String yourName = "Emily";

        mChatView = (ChatView)findViewById(R.id.chat_view);

        //Set UI options
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

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new message
                Message message = new Message.Builder()
                        .setUserIcon(myIcon)
                        .setUserName(myName)
                        .setRightMessage(true)
                        .setMessageText(mChatView.getInputText())
                        .build();
                //Set to chat view
                mChatView.send(message);
                //Reset edit text
                mChatView.setInputText("");

                //Receive message
                final Message receivedMessage = new Message.Builder()
                        .setUserIcon(yourIcon)
                        .setUserName(yourName)
                        .setRightMessage(false)
                        .setMessageText(ChatBot.talk(message.getUserName(), message.getMessageText()))
                        .build();

                // This is a demo bot
                // Return within 3 seconds
                int sendDelay  = (new Random().nextInt(4) +1) * 1000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.receive(receivedMessage);
                    }
                }, sendDelay);
            }
        });
    }
}
