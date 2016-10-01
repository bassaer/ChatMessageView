package jp.bassaer.chatmessageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nakayama on 2016/08/08.
 */
public class MessageView extends ListView {

    private List<Message> mMessageList;

    public MessageView(Context context) {
        super(context);
        init();
    }


    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init() {
        mMessageList = new ArrayList<>();
        setChoiceMode(ListView.CHOICE_MODE_NONE);

        MessageAdapter adapter = new MessageAdapter(getContext(), 0, mMessageList);
        setDividerHeight(0);
        setAdapter(adapter);

    }

    public void setMessage(Message message){
        if(mMessageList.size() == 0){
            setDateSeparator(message);
        }else{
            Message prevMessage = mMessageList.get(mMessageList.size() - 1);

            int diff = message.getCompareDate().compareTo(prevMessage.getCompareDate());
            if(diff != 0){
                setDateSeparator(message);
            }
        }
        mMessageList.add(message);
    }

    private void setDateSeparator(Message message) {
        Message dateSeparator = new Message();
        dateSeparator.setDateCell(true);
        dateSeparator.setDateSeparateText(message.getDateSeparateText());
        mMessageList.add(dateSeparator);
    }

    public void setMessageList(ArrayList<Message> list){
        mMessageList = new ArrayList<>(list);
    }

}
