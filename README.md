#ChatMessageView

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ChatMessageView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5032)
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](https://github.com/bassaer/ChatMessageView/blob/master/LICENSE)

This library aims to provide a chat UI view for Android.  

<img src="https://github.com/bassaer/ChatMessageView/blob/master/screens.png" height="285dp">


##Feature

- You need to write just few code to create chat view.
- Auto date setting
- Easy to use for bot app

##Gradle

```
dependencies {
    compile 'jp.bassaer:chatmessageview:1.2.0'
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
MIT License

Copyright (c) 2016 Tsubasa Nakayama

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
