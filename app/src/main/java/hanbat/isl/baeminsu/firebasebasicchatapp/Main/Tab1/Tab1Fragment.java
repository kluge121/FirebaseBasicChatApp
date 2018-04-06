package hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab1;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.LoginUserInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Main.MainActivity;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.InviteListUser;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.ProfileEdit.ProfileEdit;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 6..
 */

public class Tab1Fragment extends Fragment implements MainActivity.OnCallbackInviteListener {


    private RecyclerView recyclerView;
    private CircleImageView myProfile;
    private TextView name;
    private TextView message;
    private TextView tvFriendCount;
    private Tab1RecyclerviewAdapter adapter;
    private ConstraintLayout myLayout;
    private ArrayList<User> arrayList;
    private int count;


    public static Tab1Fragment newInstance() {
        return new Tab1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab1, container, false);

        myProfile = v.findViewById(R.id.my_friend_profile);
        name = v.findViewById(R.id.my_friend_name);
        message = v.findViewById(R.id.my_friend_message);
        tvFriendCount = v.findViewById(R.id.tab1_friend_count);
        myLayout = v.findViewById(R.id.my_friend_layout);

        myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileEdit.class);
                startActivity(intent);

            }
        });

        if (LoginUserInfo.checkProfile())
            Glide.with(getContext()).load(LoginUserInfo.getLoginUserInfo().getProfileUrl()).into(myProfile);
        name.setText(LoginUserInfo.getLoginUserInfo().getName());
        message.setText(LoginUserInfo.getLoginUserInfo().getMessage());

        adapter = new Tab1RecyclerviewAdapter(getContext());
        recyclerView = v.findViewById(R.id.tab1_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        getFriendList();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getContext()).load(LoginUserInfo.getLoginUserInfo().getProfileUrl()).into(myProfile);
        name.setText(LoginUserInfo.getLoginUserInfo().getName());
        message.setText(LoginUserInfo.getLoginUserInfo().getMessage());
    }

    void getFriendList() {
        count = 0;
        User loginUser = LoginUserInfo.getLoginUserInfo();
        CollectionReference friendRef = FirebaseFirestore.getInstance().collection("users").document(loginUser.getEmail())
                .collection("friend");

        friendRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    final List<DocumentSnapshot> list = task.getResult().getDocuments();
                    tvFriendCount.setText("친구 " + list.size());
                    arrayList = new ArrayList<User>();

                    for (DocumentSnapshot snapshot : list) {

                        DocumentReference dataGetRef = (DocumentReference) snapshot.get("user");
                        dataGetRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot snapshot) {
                                            arrayList.add(snapshot.toObject(User.class));
                                            count++;
                                            if (count == list.size()) {
                                                adapter.setList(arrayList);
                                                adapter.notifyDataSetChanged();
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

    @Override
    public ArrayList<InviteListUser> getFridList() {
        ArrayList<User> list = adapter.getList();
        ArrayList<InviteListUser> inputList = new ArrayList<>();

        for (User user : list) {
            inputList.add(InviteListUser.userToInviteSelectUser(user));
        }

        return inputList;
    }
}


























