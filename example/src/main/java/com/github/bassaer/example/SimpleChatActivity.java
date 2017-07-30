package com.github.bassaer.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.github.bassaer.chatmessageview.models.LeftMessage;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.MessageView;

import java.util.ArrayList;

/**
 * Simple chat UI
 * Created by nakayama on 2016/12/03.
 */
public class SimpleChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_chat);

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


        ArrayList<Message> messages = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Message message1 = new Message.Builder()
                    .setUser(me)
                    .setMessageText(me.getName() + " " + i)
                    .setRightMessage(true)
                    .build();
            Message message2 = new LeftMessage.Builder()
                    .setUser(you)
                    .setMessageText(you.getName() + " " + i)
                    .setRightMessage(false)
                    .build();
            messages.add(message1);
            messages.add(message2);
        }

        MessageView messageView = (MessageView)findViewById(R.id.message_view);
        messageView.init(messages);

    }
}
