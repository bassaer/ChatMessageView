package jp.bassaer.chatmessageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nakayama on 2016/08/08.
 */
public class ChatView extends LinearLayout {
    private MessageView mMessageView;
    private EditText mInputText;
    private ImageButton mSendButton;

    private String mUserName;
    private Bitmap mUserIcon;


    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mMessageView = new MessageView(context);
        mInputText = (EditText)findViewById(R.id.message_edit_text);
        mSendButton = (ImageButton)findViewById(R.id.send_button);

        setUserName(context.getString(R.string.unknown));
        setUserIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.face_1));

        mSendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message.Builder()
                        .setRightMessage(true)
                        .setUserName(getUserName())
                        .setUserIcon(getUserIcon())
                        .setMessageText(mInputText.getText().toString())
                        .setDateCell(false)
                        .setCreatedAt(Calendar.getInstance())
                        .build();

                send(message);
                mInputText.setText("");
            }
        });

        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();

                //Cannot send when input is empty
                if (input.length() == 0) {
                    mSendButton.setEnabled(false);
                } else {
                    mSendButton.setEnabled(true);
                }
            }
        });
    }

    public void send(Message message) {
        mMessageView.setMessage(message);
    }

    public void receive(Message message) {
        mMessageView.setMessage(message);
    }

    public void setMessages(ArrayList<Message> list) {
        mMessageView.setMessageList(list);
    }

    public Bitmap getUserIcon() {
        return mUserIcon;
    }

    public void setUserIcon(Bitmap userIcon) {
        mUserIcon = userIcon;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
