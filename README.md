#ChatMessageView

This library aims to provide an chat UI view for Android.  
<img src="https://github.com/bassaer/ChatMessageView/blob/master/Screenshot.png" width="540px">

##Feature

- You need to write just few code to create chat view.
- Auto date setting
- Easy to user for bot app

##Gradle

```
repositories {
    maven { url 'http://raw.github.com/bassaer/ChatMessageView/master/repository' }
}

dependencies {
    compile 'jp.bassaer.chatmessageview:chatmessageview:1.0.0'
}

```

##Usage

Layoutfile

```xml:activity_main.xml
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

Sample code

```java:MainActivity.java

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), jp.bassaer.chatmessageview.R.drawable.face_1);

        Message message1 = new Message();
        message1.setUserIcon(icon1);
        message1.setUserName("Michael");
        message1.setMessageText("hey! how are you?");
        message1.setRightMessage(true);

        ArrayList<Message> messages = new ArrayList<>();

        messages.add(message1);

        MessageView messageView  = (MessageView) findViewById(R.id.message_view);

        messageView.init(messages);
    }
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
