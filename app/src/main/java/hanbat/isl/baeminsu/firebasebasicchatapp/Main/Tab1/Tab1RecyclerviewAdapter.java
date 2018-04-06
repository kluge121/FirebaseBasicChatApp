package hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 8..
 */

public class Tab1RecyclerviewAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private Context context;
    private ArrayList<User> list = new ArrayList<User>();

    Tab1RecyclerviewAdapter(Context context) {
        this.context = context;
    }

    void setList(ArrayList<User> list) {
        this.list = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new Tab1ViewHolder(parent, R.layout.recycler_item_friend, this);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(context, list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<User> getList() {
        return list;
    }
}
