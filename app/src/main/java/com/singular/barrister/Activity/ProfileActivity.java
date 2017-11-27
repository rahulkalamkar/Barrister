package com.singular.barrister.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

public class ProfileActivity extends AppCompatActivity implements IDataChangeListener<IModel> {
    private MenuItem btnEdit, btnSubmit;
    TextView txtName, txtNumber, txtAddress, txtEmailId;
    LinearLayout linearLayout;
    ProgressBar mProgressBar;

    RetrofitManager retrofitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.menu_profile));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_profile));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtName = (TextView) findViewById(R.id.textViewName);
        txtEmailId = (TextView) findViewById(R.id.textViewEmailId);
        txtNumber = (TextView) findViewById(R.id.textViewNumber);
        txtAddress = (TextView) findViewById(R.id.textViewAddress);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        linearLayout = (LinearLayout) findViewById(R.id.profileContainer);

        retrofitManager = new RetrofitManager();
        getProfile();
    }

    public void getProfile() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            mProgressBar.setVisibility(View.VISIBLE);
            retrofitManager.getProfile(this, new UserPreferance(getApplicationContext()).getToken());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            setDataWithoutNetwork();
        }
    }

    public void setDataWithoutNetwork() {
        UserPreferance userPreferance = new UserPreferance(getApplicationContext());
        txtName.setText(userPreferance.getFirst_name() + " " + userPreferance.getLast_name());
        txtNumber.setText(userPreferance.getMobile());
        txtEmailId.setText(userPreferance.getEmail());
        txtAddress.setText(userPreferance.getAddress() != null ? userPreferance.getAddress() : "");
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_done, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        btnEdit = menu.findItem(R.id.menuEdit);
        btnSubmit = menu.findItem(R.id.menuSubmit);

        btnSubmit.setVisible(false);
        btnEdit.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
            setDataWithoutNetwork();
        } else {

        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
