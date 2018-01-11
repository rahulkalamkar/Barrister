package com.singular.barrister.Activity.SubActivity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

public class AddClientActivity extends AppCompatActivity implements IDataChangeListener<IModel> {

    TextInputLayout ltedtFirstName, ltedtLastName, ltedtPhone, ltedtEmailId, ltedtPassword;
    TextInputEditText edtFirstName, edtLastName, edtPhone, edtEmailId, edtPassword;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        if (getActionBar() != null) {
            getActionBar().setTitle("Add client");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add client");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ltedtFirstName = (TextInputLayout) findViewById(R.id.LayoutEditTextFirstName);
        ltedtLastName = (TextInputLayout) findViewById(R.id.LayoutEditTextLastName);
        ltedtEmailId = (TextInputLayout) findViewById(R.id.LayoutEditTextEMail);
        ltedtPhone = (TextInputLayout) findViewById(R.id.LayoutEditTextNumber);
        ltedtPassword = (TextInputLayout) findViewById(R.id.LayoutEditTextPassword);

        edtFirstName = (TextInputEditText) findViewById(R.id.editTextFirstName);
        edtLastName = (TextInputEditText) findViewById(R.id.editTextLastName);
        edtEmailId = (TextInputEditText) findViewById(R.id.editTextEMail);
        edtPhone = (TextInputEditText) findViewById(R.id.editTextNumber);
        edtPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void setOtherErrorNull() {
        ltedtFirstName.setError(null);
        ltedtLastName.setError(null);
        ltedtEmailId.setError(null);
        ltedtPhone.setError(null);
        ltedtPassword.setError(null);
    }

    public void checkValues() {
        if (TextUtils.isEmpty(edtFirstName.getText().toString())) {
            setOtherErrorNull();
            ltedtFirstName.setError("Enter first name");
        } else if (TextUtils.isEmpty(edtLastName.getText().toString())) {
            setOtherErrorNull();
            ltedtLastName.setError("Enter last name");
        } else if (TextUtils.isEmpty(edtPhone.getText().toString())) {
            setOtherErrorNull();
            ltedtPhone.setError("Enter phone number");
        } else if (edtPhone.getText().toString().trim().length() < 10) {
            setOtherErrorNull();
            ltedtPhone.setError("Enter valid phone number");
        }
        if (TextUtils.isEmpty(edtEmailId.getText().toString())) {
            setOtherErrorNull();
            ltedtEmailId.setError("Enter email id");
        } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            setOtherErrorNull();
            edtPassword.setError("Enter password");
        } else {
            ltedtFirstName.setError(null);
            ltedtLastName.setError(null);
            ltedtEmailId.setError(null);
            ltedtPhone.setError(null);
            ltedtPassword.setError(null);
            RetrofitManager retrofitManager = new RetrofitManager();
            if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                mProgressBar.setVisibility(View.VISIBLE);
                retrofitManager.addClient(this, new UserPreferance(getApplicationContext()).getToken(),
                        edtFirstName.getText().toString(),
                        edtLastName.getText().toString(),
                        edtPhone.getText().toString(),
                        "91",
                        edtEmailId.getText().toString(),
                        edtPassword.getText().toString()
                );
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void returnDataToHome(int resultCode) {
        Intent returnIntent = getIntent();
        returnIntent.putExtra("result", resultCode);
        setResult(RESULT_OK, returnIntent);
        finish();
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
            case android.R.id.home:
                finish();
                break;
            case R.id.menuSubmit:
                checkValues();
                break;
        }
        return true;
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof SimpleMessageResponse) {
            SimpleMessageResponse simpleMessageResponse = (SimpleMessageResponse) response;
            if (!simpleMessageResponse.isAlready_created()) {
                Toast.makeText(getApplicationContext(), "Client added successfully", Toast.LENGTH_SHORT).show();
                returnDataToHome(1);
            } else {
                Toast.makeText(getApplicationContext(), simpleMessageResponse.getMessage() != null ? simpleMessageResponse.getMessage() : "OOPS! something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "OOPS! something went wrong", Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
