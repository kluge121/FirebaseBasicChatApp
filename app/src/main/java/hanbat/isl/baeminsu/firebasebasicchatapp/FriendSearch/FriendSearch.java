package hanbat.isl.baeminsu.firebasebasicchatapp.FriendSearch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseActivity;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.LoginUserInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.UserRef;
import hanbat.isl.baeminsu.firebasebasicchatapp.ProfilePhoto.ProfilePhotoView;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

public class FriendSearch extends BaseActivity implements View.OnClickListener {

    private SearchView searchView;
    private CircleImageView profile;
    private TextView name;
    private TextView email;
    private Button btn;
    private User user;
    private TextView notification;

    DocumentReference searchRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_search_view, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("친구 이메일 검색...");
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                friendSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        setToolbar(R.id.friend_search_toolbar, true);
        initWidget();


    }


    void initWidget() {
        profile = findViewById(R.id.friend_search_profile);
        name = findViewById(R.id.friend_search_name);
        email = findViewById(R.id.friend_search_email);
        btn = findViewById(R.id.friend_search_add_btn);
        notification = findViewById(R.id.friend_search_notification);

        profile.setOnClickListener(this);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.friend_search_add_btn:
                addFriend(name.getText().toString());
                break;
            case R.id.friend_search_profile:
                if (user.getProfileUrl() != null) {
                    Intent intent = new Intent(FriendSearch.this, ProfilePhotoView.class);
                    startActivity(intent);
                }
                break;
        }

    }

    void friendSearch(String keywordEmail) {


        searchRef = db.collection("users").document(keywordEmail);

        searchRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        user = task.getResult().toObject(User.class);
                        if (LoginUserInfo.getLoginUserInfo().getEmail().equals(user.getEmail())) {
                            name.setText("자기 자신은 영원한 친구입니다 헿");
                            name.setVisibility(View.VISIBLE);
                            return;
                        }

                        Query query = db.collection("users").document(LoginUserInfo.getLoginUserInfo().getEmail()).
                                collection("friend").whereEqualTo("email", user.getEmail());

                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    if (task.getResult().toObjects(UserRef.class).size() > 0) {
                                        settingUserInfo(true);
                                    } else {
                                        settingUserInfo(false);
                                    }
                                }

                            }
                        });

                    } else {
                        name.setText("해당 유저를 찾을수 없습니다.");
                        name.setVisibility(View.VISIBLE);
                        return;
                    }


                } else {
                    Toast.makeText(FriendSearch.this, "네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void settingUserInfo(boolean alreadyCheck) {

        profile.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);


        if (alreadyCheck) {
            btn.setVisibility(View.INVISIBLE);
            notification.setVisibility(View.VISIBLE);
            notification.setText("이미 등록된 친구입니다.");
        } else {
            btn.setVisibility(View.VISIBLE);
            notification.setVisibility(View.INVISIBLE);
        }

        if (user.getProfileUrl() != null)
            Glide.with(FriendSearch.this).load(user.getProfileUrl()).into(profile);
        name.setText(user.getName());
        email.setText(user.getEmail());


    }

    void addFriend(String name) {


        DocumentReference myUserFriendRef = db.collection("users").document(LoginUserInfo.getLoginUserInfo().getEmail())
                .collection("friend").document(user.getEmail());

        UserRef userRef = new UserRef();
        userRef.setEmail(user.getEmail());
        userRef.setUser(searchRef);
        userRef.setName(name);
        myUserFriendRef.set(userRef).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FriendSearch.this, "친구추가 되었습니다.", Toast.LENGTH_SHORT).show();
                    btn.setVisibility(View.INVISIBLE);
                }
            }
        });


    }
}