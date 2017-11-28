package com.singular.barrister.Activity.SubActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.R;

public class DisplayClientActivity extends AppCompatActivity {

    private MenuItem btnEdit, btnSubmit;
    TextView txtName, txtNumber, txtEmailId;
    Client client;
    LinearLayout linearLayout;
    ProgressBar mProgressBar;
    FrameLayout mFragmentContainer;

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
        setData();
    }

    public void setData() {
        txtName.setText(client.getClient().getFirst_name() + " " + client.getClient().getLast_name());
        txtNumber.setText(client.getClient().getCountry_code() + " " + client.getClient().getMobile());
        txtEmailId.setText(client.getClient().getEmail());
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
                btnEdit.setVisible(false);/*
                linearLayout.setVisibility(View.GONE);
                mFragmentContainer.setVisibility(View.VISIBLE);*/
                //  showFragment();
                break;
            case R.id.menuSubmit:
                //     updateProfile();
                break;
        }
        return true;
    }
}
