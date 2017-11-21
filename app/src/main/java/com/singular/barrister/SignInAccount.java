package com.singular.barrister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignInAccount extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogin;
    private TextView txtCreateNewAccount,txtForgotPassword,txtPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_account);
        if (getActionBar() != null)
            getActionBar().setTitle(getResources().getString(R.string.sign_in));
        else
            getSupportActionBar().setTitle(getResources().getString(R.string.sign_in));

        btnLogin=(Button)findViewById(R.id.textViewLogin);
        txtCreateNewAccount=(TextView)findViewById(R.id.textViewNoAccountCreateNew);
        txtForgotPassword=(TextView)findViewById(R.id.textViewForgotPassword);
        txtPrivacyPolicy=(TextView)findViewById(R.id.textViewPrivacyPolicy);

        btnLogin.setOnClickListener(this);
        txtCreateNewAccount.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        txtPrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.textViewLogin :
                Intent intent=new Intent(SignInAccount.this,HomeScreen.class);
                startActivity(intent);
                finish();
                break;
            case R.id.textViewNoAccountCreateNew :
                Intent intent1=new Intent(SignInAccount.this,CreateAccount.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.textViewForgotPassword :
                Intent intent2=new Intent(SignInAccount.this,ForgotPassword.class);
                startActivity(intent2);
                break;
            case R.id.textViewPrivacyPolicy :
                Intent  intent3=new Intent(SignInAccount.this,PrivacyPolicy.class);
                startActivity(intent3);
                break;
        }
    }
}
