package hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Iterator;
import java.util.List;

import hanbat.isl.baeminsu.firebasebasicchatapp.ChatRoom.ChatRoom;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.LoginUserInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.ChatInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.Message;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.Notification;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.TextMessage;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 6..
 */

public class Tab2Fragment extends Fragment {

    private ListenerRegistration chatRoomRegistration;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatRef;
    private Query chatGetQuery;
    private Tab2RecyclerviewAdapter adapter;
    private Notification notification;
    private Context context;
    public static String lastJoinChatId = "";
    private final int JOIN_ROOM_REQUEST_CODE = 140;

    public static Tab2Fragment newInstance() {
        return new Tab2Fragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String email = LoginUserInfo.getLoginUserInfo().getEmail();
        chatGetQuery = db.collection("users").document(email).collection("chat").orderBy("createDate");

        chatRef = db.collection("users").document(email).collection("chat");
        notification = new Notification(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        context = getContext();
        recyclerView = v.findViewById(R.id.tab2_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new Tab2RecyclerviewAdapter(getContext()));
        adapter.setTab2Fragment(this);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        chatRoomRegistration = chatGetQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {

                    ChatInfo tmpChatInfo = dc.getDocument().toObject(ChatInfo.class);
                    int position;
                    switch (dc.getType()) {

                        case ADDED:

                            if (tmpChatInfo.getChatId() == null) {
                                tmpChatInfo.setChatId(dc.getDocument().getId());
                            }

                            position = adapter.findChatId(tmpChatInfo);

                            if (position != -1) {
                                adapter.removeItem(position);
                            }
                            adapter.addItemToZeoro(tmpChatInfo);
//                            Log.e("도큐먼트리스너", "애드" + tmpChatInfo.getChatId());
                            break;

                        case MODIFIED:
//                            Log.e("도큐먼트리스너", "모디파이" + tmpChatInfo.getChatId());
                            position = adapter.findChatId(tmpChatInfo);


                            if (position == -1) {
                                adapter.addItem(dc.getDocument().toObject(ChatInfo.class));
                            } else {
                                adapter.getList().remove(position);
                                adapter.getList().add(0, dc.getDocument().toObject(ChatInfo.class));

                            }

                            //TODO 노티피케이션 처리
                            if (tmpChatInfo.getLastMessage() == null) return;
//                            if (!tmpChatInfo.getLastMessage().getUserEmail().equals(LoginUserInfo.getLoginUserInfo().getEmail())) {
//                            if (!tmpChatInfo.getChatId().equals(lastJoinChatId)) {
                            Intent chatIntent = new Intent(context, ChatRoom.class);
                            chatIntent.putExtra("chatId", tmpChatInfo.getChatId());
                            notification
                                    .setData(chatIntent)
                                    .setTitle("왓섭")
                                    .setText(tmpChatInfo.getLastMessage())
                                    .notification();
//                            }
//                            }

                            break;

                        case REMOVED:
//                            Log.e("도큐먼트리스너", "리무브" + tmpChatInfo.getChatId());
                            adapter.removeItemChatId(tmpChatInfo.getChatId());
                            break;
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });


    }

    public void leaveChat(final ChatInfo chatInfo) {
        Snackbar.make(getView(), "선택한 대화방을 나가시겠습니까?", Snackbar.LENGTH_LONG).setAction("예", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference removeChatRef = db.collection("users").document(LoginUserInfo.getLoginUserInfo().getEmail())
                        .collection("chat").document(chatInfo.getChatId());


                removeChatRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            DocumentReference removeChatMember = db.collection("chat_member").document(chatInfo.getChatId())
                                    .collection("members_ref").document(LoginUserInfo.getLoginUserInfo().getEmail());
                            removeChatMember.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        CollectionReference messageRef = db.collection("chat_message").document(chatInfo.getChatId())
                                                .collection("message");
                                        messageRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    Iterator<DocumentSnapshot> messageIterator = task.getResult().iterator();
                                                    String myEmail = LoginUserInfo.getLoginUserInfo().getEmail();

                                                    while (messageIterator.hasNext()) {
                                                        DocumentSnapshot docMessage = messageIterator.next();
                                                        Message currentMessage = docMessage.toObject(Message.class);

                                                        if (!currentMessage.getReadUserList().contains(myEmail)) {
                                                            docMessage.getReference().update("unReadCount", currentMessage.getUnReadCount() - 1);
                                                        }

                                                    }


                                                }
                                            }
                                        });

                                    }
                                }
                            });

                        }
                    }
                });


            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JOIN_ROOM_REQUEST_CODE) {
            lastJoinChatId = "";
        }
    }
}
