package com.singular.barrister;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.accountkit.AccountKit;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.singular.barrister.Preferance.UserPreferance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;

/**
 * Created by rahulbabanaraokalamkar on 11/22/17.
 */

public class ApplicationClass extends MultiDexApplication {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Fabric.with(this, new Crashlytics());
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-2696210102764869~2529670089");
        fullScreenAds();
    }

    private InterstitialAd mInterstitialAd;
    private boolean isFirstTime = false;

    public void fullScreenAds() {
        isFirstTime = true;
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2696210102764869/1364762227");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adListener();
        startTimer();
    }

    public void adListener() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (isFirstTime && new UserPreferance(getApplicationContext()).isUserLoggedIn()) {
                    mInterstitialAd.show();
                }
                isFirstTime = false;
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                // Code to be executed when when the interstitial ad is closed.
            }
        });
    }

    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    private void stopTimer() {
        if (mTimer1 != null) {
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    private void startTimer() {
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded() && new UserPreferance(getApplicationContext()).isUserLoggedIn()) {
                            mInterstitialAd.show();
                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 180000);
    }
}
