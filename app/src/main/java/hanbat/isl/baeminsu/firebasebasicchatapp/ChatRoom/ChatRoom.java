package hanbat.isl.baeminsu.firebasebasicchatapp.ChatRoom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseActivity;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.LoginUserInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab2.Tab2Fragment;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.ChatInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.Message;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.PhotoMessage;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.TextMessage;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.UserRef;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

public class ChatRoom extends BaseActivity {


    private ListenerRegistration messageListener;


    private String roomId;
    private EditText contentEdit;
    private Button sendBtn;
    private String chat_id;

    private FirebaseFirestore db;
    private CollectionReference colChatRef;
    private DocumentReference docChatRef;
    private FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();

    private CollectionReference chatMemberRef;
    private DocumentReference newChatMessageRef;
    private CollectionReference allChatMessageRef;
    private DocumentReference userRef;

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private LinearLayoutManager linearLayoutManager;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        db = FirebaseFirestore.getInstance();
        chat_id = getIntent().getStringExtra("chatId");
        initWidget();
        if (chat_id != null) {
            docChatRef = db.collection("users").document(LoginUserInfo.getLoginUserInfo().getEmail())
                    .collection("chat").document(chat_id);
            chatMemberRef = db.collection("chat_member").document(chat_id).collection("members_ref");
            settingTotalUnReadCount();
            Tab2Fragment.lastJoinChatId = chat_id;


        }


    }

    void initWidget() {
        setToolbar(R.id.chat_room_toolbar, true);
        contentEdit = findViewById(R.id.chat_room_edit);
        sendBtn = findViewById(R.id.chat_room_send);
        recyclerView = findViewById(R.id.chat_room_recycler);
        adapter = new MessageAdapter(this);


        if (contentEdit.getText().toString().length() == 0) {
            sendBtn.setEnabled(false);
        } else {
            sendBtn.setEnabled(true);
        }


        contentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (contentEdit.getText().toString().length() == 0) {
                    sendBtn.setEnabled(false);
                } else {
                    sendBtn.setEnabled(true);
                }

            }
        });

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        if (chat_id != null) {
            allChatMessageRef = db.collection("chat_message").document(chat_id).collection("message");
            settingTitleToolbar();

        } else {
            colChatRef = db.collection("users").document(LoginUserInfo.getLoginUserInfo().getEmail())
                    .collection("chat");
        }


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chat_id == null) {
                    createRoom();
                } else {
                    sendMessage();
                }

            }
        });

    }

    private void sendMessage() {

        // 메세지 키 새성
        newChatMessageRef = db.collection("chat_message").document(chat_id).collection("message").document(dateFormat.format(new Date()));
        chatMemberRef = db.collection("chat_member").document(chat_id).collection("members_ref");

        String messageId = newChatMessageRef.getId();

        final String messageText = contentEdit.getText().toString();

        if (messageText.isEmpty())
            return;

        final TextMessage textMessage = new TextMessage();
        textMessage.setMessageText(messageText);
        textMessage.setMessageDate(new Date());
        textMessage.setChatId(chat_id);
        textMessage.setMessageId(messageId);
        textMessage.setMessageType(Message.MessageType.TEXT);
        textMessage.setReadUserList(Arrays.asList(LoginUserInfo.getLoginUserInfo().getEmail()));

        UserRef tmpUserRef = new UserRef();
        tmpUserRef.setEmail(LoginUserInfo.getLoginUserInfo().getEmail());
        tmpUserRef.setUser(LoginUserInfo.getLoginUserRef());
        tmpUserRef.setName(LoginUserInfo.getLoginUserInfo().getName());
        textMessage.setMessageUser(tmpUserRef);


        ArrayList<String> emails = getIntent().getStringArrayListExtra("emails");
        if (emails != null)
            textMessage.setUnReadCount(emails.size() - 1);
        contentEdit.setText("");
        chatMemberRef = db.collection("chat_member").document(chat_id).collection("members_ref");

        chatMemberRef.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot documentSnapshots) {
                        long memberCount = documentSnapshots.size();
                        textMessage.setUnReadCount((int) (memberCount - 1));
                        newChatMessageRef.set(textMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Iterator<DocumentSnapshot> memberIterator = documentSnapshots.getDocuments().iterator();
                                while (memberIterator.hasNext()) {
                                    final String email = (String) memberIterator.next().get("email");

                                    userRef = db.collection("users").document(email)
                                            .collection("chat").document(chat_id);

                                    userRef.update("lastMessage", messageText);

                                    if (!email.equals(LoginUserInfo.getLoginUserInfo().getEmail())) {

                                        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot snapshot) {
                                                long totalUnReadCount = (long) snapshot.get("totalUnReadCount");
                                                DocumentReference tmpUserRef = db.collection("users").document(email)
                                                        .collection("chat").document(chat_id);
                                                tmpUserRef.update("totalUnReadCount", totalUnReadCount + 1);
                                                tmpUserRef.update("createDate", new Date());
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });

    }

    private boolean isSentFlag;
    private int count = 0;

    private void createRoom() {

        sendBtn.setEnabled(false);
        String email = getIntent().getStringExtra("email");
        ArrayList<String> emails;


        if (email != null) {
            // 1:1대화 세팅
            emails = new ArrayList<String>();
            emails.add(email);
        } else {
            emails = getIntent().getStringArrayListExtra("emails");
        }


        final ChatInfo chatInfo = new ChatInfo();
        chatInfo.setCreateDate(new Date());
        chatInfo.setTotalUserCount(emails.size() + 1);


        final ArrayList<User> userArrayList = new ArrayList<>();
        final ArrayList<String> finalEmails = emails;
        finalEmails.add(LoginUserInfo.getLoginUserInfo().getEmail());


        colChatRef.add(chatInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                chat_id = documentReference.getId();
                chatInfo.setChatId(chat_id);
                chatMemberRef = db.collection("chat_member").document(chat_id).collection("members_ref");


                for (final String userEmail : finalEmails) {

                    userRef = db.collection("users").document(userEmail);


                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            userArrayList.add(snapshot.toObject(User.class));
                            count++;

                            if (finalEmails.size() == count) {


                                for (final User user : userArrayList) {

                                    userRef = db.collection("users").document(user.getEmail());
                                    //선택된 유저 레퍼런스 가져오기
                                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                            if (task.isSuccessful()) {
                                                UserRef member = new UserRef();
                                                member.setUser(task.getResult().getReference());
                                                member.setEmail(user.getEmail());
                                                // 선택된 유저 레퍼런스 채팅멤버에 추가
                                                DocumentReference addMemberRef = db.collection("chat_member").document(chat_id)
                                                        .collection("members_ref").document(member.getEmail());

                                                addMemberRef.set(member).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        DocumentReference tmpUserCharRef = db.collection("users").document(user.getEmail()).collection("chat")
                                                                .document(chat_id);

                                                        final boolean meFlag;
                                                        if (user.getEmail().equals(LoginUserInfo.getLoginUserInfo().getEmail()))
                                                            meFlag = true;
                                                        else
                                                            meFlag = false;

                                                        chatInfo.setTitle(makeTitle(userArrayList, user.getName()));
                                                        tmpUserCharRef.set(chatInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                if (meFlag)
                                                                    settingTitleToolbar();
                                                            }
                                                        });

                                                        if (!isSentFlag) {
                                                            addMessageListener();
                                                            sendMessage();
                                                            isSentFlag = true;
                                                        }

                                                    }
                                                });
                                            }
                                        }
                                    });

                                }

                            }
                        }
                    });

                }

                //TODO 아직 완벽하진 않음, 카운팅해서 마지막께 완료됐을때 인어블되게 해야함
                //TODO 임시 조치
                sendBtn.setEnabled(true);


            }
        });


    }

    String makeTitle(ArrayList<User> list, String name) {

        StringBuffer sb = new StringBuffer();
        int count = 0;


        for (User user : list) {
            if (user.getName().equals(name)) continue;
            sb.append(user.getName());
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        return sb.toString();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (chat_id != null)
            addMessageListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (messageListener != null) {
            messageListener.remove();

        }
        settingTotalUnReadCount();

    }

    void settingTotalUnReadCount() {
        docChatRef.update("totalUnReadCount", 0);
    }

    void settingTitleToolbar() {
        Log.e("채팅방이름", "채팅방");
        docChatRef = db.collection("users").document(LoginUserInfo.getLoginUserInfo().getEmail())
                .collection("chat").document(chat_id);
        docChatRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                String title = (String) snapshot.get("title");
                toolbar.setTitle(title);
            }
        });
    }


    void addMessageListener() {
        allChatMessageRef = db.collection("chat_message").document(chat_id).collection("message");
        messageListener = allChatMessageRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for (final DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    Message message = null;
                    switch (dc.getType()) {
                        case ADDED:

                            Log.e("리스너 체크", "애드");

                            if (dc.getDocument().get("messageType").equals("TEXT")) {
                                message = dc.getDocument().toObject(TextMessage.class);
                            } else if (dc.getDocument().get("MessageType").equals("PHOTO")) {
                                message = dc.getDocument().toObject(PhotoMessage.class);
                            }

                            final Message finalMessage = message;
                            db.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {


                                    if (!finalMessage.getReadUserList().contains(LoginUserInfo.getLoginUserInfo().getEmail())) {
                                        long readCount = (long) transaction.get(dc.getDocument().getReference()).get("unReadCount");
                                        List readUserList = (List) transaction.get(dc.getDocument().getReference()).get("readUserList");
                                        readCount -= 1;
                                        readUserList.add(LoginUserInfo.getLoginUserInfo().getEmail());

                                        transaction.update(dc.getDocument().getReference(), "unReadCount", readCount);
                                        transaction.update(dc.getDocument().getReference(), "readUserList", readUserList);
                                    }

                                    return null;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("왜실패했냐", e.toString());
                                }
                            });
                            settingTotalUnReadCount();
                            adapter.addMessage(message);
                            recyclerView.smoothScrollToPosition(adapter.getItemCount());

                            break;
                        case MODIFIED:
                            Log.e("리스너 체크", "모디파이");

                            if (dc.getDocument().get("messageType").equals("TEXT")) {
                                message = dc.getDocument().toObject(TextMessage.class);
                            } else if (dc.getDocument().get("MessageType").equals("PHOTO")) {
                                message = dc.getDocument().toObject(PhotoMessage.class);
                            }

                            final Message finalMessage1 = message;

                            db.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {


                                    if (!finalMessage1.getReadUserList().contains(LoginUserInfo.getLoginUserInfo().getEmail())) {
                                        long readCount = (long) transaction.get(dc.getDocument().getReference()).get("unReadCount");
                                        List readUserList = (List) transaction.get(dc.getDocument().getReference()).get("readUserList");
                                        readCount -= 1;
                                        readUserList.add(LoginUserInfo.getLoginUserInfo().getEmail());

                                        transaction.update(dc.getDocument().getReference(), "unReadCount", readCount);
                                        transaction.update(dc.getDocument().getReference(), "readUserList", readUserList);
                                    }

                                    return null;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("왜실패했냐", e.toString());
                                }
                            });

                            adapter.updateMessage(message);
                            settingTotalUnReadCount();


                            break;

                        case REMOVED:
                            Log.e("리스너 체크", "리무브");
                            break;

                    }
                }

            }
        });

    }
}

