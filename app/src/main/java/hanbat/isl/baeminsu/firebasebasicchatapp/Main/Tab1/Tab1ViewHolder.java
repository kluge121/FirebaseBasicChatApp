package hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.OtherProfile.OtherProfile;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 8..
 */

public class Tab1ViewHolder extends BaseViewHolder<User> {

    private CircleImageView profile;
    private TextView name;
    private TextView message;
    private ConstraintLayout layout;
    private Tab1RecyclerviewAdapter adapter;


    Tab1ViewHolder(ViewGroup parent, int layoutId, Tab1RecyclerviewAdapter adapter) {
        super(parent, layoutId);

        this.adapter = adapter;
        profile = itemView.findViewById(R.id.item_friend_profile);
        name = itemView.findViewById(R.id.item_friend_name);
        message = itemView.findViewById(R.id.item_friend_message);
        layout = itemView.findViewById(R.id.item_friend_layout);

    }

    @Override
    public void bindView(final Context context, final User model, int position) {

        if (model.getProfileUrl() != null)
            Glide.with(context).load(model.getProfileUrl()).into(profile);
        name.setText(model.getName());
        message.setText(model.getMessage());


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherProfile.class);
                intent.putExtra("user", model);
                context.startActivity(intent);

            }
        });


    }


}
