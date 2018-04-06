package hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab3;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hanbat.isl.baeminsu.firebasebasicchatapp.R;

/**
 * Created by baeminsu on 2018. 1. 6..
 */

public class Tab3Fragment extends Fragment {

    RecyclerView recyclerView;

    public static Tab3Fragment newInstance() {
        return new Tab3Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab3, container, false);
        recyclerView = v.findViewById(R.id.tab3_recyclerview);
        return v;
    }
}

