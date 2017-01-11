package jp.bassaer.chatmessageview.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import jp.bassaer.chatmessageview.R;
import jp.bassaer.chatmessageview.models.Message;

/**
 * Chat view with edit view and send button
 * Created by nakayama on 2016/08/08.
 */
public class ChatView extends LinearLayout {
    private MessageView mMessageView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private FrameLayout mChatContainer;
    private InputMethodManager mInputMethodManager;
    private int mSendIconId = R.drawable.ic_action_send;
    private int mSendIconColor = ContextCompat.getColor(getContext(), R.color.lightBlue500);
    private boolean mAutoScroll = true;

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View layout = LayoutInflater.from(context).inflate(R.layout.chat_view, this);
        mMessageView = (MessageView) layout.findViewById(R.id.message_view);
        mInputText = (EditText) layout.findViewById(R.id.message_edit_text);
        mSendButton = (ImageButton) layout.findViewById(R.id.send_button);
        mChatContainer = (FrameLayout) layout.findViewById(R.id.chat_container);

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


        mMessageView.setFocusableInTouchMode(true);
        //if touched Chat screen
        mMessageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
            }
        });

        //if touched empty space
        mChatContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });


        mMessageView.setOnKeyboardAppearListener(new MessageView.OnKeyboardAppearListener() {
            @Override
            public void onKeyboardAppeared(boolean hasChanged) {
                //Appeared keyboard
                if (hasChanged) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Scroll to end
                            mMessageView.scrollToEnd();
                        }
                    }, 500);
                }
            }
        });

    }

    /**
     * Hide software keyboard
     */
    public void hideKeyboard() {
        //Hide keyboard
        mInputMethodManager.hideSoftInputFromWindow(
                mMessageView.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        //Move focus to background
        mMessageView.requestFocus();
    }

    public void setLeftBubbleColor(int color) {
        mMessageView.setLeftBubbleColor(color);
    }

    public void setRightBubbleColor(int color) {
        mMessageView.setRightBubbleColor(color);
    }


    /**
     * Set message to right side
     * @param message Sent message
     */
    public void send(Message message) {
        Object lastItem = mMessageView.getLastChatObject();
        if (lastItem instanceof Message) {
            if (((Message) lastItem).getUser().getId() == message.getUser().getId()) {
                //If send same person, hide username and icon.
                message.setIconVisibility(false);
                message.setUsernameVisibility(false);
            }
        }
        mMessageView.setMessage(message);
        hideKeyboard();
        mMessageView.scrollToEnd();
    }

    /**
     * Set message to left side
     * @param message Received message
     */
    public void receive(Message message) {
        Object lastItem = mMessageView.getLastChatObject();
        if (lastItem instanceof Message) {
            if (((Message) lastItem).getUser().getId() == message.getUser().getId()) {
                //If send same person, hide username and icon.
                message.setIconVisibility(false);
                message.setUsernameVisibility(false);
            }
        }
        mMessageView.setMessage(message);
        if (mAutoScroll) {
            mMessageView.scrollToEnd();
        }
    }

    public void setInputText(String input) {
        mInputText.setText(input);
    }

    public String getInputText() {
        return mInputText.getText().toString();
    }

    public void setOnClickSendButtonListener(View.OnClickListener listener) {
        mSendButton.setOnClickListener(listener);
    }

    public void setBackgroundColor(int color) {
        mMessageView.setBackgroundColor(color);
        mChatContainer.setBackgroundColor(color);
    }

    public void setSendButtonColor(int color) {
        mSendIconColor = color;
        ColorStateList colorStateList = ColorStateList.valueOf(mSendIconColor);
        Drawable icon = ContextCompat.getDrawable(getContext(), mSendIconId);
        DrawableCompat.setTintList(icon, colorStateList);
        mSendButton.setImageDrawable(icon);
    }

    public void setSendIcon(int resId) {
        mSendIconId = resId;
        setSendButtonColor(mSendIconColor);
    }

    public void setInputTextHint(String hint) {
        mInputText.setHint(hint);
    }

    public void setUsernameTextColor(int color) {
        mMessageView.setUsernameTextColor(color);
    }

    public void setSendTimeTextColor(int color) {
        mMessageView.setSendTimeTextColor(color);
    }

    public void setDateSeparatorColor(int color) {
        mMessageView.setDateSeparatorTextColor(color);
    }

    public void setRightMessageTextColor(int color) {
        mMessageView.setRightMessageTextColor(color);
    }

    public void setLeftMessageTextColor(int color) {
        mMessageView.setLeftMessageTextColor(color);
    }

    /**
     * Auto Scroll when message received.
     * @param enable Whether auto scroll is enable or not
     */
    public void setAutoScroll(boolean enable) {
        mAutoScroll = enable;
    }

    public void setMessageMarginTop(int px) {
        mMessageView.setMessageMarginTop(px);
    }

    public void setMessageMarginBottom(int px) {
        mMessageView.setMessageMarginBottom(px);
    }


}
