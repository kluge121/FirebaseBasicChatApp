package hanbat.isl.baeminsu.firebasebasicchatapp.FriendInvite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.InviteListUser;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 16..
 */

public class FriendInviteAdpater extends RecyclerView.Adapter<BaseViewHolder> {


    private Context context;
    private ArrayList<InviteListUser> arrayList;
    private SelectAdapter selectAdapter;
    private FriendInvite rootActivity;

    public void setSelectAdapter(SelectAdapter selectAdapter) {
        this.selectAdapter = selectAdapter;
    }

    public FriendInviteAdpater(Context context, ArrayList<InviteListUser> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        rootActivity = (FriendInvite) context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendInviteViewHolder(parent, R.layout.recycle_item_invite_friend, this, selectAdapter);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(context, arrayList.get(position), position);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
