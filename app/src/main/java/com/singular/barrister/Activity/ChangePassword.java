package com.singular.barrister.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

public class ChangePassword extends AppCompatActivity implements IDataChangeListener<IModel> {

    EditText edtOldPassword, edtNewPassword, edtNewPasswordConfirm;
    RetrofitManager retrofitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.menu_change_password));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_change_password));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        retrofitManager = new RetrofitManager();

        edtOldPassword = (EditText) findViewById(R.id.edtTextOldPassword);
        edtNewPassword = (EditText) findViewById(R.id.edtTextNewPassword);
        edtNewPasswordConfirm = (EditText) findViewById(R.id.edtTextNewPasswordConfirm);
    }

    public void checkValue() {
        if (TextUtils.isEmpty(edtOldPassword.getText().toString())) {
            edtOldPassword.setError("Enter current password");
        } else if (TextUtils.isEmpty(edtNewPassword.getText().toString())) {
            edtOldPassword.setError(null);
            edtNewPassword.setError("Enter new password");
        } else if (TextUtils.isEmpty(edtNewPasswordConfirm.getText().toString())) {
            edtOldPassword.setError(null);
            edtNewPassword.setError(null);
            edtNewPasswordConfirm.setError("Enter new password");
        } else if (!edtNewPassword.getText().toString().equalsIgnoreCase(edtNewPasswordConfirm.getText().toString())) {
            edtOldPassword.setError(null);
            edtNewPassword.setError(null);
            edtNewPasswordConfirm.setError("Password doesn't match");
        } else {
            if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                retrofitManager.changePassword(this, new UserPreferance(getApplicationContext()).getToken(), edtOldPassword.getText().toString(),
                        edtNewPassword.getText().toString(), edtNewPasswordConfirm.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
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
                checkValue();
                break;
            case android.R.id.home:
                finish();
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
            if (simpleMessageResponse != null) {
                Toast.makeText(getApplicationContext(), "" + simpleMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "OOPS! Try after sometime", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "OOPS! Try after sometime", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
