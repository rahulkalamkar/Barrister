package com.singular.barrister;

import android.app.Application;

import com.facebook.accountkit.AccountKit;

/**
 * Created by rahulbabanaraokalamkar on 11/22/17.
 */

public class ApplicationClass extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        AccountKit.initialize(getApplicationContext());
    }
}
