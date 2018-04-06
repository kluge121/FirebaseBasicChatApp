package hanbat.isl.baeminsu.firebasebasicchatapp.ChatRoom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.TextMessage;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 20..
 */

public class SentTextMessageViewHolder extends BaseViewHolder<TextMessage> {

    private TextView messageBody;
    private TextView messageTime;
    private TextView unReadCount;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

    public SentTextMessageViewHolder(ViewGroup parent, int layoutId) {
        super(parent, layoutId);
        messageBody = itemView.findViewById(R.id.text_message_body);
        messageTime = itemView.findViewById(R.id.text_message_time);
        unReadCount = itemView.findViewById(R.id.text_message_unread_count);
    }

    @Override
    public void bindView(Context context, TextMessage model, int position) {

        messageBody.setText(model.getMessageText());
        messageTime.setText(dateFormat.format(model.getMessageDate()));

        if (model.getUnReadCount() > 0) {
            unReadCount.setVisibility(View.VISIBLE);
            unReadCount.setText(model.getUnReadCount() + "");
        } else {
            unReadCount.setVisibility(View.INVISIBLE);

        }


    }
}
