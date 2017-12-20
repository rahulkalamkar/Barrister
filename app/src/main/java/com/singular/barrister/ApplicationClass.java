package com.singular.barrister;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.accountkit.AccountKit;

/**
 * Created by rahulbabanaraokalamkar on 11/22/17.
 */

public class ApplicationClass extends MultiDexApplication {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //   AccountKit.initialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
