package hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseViewHolder;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.ChatInfo;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 19..
 */

public class Tab2RecyclerviewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<ChatInfo> list = new ArrayList<>();
    private Context context;
    private Tab2Fragment tab2Fragment;

    public Tab2RecyclerviewAdapter(Context context) {
        this.context = context;
    }

    public List<ChatInfo> getList() {
        return list;
    }

    public void setTab2Fragment(Tab2Fragment tab2Fragment) {
        this.tab2Fragment = tab2Fragment;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Tab2ViewHolder(parent, R.layout.recycler_item_chatroom, context,this);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(context, list.get(position), position);
    }

    public Tab2Fragment getTab2Fragment() {
        return tab2Fragment;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(ChatInfo chatInfo) {
        list.add(chatInfo);
    }

    public void addItemToZeoro(ChatInfo chatInfo) {
        list.add(0, chatInfo);
    }


    public void removeItem(int position) {
        list.remove(position);
    }

    public void removeItemChatId(String ChatId) {

        for (int i = 0; i < list.size(); i++) {

            if (ChatId.equals(list.get(i).getChatId()))
                removeItem(i);

        }

    }


    public int findChatId(ChatInfo chatInfo) {


        for (int i = 0; i < list.size(); i++) {

            if (chatInfo.equals(list.get(i)))
                return i;

        }

        return -1;
    }


}
