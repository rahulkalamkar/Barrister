package com.singular.barrister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    private TextView txtPrivacyPolicy;
    private Button btnCreate;
    private EditText edtNumber,edtEmailId,edtPassword,edtFirstName,edtLastName,edtRefferalCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.create_account));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.create_account));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtNumber=(EditText)findViewById(R.id.editTextPhoneNumber);
        edtNumber.setOnClickListener(this);
        btnCreate = (Button) findViewById(R.id.textViewRegister);
        btnCreate.setOnClickListener(this);
        txtPrivacyPolicy = (TextView) findViewById(R.id.textViewPrivacy);
        txtPrivacyPolicy.setOnClickListener(this);
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
                Intent intent1 = new Intent(CreateAccount.this, SignInAccount.class);
                startActivity(intent1);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public static int APP_REQUEST_CODE = 99;
    public void initAccountKitSmsFlow() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage= "";
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                    getAccount();
                }
            }
            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


    /**
     * Gets current account from Facebook Account Kit which include user's phone number.
     */
    private void getAccount(){
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = phoneNumber.toString();

                edtNumber.setText(phoneNumberString);
                // Surface the result to your user in an appropriate way.
                Toast.makeText(getApplicationContext(),
                        phoneNumberString,
                        Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e("AccountKit",error.toString());
                // Handle Error
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewPrivacy:
                Intent intent = new Intent(CreateAccount.this, PrivacyPolicy.class);
                startActivity(intent);
                break;

            case R.id.editTextPhoneNumber :
                initAccountKitSmsFlow();
            break;

            case R.id.textViewRegister:
                Intent intent1 = new Intent(CreateAccount.this, SignInAccount.class);
                startActivity(intent1);
                break;
        }
    }
}
