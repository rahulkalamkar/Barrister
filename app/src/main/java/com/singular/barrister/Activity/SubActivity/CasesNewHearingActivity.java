package com.singular.barrister.Activity.SubActivity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.singular.barrister.Interface.CaseListeners;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.DatePickerWindow;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.util.Calendar;

public class CasesNewHearingActivity extends AppCompatActivity implements CaseListeners, IDataChangeListener<IModel> {

    TextInputEditText edtDate, edtCaseNotes;
    ProgressBar mProgressBar;
    String caseId;
    TextInputLayout txtILDate, txtILNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_sub);
        if (getActionBar() != null) {
            getActionBar().setTitle("New Hearing Date");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("New Hearing Date");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        edtDate = (TextInputEditText) findViewById(R.id.editTextDate);
        edtCaseNotes = (TextInputEditText) findViewById(R.id.editTextCaseNotes);
        txtILDate = (TextInputLayout) findViewById(R.id.editTextDateError);
        txtILNote = (TextInputLayout) findViewById(R.id.editTextCaseNotesError);

        txtILDate.setErrorEnabled(false);
        txtILNote.setErrorEnabled(false);

        caseId = getIntent().getExtras().getString("CaseID");

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerWindow datePickerWindow = new DatePickerWindow(getApplicationContext(), edtDate, CasesNewHearingActivity.this);
            }
        });
    }

    RetrofitManager retrofitManager;

    public void checkValues() {
        if (TextUtils.isEmpty(edtDate.getText().toString())) {
            txtILDate.setErrorEnabled(true);
            txtILNote.setErrorEnabled(false);
            txtILDate.setError("Select date");
            txtILNote.setError(null);
        } else if (TextUtils.isEmpty(edtCaseNotes.getText().toString())) {
            txtILDate.setErrorEnabled(false);
            txtILNote.setErrorEnabled(true);
            txtILDate.setError(null);
            txtILNote.setError("Enter notes");
        } else {
            txtILNote.setError(null);
            txtILDate.setError(null);
            if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                mProgressBar.setVisibility(View.VISIBLE);
                retrofitManager = new RetrofitManager();
                retrofitManager.addHearingDate(this, new UserPreferance(getApplicationContext()).getToken(), caseId, edtDate.getText().toString(), edtCaseNotes.getText().toString());
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

    public void returnDataToHome(int resultCode) {
        Intent returnIntent = getIntent();
        returnIntent.putExtra("hearing", edtCaseNotes.getText().toString());
        returnIntent.putExtra("date", edtDate.getText().toString());
        returnIntent.putExtra("result", resultCode);
        setResult(RESULT_OK, returnIntent);
        finish();
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
    public void dateTime(String date) {
        edtDate.setText(date);
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        returnDataToHome(1);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
