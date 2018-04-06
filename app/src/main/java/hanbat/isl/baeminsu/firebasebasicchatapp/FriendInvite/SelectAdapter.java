package hanbat.isl.baeminsu.firebasebasicchatapp.FriendInvite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.InviteListUser;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 16..
 */

public class SelectAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public SelectAdapter(Context context) {
        this.context = context;
        rootActivity = (FriendInvite) context;
    }

    public ArrayList<InviteListUser> getArrayList() {
        return arrayList;
    }

    private ArrayList<InviteListUser> arrayList = new ArrayList<InviteListUser>();
    private Context context;
    private FriendInviteAdpater inviteAdpater;
    private FriendInvite rootActivity;

    public void setInviteAdpater(FriendInviteAdpater inviteAdpater) {
        this.inviteAdpater = inviteAdpater;

    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SelectViewHolder(parent, R.layout.recycler_item_invite_select_friend, this, inviteAdpater);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(context, arrayList.get(position), position);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItem(InviteListUser user) {
        if (getItemCount() == 0)
            layoutShow();
        arrayList.add(user);
        notifyDataSetChanged();

    }

    public void remove(int position) {
        arrayList.remove(position);
        notifyDataSetChanged();
    }


    public void remove(InviteListUser user) {
        if (getItemCount() == 1)
            layoutHide();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getEmail().equals(user.getEmail())) {
                user.setCheck(false);
                remove(i);
                break;
            }
        }

    }

    public void layoutHide() {
        rootActivity.selectLayoutHide();
    }

    public void layoutShow() {
        rootActivity.selectLayoutShow();
    }


}
