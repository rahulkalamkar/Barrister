package com.singular.barrister.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.singular.barrister.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LandingScreen extends AppCompatActivity implements View.OnClickListener {
    private Button signInButton;
    TextView createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        createAccountButton = (TextView) findViewById(R.id.button_create_account);
        signInButton = (Button) findViewById(R.id.button_sign_in);
        signInButton.setTransformationMethod(null);
        createAccountButton.setTransformationMethod(null);
        signInButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_create_account:
                Intent intent = new Intent(LandingScreen.this, CreateAccount.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_sign_in:
                Intent intent1 = new Intent(LandingScreen.this, SignInAccount.class);
                startActivity(intent1);
                finish();
                break;
        }

    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}
