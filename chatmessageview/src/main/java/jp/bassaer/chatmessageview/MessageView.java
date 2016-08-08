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

    public MessageView(Context context, ArrayList<Message> messages) {
        super(context);
        init(messages);
    }


    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init(ArrayList<Message> list) {
        mMessageList = new ArrayList<>();
        setChoiceMode(ListView.CHOICE_MODE_NONE);

        for(int i=0; i < list.size(); i++){
            setMessage(list.get(i));
        }

        MessageAdapter adapter = new MessageAdapter(getContext(), 0, mMessageList);
        setDividerHeight(0);
        setAdapter(adapter);

    }

    private void setMessage(Message message){
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
