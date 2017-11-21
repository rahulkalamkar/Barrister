package com.singular.barrister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener{

    private TextView txtPrivacyPolicy;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        if (getActionBar() != null)
            getActionBar().setTitle(getResources().getString(R.string.create_account));
        else
            getSupportActionBar().setTitle(getResources().getString(R.string.create_account));

        btnCreate=(Button)findViewById(R.id.textViewRegister);
        btnCreate.setOnClickListener(this);
        txtPrivacyPolicy=(TextView)findViewById(R.id.textViewPrivacy);
        txtPrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.textViewPrivacy :
                Intent intent=new Intent(CreateAccount.this,PrivacyPolicy.class);
                startActivity(intent);
                break;

            case R.id.textViewRegister :
                Intent intent1=new Intent(CreateAccount.this,SignInAccount.class);
                startActivity(intent1);
                break;
        }
    }
}
