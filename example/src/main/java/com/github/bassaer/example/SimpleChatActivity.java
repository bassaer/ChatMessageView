package com.github.bassaer.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.MessageView;

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
                    .setText(me.getName() + " " + i)
                    .setRight(true)
                    .build();
            Message message2 = new Message.Builder()
                    .setUser(you)
                    .setText(you.getName() + " " + i)
                    .setRight(false)
                    .build();
            messages.add(message1);
            messages.add(message2);
        }

        MessageView messageView = findViewById(R.id.message_view);
        messageView.init(messages);

    }
}
