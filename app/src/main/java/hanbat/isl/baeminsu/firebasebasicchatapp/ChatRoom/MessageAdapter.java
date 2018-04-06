package hanbat.isl.baeminsu.firebasebasicchatapp.ChatRoom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.LoginUserInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.Message;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.PhotoMessage;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.TextMessage;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 20..
 */

public class MessageAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private final int MY_SENT_TEXT_MESSAGE = 10;
    private final int MY_SENT_PHOTO_MESSAGE = 20;

    private final int OTHER_SENT_TEXT_MESSAGE = 11;
    private final int OTHER_SENT_PHOTO_MESSAGE = 21;


    private ArrayList<Message> messageList = new ArrayList<>();
    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case MY_SENT_TEXT_MESSAGE:
                return new SentTextMessageViewHolder(parent, R.layout.recycler_item_sent_message);

            case OTHER_SENT_TEXT_MESSAGE:
                return new ReceiveTextMessageViewHolder(parent, R.layout.recycler_item_receive_message);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        Message item = messageList.get(position);
        TextMessage textMessage;
        PhotoMessage photoMessage;

        if (item.getMessageType() == Message.MessageType.TEXT) {
            textMessage = (TextMessage) item;
            holder.bindView(context, textMessage, position);

        } else if (item.getMessageType() == Message.MessageType.PHOTO) {
            photoMessage = (PhotoMessage) item;
            holder.bindView(context, photoMessage, position);
        }


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int returnType = 0;

        if (!messageList.get(position).getMessageUser().getEmail().equals(LoginUserInfo.getLoginUserInfo().getEmail()))
            returnType++;

        if (messageList.get(position).getMessageType() == Message.MessageType.TEXT)
            returnType += 10;
        else
            returnType += 20;

        return returnType;
    }

    //TODO 메신저 내용 캐시를 불러오는 식으로 변경하고
    //TODO 실시간으로 일단 보여주고 -> 캐싱
    //TODO 만약에 앱이 꺼진 상태에서 메신저가 왔다면? 그 모디파이로 보여준 뒤 해당 내용 그대로 캐싱

    public void addMessage(Message message) {
        messageList.add(message);
        notifyDataSetChanged();
    }

    public void updateMessage(Message message) {

        int position = getItemPostition(message.getMessageId());
        if (position < 0) return;
        messageList.set(position, message);
        notifyItemChanged(position);

    }

    public int getItemPostition(String messageId) {

        int position = 0;
        for (Message loopMessage : messageList) {
            if (loopMessage.getMessageId().equals(messageId))
                return position;
            position++;
        }
        return -1;
    }


}
