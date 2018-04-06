package hanbat.isl.baeminsu.firebasebasicchatapp.ProfilePhoto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseActivity;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

public class ProfilePhotoView extends BaseActivity {

    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo_view);

        setToolbar(R.id.profile_photo_toolbar, true);
        initWidget();

        String profileUri = getIntent().getStringExtra("url");

        if (profileUri != null)
            Glide.with(ProfilePhotoView.this).load(profileUri).into(photoView);

    }

    void initWidget() {
        photoView = findViewById(R.id.profile_photo_photo);
    }

}
