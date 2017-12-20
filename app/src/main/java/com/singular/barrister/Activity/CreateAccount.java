package com.singular.barrister.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.singular.barrister.Model.RegisterResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;
import com.singular.barrister.WebActivity;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener, IDataChangeListener<IModel> {

    private TextView txtPrivacyPolicy;
    private Button btnCreate;
    private TextInputEditText edtNumber, edtEmailId, edtPassword, edtFirstName, edtLastName, edtRefferalCode, edtAddress;
    private TextInputLayout txtIPNumber, txtIPEmailId, txtIPPassword, txtIPFirstName, txtIPLastName, txtIPRefferalCode, txtIPAddress;

    private ProgressBar progressBar;

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        edtNumber = (TextInputEditText) findViewById(R.id.editTextPhoneNumber);
        edtEmailId = (TextInputEditText) findViewById(R.id.editTextEmailId);
        edtFirstName = (TextInputEditText) findViewById(R.id.editTextFirstName);
        edtLastName = (TextInputEditText) findViewById(R.id.editTextLastName);
        edtPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        edtRefferalCode = (TextInputEditText) findViewById(R.id.editTextReferralCode);

        txtIPNumber = (TextInputLayout) findViewById(R.id.editTextPhoneNumberError);
        txtIPEmailId = (TextInputLayout) findViewById(R.id.editTextEmailIdError);
        txtIPFirstName = (TextInputLayout) findViewById(R.id.editTextFirstNameError);
        txtIPLastName = (TextInputLayout) findViewById(R.id.editTextLastNameError);
        txtIPPassword = (TextInputLayout) findViewById(R.id.editTextPasswordError);
        txtIPRefferalCode = (TextInputLayout) findViewById(R.id.editTextReferralCodeError);

        txtIPNumber.setErrorEnabled(false);
        txtIPEmailId.setErrorEnabled(false);
        txtIPFirstName.setErrorEnabled(false);
        txtIPLastName.setErrorEnabled(false);
        txtIPPassword.setErrorEnabled(false);
        txtIPRefferalCode.setErrorEnabled(false);

        edtNumber.setOnClickListener(this);
        txtIPNumber.setOnClickListener(this);
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
                checkValues();
                break;

            case android.R.id.home:
                startActivity(new Intent(CreateAccount.this,
                        LandingScreen.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateAccount.this,
                LandingScreen.class));
        super.onBackPressed();
    }

    public static int APP_REQUEST_CODE = 99;

    public void initAccountKitSmsFlow() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
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
            String toastMessage = "";
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
    private void getAccount() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = phoneNumber.toString();
                country_code = phoneNumber.getCountryCode();
                edtNumber.setText(phoneNumberString);
                progressBar.setVisibility(View.GONE);
                setOtherErrorNull();
                // Surface the result to your user in an appropriate way.
                Toast.makeText(getApplicationContext(),
                        phoneNumberString,
                        Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e("AccountKit", error.toString());
                // Handle Error
            }
        });
    }

    public void checkValues() {
        if (TextUtils.isEmpty(edtNumber.getText().toString())) {
            setOtherErrorNull();
            txtIPNumber.setErrorEnabled(true);
            txtIPNumber.setError("Enter phone number");
        } else if (TextUtils.isEmpty(edtEmailId.getText().toString())) {
            setOtherErrorNull();
            txtIPEmailId.setErrorEnabled(true);
            txtIPEmailId.setError("Enter email id");
        } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            setOtherErrorNull();
            txtIPPassword.setErrorEnabled(true);
            txtIPPassword.setError("Enter password");
        } else if (TextUtils.isEmpty(edtFirstName.getText().toString())) {
            setOtherErrorNull();
            txtIPFirstName.setErrorEnabled(true);
            txtIPFirstName.setError("Enter first name");
        } else if (TextUtils.isEmpty(edtLastName.getText().toString())) {
            setOtherErrorNull();
            txtIPLastName.setErrorEnabled(true);
            txtIPLastName.setError("Enter last name");
        } else {
            setOtherErrorNull();
            txtIPNumber.setError(null);
            txtIPEmailId.setError(null);
            txtIPPassword.setError(null);
            txtIPFirstName.setError(null);
            txtIPLastName.setError(null);
            RetrofitManager retrofitManager = new RetrofitManager();
            if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                retrofitManager.setRegistration(this, edtFirstName.getText().toString(), edtLastName.getText().toString(), edtNumber.getText().toString(), country_code,
                        edtEmailId.getText().toString(), edtPassword.getText().toString(), edtRefferalCode.getText().toString(), "", "");
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setOtherErrorNull() {
        txtIPNumber.setError(null);
        txtIPEmailId.setError(null);
        txtIPPassword.setError(null);
        txtIPFirstName.setError(null);
        txtIPLastName.setError(null);

        txtIPNumber.setErrorEnabled(false);
        txtIPEmailId.setErrorEnabled(false);
        txtIPFirstName.setErrorEnabled(false);
        txtIPLastName.setErrorEnabled(false);
        txtIPPassword.setErrorEnabled(false);
        txtIPRefferalCode.setErrorEnabled(false);
    }

    private String country_code = "+91";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewPrivacy:
                Bundle bundle = new Bundle();
                bundle.putString("Name", getResources().getString(R.string.privacy_policy));
                bundle.putString("Url", "http://www.barristerdiary.com/privacy.html");
                Intent i = new Intent(getApplicationContext(), WebActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                /*Intent intent = new Intent(CreateAccount.this, PrivacyPolicy.class);
                startActivity(intent);*/
                break;

            case R.id.editTextPhoneNumberError:
            case R.id.editTextPhoneNumber:
                progressBar.setVisibility(View.VISIBLE);
                initAccountKitSmsFlow();
                break;

            case R.id.textViewRegister:
                Intent intent1 = new Intent(CreateAccount.this, SignInAccount.class);
                startActivity(intent1);
                break;
        }
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
                    , registerResponse.getToken(), false);
            Intent intent1 = new Intent(CreateAccount.this, SignInAccount.class);
            startActivity(intent1);
            finish();
        }
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
