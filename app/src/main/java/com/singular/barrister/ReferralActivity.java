package com.singular.barrister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class ReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.menu_love_app));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_love_app));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
