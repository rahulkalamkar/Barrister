package com.singular.barrister.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Model.RegisterResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

public class SignInAccount extends AppCompatActivity implements View.OnClickListener, IDataChangeListener<IModel> {

    private Button btnLogin;
    private TextView txtCreateNewAccount, txtForgotPassword, txtPrivacyPolicy;
    private EditText edtNumber, edtPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_account);
        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.sign_in));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.sign_in));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnLogin = (Button) findViewById(R.id.textViewLogin);
        txtCreateNewAccount = (TextView) findViewById(R.id.textViewNoAccountCreateNew);
        txtForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        txtPrivacyPolicy = (TextView) findViewById(R.id.textViewPrivacyPolicy);

        btnLogin.setOnClickListener(this);
        txtCreateNewAccount.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        txtPrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewLogin:
                if (TextUtils.isEmpty(edtNumber.getText().toString())) {
                    edtNumber.setError("Enter phone number");
                    edtPassword.setError(null);
                } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    edtPassword.setError("Enter password");
                    edtNumber.setError(null);
                } else {
                    RetrofitManager retrofitManager = new RetrofitManager();
                    if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                        progressBar.setVisibility(View.VISIBLE);
                        retrofitManager.setLogin(this, edtNumber.getText().toString().trim(), edtPassword.getText().toString(), "");
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                }
                /*Intent intent = new Intent(SignInAccount.this, HomeScreen.class);
                startActivity(intent);
                finish();*/
                break;
            case R.id.textViewNoAccountCreateNew:
                Intent intent1 = new Intent(SignInAccount.this, CreateAccount.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.textViewForgotPassword:
                Intent intent = new Intent(SignInAccount.this, ForgotPassword.class);
                startActivity(intent);
                finish();
                break;
            case R.id.textViewPrivacyPolicy:
                Intent intent3 = new Intent(SignInAccount.this, PrivacyPolicy.class);
                startActivity(intent3);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSubmit:
                if (TextUtils.isEmpty(edtNumber.getText().toString())) {
                    edtNumber.setError("Enter phone number");
                    edtPassword.setError(null);
                } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    edtPassword.setError("Enter password");
                    edtNumber.setError(null);
                } else {
                    RetrofitManager retrofitManager = new RetrofitManager();
                    if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                        progressBar.setVisibility(View.VISIBLE);
                        retrofitManager.setLogin(this, edtNumber.getText().toString().trim(), edtPassword.getText().toString(), "");
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case android.R.id.home:
                startActivity(new Intent(SignInAccount.this,
                        LandingScreen.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignInAccount.this,
                LandingScreen.class));
        super.onBackPressed();
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        progressBar.setVisibility(View.GONE);
        if (response != null && response instanceof RegisterResponse) {
            RegisterResponse registerResponse = (RegisterResponse) response;
            UserPreferance userPreferance = new UserPreferance(getApplicationContext()
                    , registerResponse.getData().getFirst_name()
                    , registerResponse.getData().getLast_name()
                    , registerResponse.getData().getMobile()
                    , registerResponse.getData().getEmail()
                    , registerResponse.getData().getAddress()
                    , registerResponse.getData().getSubscription()
                    , registerResponse.getData().getReferral_code()
                    , registerResponse.getData().getStart_date()
                    , registerResponse.getData().getEnd_date()
                    , registerResponse.getData().getUser_type()
                    , registerResponse.getToken(), true);
            Intent intent1 = new Intent(SignInAccount.this, HomeScreen.class);
            startActivity(intent1);
            finish();
        } else {
            showError();
        }
    }

    public void showError() {
        edtNumber.setError("Enter registered number");
        edtPassword.setError("Incorrect password");
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
