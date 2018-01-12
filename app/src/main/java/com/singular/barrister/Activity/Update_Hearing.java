package com.singular.barrister.Activity;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.DisplayCaseActivity;
import com.singular.barrister.Model.Cases.CaseHearing;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.Utils;
import com.singular.barrister.Util.WebServiceError;

public class Update_Hearing extends AppCompatActivity implements IDataChangeListener {


    CaseHearing caseHearing;
    TextInputEditText edtHearingStatus, edtHearingNotes, edtHearingDecision;
    TextInputLayout ltHearingStatus, ltHearingNotes, ltHearingDecision;
    ProgressBar mProgressBar;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__hearing);

        ltHearingStatus = (TextInputLayout) findViewById(R.id.editTextCaseStatusError);
        ltHearingNotes = (TextInputLayout) findViewById(R.id.editTextCaseNotesError);
        ltHearingDecision = (TextInputLayout) findViewById(R.id.editTextCaseDecisionError);

        edtHearingStatus = (TextInputEditText) findViewById(R.id.editTextCaseStatus);
        edtHearingNotes = (TextInputEditText) findViewById(R.id.editTextCaseNotes);
        edtHearingDecision = (TextInputEditText) findViewById(R.id.editTextCaseDecision);

        txt = (TextView) findViewById(R.id.textView);

        ltHearingStatus.setFocusable(false);
        edtHearingStatus.setFocusable(false);
        ltHearingStatus.setFocusableInTouchMode(false);
        edtHearingStatus.setFocusableInTouchMode(false);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        caseHearing = (CaseHearing) bundle.getSerializable("CaseHearing");
        setData();
        if (getActionBar() != null) {
            getActionBar().setTitle("Update Hearing");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Update Hearing");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtHearingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusAlert();
            }
        });
    }

    public void dateYetToCome(String date) {
        ltHearingDecision.setVisibility(View.GONE);
        txt.setVisibility(View.VISIBLE);
        txt.setText("Hearing date for this case is " + date + " means hearing is not completed, You can add case decision once hearing is completed.");
    }

    public void setData() {

        if (!Utils.compareMonth(caseHearing.getCase_hearing_date())) {
            ltHearingStatus.setVisibility(View.GONE);
            ltHearingNotes.setVisibility(View.VISIBLE);
            ltHearingDecision.setVisibility(View.GONE);
            dateYetToCome(Utils.getDateFormat(caseHearing.getCase_hearing_date()));
        } else {
            txt.setVisibility(View.GONE);
            ltHearingStatus.setVisibility(View.VISIBLE);
            ltHearingNotes.setVisibility(View.VISIBLE);
            ltHearingDecision.setVisibility(View.VISIBLE);
        }

        if (caseHearing.getCase_notes() != null) {
            edtHearingNotes.setText(caseHearing.getCase_notes());
        }

        if (caseHearing.getCase_decision() != null) {
            edtHearingDecision.setText(caseHearing.getCase_decision());
        }

        if (caseHearing.getCase_disposed() != null) {
            edtHearingStatus.setText(caseHearing.getCase_disposed());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_done, menu);
        return true;
    }

    String selectedItem;

    public void changeStatusAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Update_Hearing.this);
        builder.setTitle("Hearing status");

        int i = 3;

        if (caseHearing.getCase_disposed() != null) {
            if (caseHearing.getCase_disposed().equalsIgnoreCase("hearing completed")) {
                i = 2;
            } else if (caseHearing.getCase_disposed().equalsIgnoreCase("hearing postponed")) {
                i = 1;
            } else {
                i = 0;
            }
        }
        //list of items
        final String[] items = {"hearing completed", "hearing postponed", "case completed"};
        builder.setSingleChoiceItems(items, i,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItem = items[which];
                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedItem != null && !TextUtils.isEmpty(selectedItem)) {
                            edtHearingStatus.setText(selectedItem);
                        }
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
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

    public void updateValues(String case_notes, String case_decision, String case_disposed) {
        RetrofitManager retrofitManager = new RetrofitManager();
        retrofitManager.updateHearingStatus(this, new UserPreferance(getApplicationContext()).getToken(), caseHearing.getId(), case_decision, case_notes, case_disposed);
    }

    public void checkValues() {
        String case_decision = "", case_notes = "", case_status = "";
        if (edtHearingStatus.getText().toString() != null) {
            case_status = edtHearingStatus.getText().toString();
        }

        if (edtHearingNotes.getText().toString() != null) {
            case_notes = edtHearingNotes.getText().toString();
        }

        if (edtHearingDecision.getText().toString() != null) {
            case_decision = edtHearingDecision.getText().toString();
        }

        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            updateValues(case_notes, case_decision, case_status);
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
