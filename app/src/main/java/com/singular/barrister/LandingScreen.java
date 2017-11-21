package com.singular.barrister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LandingScreen extends AppCompatActivity implements View.OnClickListener {
    private Button createAccountButton,signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        createAccountButton = (Button) findViewById(R.id.button_create_account);
        signInButton=(Button)findViewById(R.id.button_sign_in);

        signInButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.button_create_account :
                Intent intent = new Intent(LandingScreen.this, CreateAccount.class);
                startActivity(intent);
                break;

            case R.id.button_sign_in :
                Intent  intent1=new Intent(LandingScreen.this,SignInAccount.class);
                startActivity(intent1);
                break;
        }

    }
}
