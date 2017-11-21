package com.singular.barrister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);


        if(getActionBar()!=null)
            getActionBar().setTitle(getResources().getString(R.string.privacy_policy));
        else
            getSupportActionBar().setTitle(getResources().getString(R.string.privacy_policy));
    }
}
