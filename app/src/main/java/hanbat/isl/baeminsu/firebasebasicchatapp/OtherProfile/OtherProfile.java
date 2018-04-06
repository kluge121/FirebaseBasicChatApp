package hanbat.isl.baeminsu.firebasebasicchatapp.OtherProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import hanbat.isl.baeminsu.firebasebasicchatapp.ChatRoom.ChatRoom;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseActivity;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.ProfilePhoto.ProfilePhotoView;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 9..
 */

public class OtherProfile extends BaseActivity {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button dialogBtn;
    private TextView name;
    private CircleImageView profile;
    private TextView message;
    private User otherUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        setToolbar(R.id.other_profile_edit_toolbar, true);
        otherUser = (User) getIntent().getSerializableExtra("user");
        if (otherUser != null) {
            initWidget();
        }

    }

    void initWidget() {

        profile = findViewById(R.id.other_profile_edit_profile);
        name = findViewById(R.id.other_propfile_edit_name);
        message = findViewById(R.id.other_profile_edit_mesasge);
        dialogBtn = findViewById(R.id.other_profile_dialog);

        Glide.with(OtherProfile.this).load(otherUser.getProfileUrl()).into(profile);
        name.setText(otherUser.getName());
        message.setText(otherUser.getMessage());


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherUser.getProfileUrl() != null) {
                    Intent intent = new Intent(OtherProfile.this, ProfilePhotoView.class);
                    intent.putExtra("url", otherUser.getProfileUrl());
                    startActivity(intent);
                }
            }
        });

        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherProfile.this, ChatRoom.class);
                intent.putExtra("email", otherUser.getEmail());
                intent.putExtra("name", otherUser.getName());
                startActivity(intent);

            }
        });

    }


}
