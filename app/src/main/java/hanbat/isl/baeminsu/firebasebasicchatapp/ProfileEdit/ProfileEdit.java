package hanbat.isl.baeminsu.firebasebasicchatapp.ProfileEdit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseActivity;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.GalleryPhotoGet;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.LoginUserInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.ProfilePhoto.ProfilePhotoView;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

import static hanbat.isl.baeminsu.firebasebasicchatapp.Common.GalleryPhotoGet.PICK_FROM_ALBUM;

public class ProfileEdit extends BaseActivity {

    private CircleImageView profile;
    private TextView name;
    private TextView message;
    private Button nameBtn;
    private Button messageBtn;
    private GalleryPhotoGet galleryPhotoGet;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();

    private AlertDialog.Builder dialogBuilder;

    final int CHANGE_DEFAULT_PROFILE = 0;
    final int CHANGE_ALBUM_PROFILE = 1;
    final int SHOW_PROFILE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        setToolbar(R.id.profile_edit_toolbar, true);
        initWidget();


        galleryPhotoGet = new GalleryPhotoGet(this, this);


    }


    void initWidget() {
        profile = findViewById(R.id.profile_edit_profile);
        name = findViewById(R.id.propfile_edit_name);
        message = findViewById(R.id.profile_edit_mesasge);
        nameBtn = findViewById(R.id.profile_edit_name_btn);
        messageBtn = findViewById(R.id.profile_edit_mesasge_btn);

        User logUser = LoginUserInfo.getLoginUserInfo();
        Glide.with(ProfileEdit.this).load(logUser.getProfileUrl()).into(profile);
        name.setText(logUser.getName());
        message.setText(logUser.getMessage());

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileClickDialog();
            }
        });
        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameClickDialog();
            }
        });
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messsageClickDialog();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case PICK_FROM_ALBUM:
                //이미지 경로, 이름
                if (data == null) break;
                Uri selectImageUri = data.getData();
                String imgUpLoadPath;

                String imageName = galleryPhotoGet.getImageNameToUri(data.getData());
                Bitmap resized = galleryPhotoGet.makeBitmap(selectImageUri);
                imgUpLoadPath = galleryPhotoGet.saveBitmapToJpeg(
                        resized, "resizeTmp", imageName);

                changeProfile(imgUpLoadPath, resized);

                break;
        }
    }


    // 프로필 사진 변경
    void changeProfile(String imgUpLoadPath, final Bitmap resized) {

        if (imgUpLoadPath != null) {

            final Uri file = Uri.fromFile(new File(imgUpLoadPath));
            StorageReference profileImgRef = storageRef.child("profile_image/" + loginUser.getEmail());
            UploadTask uploadTask = profileImgRef.putFile(file);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri profile_URI = taskSnapshot.getDownloadUrl();

                    loginUser.updateProfile(new UserProfileChangeRequest.Builder()
                            .setPhotoUri(profile_URI).build());

                    db.collection("users").document(loginUser.getEmail())
                            .update("profileUrl", profile_URI.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                profile.setImageBitmap(resized);
                                Toast.makeText(ProfileEdit.this, "프로필사진이 변경되었습니다", Toast.LENGTH_SHORT).show();
                                LoginUserInfo.getLoginUserInfo().setProfileUrl(profile_URI.toString());
                            }
                        }
                    });


                }
            });
        }

    }

    void changedefaultProfile() {

        loginUser.updateProfile(new UserProfileChangeRequest.Builder()
                .setPhotoUri(null).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    db.collection("users").document(loginUser.getEmail())
                            .update("profileUrl", null)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        StorageReference profileImgRef = storageRef.child("profile_image/" + loginUser.getEmail());
                                        profileImgRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    profile.setImageResource(R.drawable.defalut_profile);
                                                    Toast.makeText(ProfileEdit.this, "프로필 사진이 변경되었습니다", Toast.LENGTH_SHORT).show();
                                                    LoginUserInfo.getLoginUserInfo().setProfileUrl("");
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

    void profileClickDialog() {

        final CharSequence[] items = {"기본이미지로 변경", "앨범에서 사진선택", "프로필사진 보기"};
        dialogBuilder = new AlertDialog.Builder(ProfileEdit.this);
        dialogBuilder.setTitle("프로필사진");
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case CHANGE_DEFAULT_PROFILE: //
                        changedefaultProfile();
                        break;
                    case CHANGE_ALBUM_PROFILE:
                        galleryPhotoGet.checkPhotoPermission();
                        break;
                    case SHOW_PROFILE:
                        Intent intent = new Intent(ProfileEdit.this, ProfilePhotoView.class);
                        intent.putExtra("url", loginUser.getPhotoUrl());
                        startActivity(intent);
                }
            }
        }).show();
    }

    // 이름 변경
    void nameClickDialog() {
        final EditText editUserNickname = new EditText(ProfileEdit.this);
        AlertDialog.Builder alertBuilder =
                new AlertDialog.Builder(ProfileEdit.this)
                        .setTitle("닉네임 변경")
                        .setMessage("변경할 닉네임을 입력해주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String name = editUserNickname.getText().toString();
                                if (name.length() > 0)
                                    changeName(name, dialogInterface);
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        alertBuilder.setView(editUserNickname);
        alertBuilder.show();

    }

    void changeName(String changName, DialogInterface dialogInterface) {


        UserProfileChangeRequest profileRequest =
                new UserProfileChangeRequest.Builder()
                        .setDisplayName(changName).build();

        loginUser.updateProfile(profileRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String changedNickname = loginUser.getDisplayName();
                            FirebaseFirestore.getInstance()
                                    .collection("users").document(loginUser.getUid())
                                    .update("name", changedNickname);

                            name.setText(changedNickname);
                            LoginUserInfo.getLoginUserInfo().setName(changedNickname);
                        } else {

                        }
                    }
                });
        dialogInterface.dismiss();
    }

    //상태 메세지 변경
    void messsageClickDialog() {
        final EditText editUserMessage = new EditText(ProfileEdit.this);
        AlertDialog.Builder alertBuilder =
                new AlertDialog.Builder(ProfileEdit.this)
                        .setTitle("상태메세지 변경")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String message = editUserMessage.getText().toString();

                                changeMessage(message, dialogInterface);
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertBuilder.setView(editUserMessage);
        alertBuilder.show();

    }

    void changeMessage(String changName, DialogInterface dialogInterface) {


        UserProfileChangeRequest profileRequest =
                new UserProfileChangeRequest.Builder()
                        .setDisplayName(changName).build();

        loginUser.updateProfile(profileRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String changedMessage = loginUser.getDisplayName();
                            FirebaseFirestore.getInstance()
                                    .collection("users").document(loginUser.getEmail())
                                    .update("message", changedMessage);

                            message.setText(changedMessage);
                            LoginUserInfo.getLoginUserInfo().setMessage(changedMessage);
                        } else {

                        }
                    }
                });
        dialogInterface.dismiss();
    }

}
