package hanbat.isl.baeminsu.firebasebasicchatapp.Main;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseActivity;
import hanbat.isl.baeminsu.firebasebasicchatapp.FriendInvite.FriendInvite;
import hanbat.isl.baeminsu.firebasebasicchatapp.FriendSearch.FriendSearch;
import hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab1.Tab1Fragment;
import hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab1.Tab1RecyclerviewAdapter;
import hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab2.Tab2Fragment;
import hanbat.isl.baeminsu.firebasebasicchatapp.Main.Tab3.Tab3Fragment;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.InviteListUser;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private OnCallbackInviteListener onCallbackInviteListener;

    public interface OnCallbackInviteListener {
        ArrayList<InviteListUser> getFridList();
    }

    final int FRIEND_TAB = 0;
    final int DIALOG_TAB = 1;

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fab;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof OnCallbackInviteListener) {
            onCallbackInviteListener = (OnCallbackInviteListener) fragment;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.main_toolbar, false);
        toolbar.setTitle("Hand Messenge");
        initWidget();

    }

    void initWidget() {


        tabLayout = findViewById(R.id.main_tablayout);
        viewPager = findViewById(R.id.main_viewpager);
        fab = findViewById(R.id.main_fab);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(Tab1Fragment.newInstance());
        viewPagerAdapter.addFragment(Tab2Fragment.newInstance());
        viewPagerAdapter.addFragment(Tab3Fragment.newInstance());

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_fab:
                floatingClick();
                break;


        }
    }

    void floatingClick() {
        Intent intent;
        switch (tabLayout.getSelectedTabPosition()) {

            case FRIEND_TAB:
                intent = new Intent(MainActivity.this, FriendSearch.class);
                startActivity(intent);
                break;

            case DIALOG_TAB:
                intent = new Intent(MainActivity.this, FriendInvite.class);
                ArrayList arrayList = (ArrayList) onCallbackInviteListener.getFridList();
                intent.putExtra("list", arrayList);
                startActivity(intent);
                break;
        }
    }


}
