package hanbat.isl.baeminsu.firebasebasicchatapp.Common;

/**
 * Created by baeminsu on 2018. 1. 6..
 */

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import hanbat.isl.baeminsu.firebasebasicchatapp.R;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }


    public void setToolbar(int toolbarId, boolean isHomeIndicator) {
        toolbar = findViewById(toolbarId);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (isHomeIndicator) {
            setHomeAsUpIndicator();
        }
    }

    private void setHomeAsUpIndicator() {
        if (toolbar == null) return;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}