package hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hanbat.isl.baeminsu.firebasebasicchatapp.ChatRoom.ChatRoom;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.LoginUserInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.ChatInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.Message;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.TextMessage;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 19..
 */

public class Tab2ViewHolder extends BaseViewHolder<ChatInfo> {


    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/DD\n hh:mm");
    private CircleImageView chatRoomProfile;
    private TextView chatRoomName;
    private TextView chatRoomLastMessage;
    private TextView chatRoomDate;
    private TextView chatRoomUnReadCountl;


    private Context context;
    private Tab2RecyclerviewAdapter adapter;


    Tab2ViewHolder(ViewGroup parent, int layoutId, Context context, Tab2RecyclerviewAdapter adapter) {
        super(parent, layoutId);
        chatRoomProfile = itemView.findViewById(R.id.item_chatroom_profile);
        chatRoomDate = itemView.findViewById(R.id.item_chatroom_date);
        chatRoomName = itemView.findViewById(R.id.item_chatroom_name);
        chatRoomLastMessage = itemView.findViewById(R.id.item_chatroom_last_message);
        chatRoomUnReadCountl = itemView.findViewById(R.id.item_chatroom_unread_count);

        this.context = context;
        this.adapter = adapter;


    }

    @Override
    public void bindView(final Context context, final ChatInfo model, int position) {


        chatRoomName.setText(model.getTitle());
        chatRoomLastMessage.setText(model.getLastMessage());
        chatRoomDate.setText(dateFormat.format(model.getCreateDate()));
        if (model.getTotalUnReadCount() == 0) {
            chatRoomUnReadCountl.setVisibility(View.GONE);
        } else {
            chatRoomUnReadCountl.setVisibility(View.VISIBLE);
            chatRoomUnReadCountl.setText(String.valueOf(model.getTotalUnReadCount()));
        }


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(context, ChatRoom.class);
                chatIntent.putExtra("chatId", model.getChatId());

                context.startActivity(chatIntent);

            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (adapter.getTab2Fragment() != null) {
                    adapter.getTab2Fragment().leaveChat(model);
                }
                return false;
            }
        });

    }


}
