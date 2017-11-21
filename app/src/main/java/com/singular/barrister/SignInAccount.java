package com.singular.barrister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignInAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_account);
        if (getActionBar() != null)
            getActionBar().setTitle(getResources().getString(R.string.sign_in));
        else
            getSupportActionBar().setTitle(getResources().getString(R.string.sign_in));
    }
}
