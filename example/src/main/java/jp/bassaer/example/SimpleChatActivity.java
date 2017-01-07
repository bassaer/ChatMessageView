package jp.bassaer.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.util.ArrayList;

import jp.bassaer.chatmessageview.models.LeftMessage;
import jp.bassaer.chatmessageview.models.Message;
import jp.bassaer.chatmessageview.views.MessageView;

/**
 * Simple chat UI
 * Created by nakayama on 2016/12/03.
 */
public class SimpleChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_chat);

        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        Bitmap icon2 = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);


        ArrayList<Message> messages = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Message message1 = new Message.Builder()
                    .setUserIcon(icon1)
                    .setUserName("Smith")
                    .setMessageText("Smith " + i)
                    .setRightMessage(true)
                    .build();
            Message message2 = new LeftMessage.Builder()
                    .setUserIcon(icon2)
                    .setUserName("Johnson")
                    .setMessageText("Johnson " + i)
                    .setRightMessage(false)
                    .build();
            messages.add(message1);
            messages.add(message2);
        }

        MessageView messageView = (MessageView)findViewById(R.id.message_view);
        messageView.init(messages);

    }
}
