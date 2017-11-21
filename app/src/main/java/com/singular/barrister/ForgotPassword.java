package com.singular.barrister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if(getActionBar()!=null)
            getActionBar().setTitle(getResources().getString(R.string.forgot_password_title));
        else
            getSupportActionBar().setTitle(getResources().getString(R.string.forgot_password_title));
    }
}
