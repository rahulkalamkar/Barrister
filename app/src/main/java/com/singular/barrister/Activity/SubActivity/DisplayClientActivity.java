package com.singular.barrister.Activity.SubActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

public class DisplayClientActivity extends AppCompatActivity implements IDataChangeListener<IModel> {

    private MenuItem btnEdit, btnSubmit;
    TextView txtName, txtNumber, txtEmailId;
    EditText edtFirstName, edtLAstName;
    Client client;
    LinearLayout linearLayout, layoutNameContainer, layoutEditContainer;
    ProgressBar mProgressBar;
    FrameLayout mFragmentContainer;
    RetrofitManager retrofitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_client);
        if (getActionBar() != null) {
            getActionBar().setTitle("Client details");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Client details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        client = (Client) getIntent().getExtras().getSerializable("Client");

        txtName = (TextView) findViewById(R.id.textViewName);
        txtNumber = (TextView) findViewById(R.id.textViewNumber);
        txtEmailId = (TextView) findViewById(R.id.textViewEmailId);
        mFragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        linearLayout = (LinearLayout) findViewById(R.id.profileContainer);

        edtFirstName = (EditText) findViewById(R.id.EditTextFirstName);
        edtLAstName = (EditText) findViewById(R.id.EditTextLastName);

        layoutEditContainer = (LinearLayout) findViewById(R.id.EditContainer);
        layoutNameContainer = (LinearLayout) findViewById(R.id.NameContainer);
        setData();
    }

    public void setData() {
        txtName.setText(client.getClient().getFirst_name() + " " + client.getClient().getLast_name());
        txtNumber.setText(client.getClient().getCountry_code() + " " + client.getClient().getMobile());
        txtEmailId.setText(client.getClient().getEmail());
    }

    String newFirstName, newLastName;

    public void displayName(boolean isToShow) {
        if (isToShow) {
            layoutNameContainer.setVisibility(View.VISIBLE);
            layoutEditContainer.setVisibility(View.GONE);
            if (newFirstName != null)
                setName(newFirstName, newLastName);
            else
                setName(client.getClient().getFirst_name(), client.getClient().getLast_name());
        } else {
            layoutNameContainer.setVisibility(View.GONE);
            layoutEditContainer.setVisibility(View.VISIBLE);
            if (newFirstName != null)
                setTextToEditSection(newFirstName, newLastName);
            else
                setTextToEditSection(client.getClient().getFirst_name(), client.getClient().getLast_name());
        }
    }

    public void setTextToEditSection(String first_name, String last_name) {
        edtFirstName.setText(first_name);
        edtLAstName.setText(last_name);
    }

    public void setName(String first_name, String last_name) {
        txtName.setText(first_name + " " + last_name);
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
            case R.id.menuEdit:
                btnSubmit.setVisible(true);
                btnEdit.setVisible(false);
                displayName(false);
                break;
            case R.id.menuSubmit:
                checkData();
                break;
        }
        return true;
    }

    public void checkData() {
        if (TextUtils.isEmpty(edtFirstName.getText().toString())) {
            edtFirstName.setError("Enter first name");
            edtLAstName.setError(null);
        } else if (TextUtils.isEmpty(edtLAstName.getText().toString())) {
            edtFirstName.setError(null);
            edtLAstName.setError("Enter last name");
        } else if (client.getClient().getFirst_name().equalsIgnoreCase(edtFirstName.getText().toString()) &&
                client.getClient().getLast_name().equalsIgnoreCase(edtLAstName.getText().toString())) {
            btnSubmit.setVisible(false);
            btnEdit.setVisible(true);
            displayName(true);
        } else {
            edtFirstName.setError(null);
            edtLAstName.setError(null);
            newFirstName = edtFirstName.getText().toString();
            newLastName = edtLAstName.getText().toString();
            updateData();
        }
    }

    public void updateData() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            retrofitManager = new RetrofitManager();
            mProgressBar.setVisibility(View.VISIBLE);
            retrofitManager.updateClient(this, new UserPreferance(getApplicationContext()).getToken(), client.getClient_id(), newFirstName, newLastName);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataReceived(IModel response) {
        btnSubmit.setVisible(false);
        btnEdit.setVisible(true);
        displayName(true);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    @Override
    public void onDataChanged() {

    }
}
