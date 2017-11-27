package com.singular.barrister.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (new UserPreferance(getApplicationContext()).isUserLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this,
                            HomeScreen.class));
                } else {
                    startActivity(new Intent(SplashActivity.this,
                            LandingScreen.class));
                }
                finish();
            }
        }, secondsDelayed * 1000);

    }
}
