package hanbat.isl.baeminsu.firebasebasicchatapp.FriendInvite;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.InviteListUser;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 16..
 */

public class SelectViewHolder extends BaseViewHolder<InviteListUser> {


    private SelectAdapter adapter;
    private CircleImageView profile;
    private TextView name;
    private FriendInviteAdpater inviteAdpater;

    SelectViewHolder(ViewGroup parent, int layoutId, SelectAdapter adapter, FriendInviteAdpater inviteAdpater) {
        super(parent, layoutId);
        this.adapter = adapter;
        this.inviteAdpater = inviteAdpater;


        profile = itemView.findViewById(R.id.select_friend_profile);
        name = itemView.findViewById(R.id.select_friend_name);
    }

    @Override
    public void bindView(final Context context, final InviteListUser model, final int position) {

        Glide.with(context).load(model.getProfileUrl()).into(profile);
        name.setText(model.getName());


//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                model.setCheck(false);
//                adapter.remove(model);
//                if (adapter.getItemCount() == 0) {
//                    adapter.layoutHide();
//                }
//
//                inviteAdpater.notifyDataSetChanged();
//                adapter.notifyDataSetChanged();
//                Log.e("체크",adapter.getItemCount()+"개");
//
//            }
//        });
    }


}












