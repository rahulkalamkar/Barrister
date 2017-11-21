package com.singular.barrister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LandingScreen extends AppCompatActivity implements View.OnClickListener {
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        createAccountButton = (Button) findViewById(R.id.button_create_account);
        createAccountButton.setOnClickListener(this);
    }

    private void goToRegisterScreen() {
        Intent intent = new Intent(LandingScreen.this, CreateAccount.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_create_account) {
            goToRegisterScreen();
        }
    }
}
