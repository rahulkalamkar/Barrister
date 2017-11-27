package com.singular.barrister.Activity;

import android.content.Intent;
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

import com.singular.barrister.Activity.SignInAccount;
import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

public class ForgotPassword extends AppCompatActivity implements IDataChangeListener<IModel>{

    EditText edtEmailId;
    ProgressBar mProgressBar;
RetrofitManager retrofitManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.forgot_password_title));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.forgot_password_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtEmailId=(EditText)findViewById(R.id.editTextEmailId);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar2);
        retrofitManager=new RetrofitManager();
    }

    public void requestForgotPassword(String emailId)
    {
        if(new NetworkConnection(getApplicationContext()).isNetworkAvailable())
        {
retrofitManager.forgotPassword(this,emailId);
        }
        else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.network_error),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_done, menu);
        return true;
    }

    public void checkEntry()
    {
        if(TextUtils.isEmpty(edtEmailId.getText().toString()))
        {
            edtEmailId.setError("Enter emailId");
        }
        else{
            edtEmailId.setError(null);
            mProgressBar.setVisibility(View.VISIBLE);
            requestForgotPassword(edtEmailId.getText().toString());
        }
    }

    @Override
    public void onDataReceived(IModel response) {
        if (response!=null && response instanceof SimpleMessageResponse)
        {
            SimpleMessageResponse simpleMessageResponse=(SimpleMessageResponse)response;
            if(simpleMessageResponse.getStatus_code() ==404)
            {
                edtEmailId.setError("Enter correct emailId");
                Toast.makeText(getApplicationContext(),"Enter correct emailId",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Password has been sent your mail id",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            edtEmailId.setError("Enter correct emailId");
            Toast.makeText(getApplicationContext(),"Enter correct emailId",Toast.LENGTH_SHORT).show();
        }

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSubmit:
                checkEntry();
                Intent intent1 = new Intent(ForgotPassword.this, SignInAccount.class);
                startActivity(intent1);
                finish();
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
