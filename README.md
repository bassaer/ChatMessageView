#ChatMessageView

This library aims to provide an chat UI view for Android.  

<img src="https://github.com/bassaer/ChatMessageView/blob/master/screens.png" height="285dp">


##Feature

- You need to write just few code to create chat view.
- Auto date setting
- Easy to use for bot app

##Gradle

```
repositories {
    maven { url 'http://raw.github.com/bassaer/ChatMessageView/master/repository' }
}

dependencies {
    compile 'jp.bassaer.chatmessageview:chatmessageview:1.2.0'
}

```

##Usage

Layout file

Only MessageView

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="match_parent">

    <jp.bassaer.chatmessageview.MessageView
        android:id="@+id/message_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</LinearLayout>
```

ChatView has MessageView and text box.

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="match_parent">

    <jp.bassaer.chatmessageview.views.ChatView
        android:id="@+id/chat_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</LinearLayout>
```

Sample code

```

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
    mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.deepOrange500));
    mChatView.setLeftBubbleColor(Color.WHITE);
    mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray700));
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

```

##License

```
Copyright 2016 Tsubasa Nakayama

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
