package com.singular.barrister.Activity.SubActivity;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.singular.barrister.R;

public class AddCaseActivity extends AppCompatActivity {

    TextInputLayout txtILCaseStatus, txtILNextHearingDate, txtILCaseType, txtILSelectCourt, txtILCaseCNRNumber, txtILCaseRegisterNumber, txtILCaseRegisterDate,
            txtILCaseNotes, txtILOpoName, txtILOpoNumber, txtILOpoLawName, txtILOpoLawNumber;

    TextInputEditText edtCaseStatus, edtNextHearingDate, edtCaseType, edtSelectCourt, edtCaseCNRNumber, edtCaseRegisterNumber, edtCaseRegisterDate,
            edtCaseNotes, edtOpoName, edtOpoNumber, edtOpoLawyerName, edtOpoLawyerNumber;

    TextView txtSelectClient;
    ProgressBar mProgressBar;
    RadioButton rdtPetitioner, rdtDefender, rdtThirdParty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_case);
        if (getActionBar() != null) {
            getActionBar().setTitle("Add case");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add case");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    public void initView() {
        txtSelectClient = (TextView) findViewById(R.id.textViewSelectClient);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        rdtPetitioner = (RadioButton) findViewById(R.id.radioButtonPetitioner);
        rdtPetitioner = (RadioButton) findViewById(R.id.radioButtonDefender);
        rdtPetitioner = (RadioButton) findViewById(R.id.radioButtonThirdParty);

        txtILCaseStatus = (TextInputLayout) findViewById(R.id.EditTextCaseStatusError);
        edtCaseStatus = (TextInputEditText) findViewById(R.id.EditTextCaseStatus);

        txtILNextHearingDate = (TextInputLayout) findViewById(R.id.EditTextCaseHearingDateError);
        edtNextHearingDate = (TextInputEditText) findViewById(R.id.EditTextCaseHearingDate);

        txtILCaseType = (TextInputLayout) findViewById(R.id.EditTextCaseTypeError);
        edtCaseType = (TextInputEditText) findViewById(R.id.EditTextCaseType);

        txtILSelectCourt = (TextInputLayout) findViewById(R.id.EditTextSelectCourtError);
        edtSelectCourt = (TextInputEditText) findViewById(R.id.EditTextSelectCourt);

        txtILCaseCNRNumber = (TextInputLayout) findViewById(R.id.EditTextCaseCNRNummberError);
        edtCaseCNRNumber = (TextInputEditText) findViewById(R.id.EditTextCaseCNRNummber);

        txtILCaseRegisterNumber = (TextInputLayout) findViewById(R.id.EditTextCaseRegisterNumberError);
        edtCaseRegisterNumber = (TextInputEditText) findViewById(R.id.EditTextCaseRegisterNumber);

        txtILCaseRegisterDate = (TextInputLayout) findViewById(R.id.EditTextCaseRegisterDateError);
        edtCaseRegisterDate = (TextInputEditText) findViewById(R.id.EditTextCaseRegisterDate);

        txtILCaseNotes = (TextInputLayout) findViewById(R.id.EditTextCaseNotesError);
        edtCaseNotes = (TextInputEditText) findViewById(R.id.EditTextCaseNotes);

        txtILCaseNotes = (TextInputLayout) findViewById(R.id.EditTextOPONameError);
        edtOpoName = (TextInputEditText) findViewById(R.id.EditTextOPOName);

        txtILOpoNumber = (TextInputLayout) findViewById(R.id.EditTextOPOMobileError);
        edtOpoNumber = (TextInputEditText) findViewById(R.id.EditTextOPOMobile);

        txtILOpoLawName = (TextInputLayout) findViewById(R.id.EditTextOPOLawyerNameError);
        edtOpoLawyerName = (TextInputEditText) findViewById(R.id.EditTextOPOLawyerName);

        txtILOpoLawNumber = (TextInputLayout) findViewById(R.id.EditTextOPOLawyerPhoneError);
        edtOpoLawyerNumber = (TextInputEditText) findViewById(R.id.EditTextOPOLawyerPhone);

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
                break;
        }
        return true;
    }
}
