package hanbat.isl.baeminsu.firebasebasicchatapp.Common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by baeminsu on 2018. 1. 8..
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {


    public BaseViewHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false));
    }

    public abstract void bindView(final Context context, T model, int position);
}
