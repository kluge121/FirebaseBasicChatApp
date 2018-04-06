package hanbat.isl.baeminsu.firebasebasicchatapp.FriendInvite;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class FriendInviteViewHolder extends BaseViewHolder<InviteListUser> {


    private CircleImageView profile;
    private TextView name;
    private CheckBox checkBox;
    private SelectAdapter selectAdapter;
    private FriendInviteAdpater inviteAdpater;


    FriendInviteViewHolder(ViewGroup parent, int layoutId, FriendInviteAdpater inviteAdpater, SelectAdapter selectAdapter) {
        super(parent, layoutId);
        profile = itemView.findViewById(R.id.item_invite_profile);
        name = itemView.findViewById(R.id.item_invite_name);
        checkBox = itemView.findViewById(R.id.item_invite_checkbox);

        this.inviteAdpater = inviteAdpater;
        this.selectAdapter = selectAdapter;


    }

    @Override
    public void bindView(Context context, final InviteListUser model, int position) {

        Glide.with(context).load(model.getProfileUrl()).into(profile);
        name.setText(model.getName());
        checkBox.setChecked(model.isCheck());


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    selectAdapter.addItem(model);
                    model.setCheck(true);
                } else {
                    selectAdapter.remove(model);
                    model.setCheck(false);

                }

            }
        });

    }


}
