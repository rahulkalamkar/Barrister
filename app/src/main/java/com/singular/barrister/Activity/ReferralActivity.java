package com.singular.barrister.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;

public class ReferralActivity extends AppCompatActivity {

    TextView txtReferralCode;
    LinearLayout btn;

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

        txtReferralCode = (TextView) findViewById(R.id.textViewReferalCode);
        String referralCode = new UserPreferance(getApplicationContext()).getReferral_code();
        txtReferralCode.setText(referralCode);

        btn = (LinearLayout) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareData();
            }
        });
    }

    public void shareData() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download this amazing app and add my referral code" + new UserPreferance(getApplicationContext()).getReferral_code() + "to get free subscription");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
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
