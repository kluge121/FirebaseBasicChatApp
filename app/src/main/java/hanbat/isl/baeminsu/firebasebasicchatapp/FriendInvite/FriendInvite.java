package hanbat.isl.baeminsu.firebasebasicchatapp.FriendInvite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

import hanbat.isl.baeminsu.firebasebasicchatapp.ChatRoom.ChatRoom;
import hanbat.isl.baeminsu.firebasebasicchatapp.Common.BaseActivity;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.InviteListUser;
import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;
import hanbat.isl.baeminsu.firebasebasicchatapp.R;

public class FriendInvite extends BaseActivity {

    RecyclerView friendRecyclerView;
    RecyclerView selectRecyclerView;
    FriendInviteAdpater inviteAdpater;
    SelectAdapter selectAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_invite);

        initWidget();


    }

    void initWidget() {
        setToolbar(R.id.invite_toolbar, true);
        toolbar.setTitle("친구 초대");
        ArrayList<InviteListUser> arrayList = (ArrayList<InviteListUser>) getIntent().getSerializableExtra("list");

        friendRecyclerView = findViewById(R.id.invite_friend_recyclerview);
        inviteAdpater = new FriendInviteAdpater(this, arrayList);
        friendRecyclerView.setAdapter(inviteAdpater);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        selectRecyclerView = findViewById(R.id.invite_select_recyclerview);
        selectAdapter = new SelectAdapter(this);
        selectRecyclerView.setAdapter(selectAdapter);
        selectRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        inviteAdpater.setSelectAdapter(selectAdapter);
        selectAdapter.setInviteAdpater(inviteAdpater);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.invite_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.invite_confirm:
                if (selectAdapter.getArrayList().size() > 0) {
                    ArrayList<String> inviteUserEmail = new ArrayList<String>();

                    for (InviteListUser user : selectAdapter.getArrayList()) {
                        inviteUserEmail.add(user.getEmail());
                    }

                    Intent intent = new Intent(FriendInvite.this, ChatRoom.class);
                    intent.putStringArrayListExtra("emails", inviteUserEmail);
                    startActivity(intent);
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);

    }

    protected void selectLayoutShow() {

        selectRecyclerView.animate().alpha(1.0f).setDuration(1000);
        selectRecyclerView.setVisibility(View.VISIBLE);

    }

    protected void selectLayoutHide() {
        selectRecyclerView.animate().alpha(0.0f).setDuration(1000);
        selectRecyclerView.setVisibility(View.GONE);
    }


}
