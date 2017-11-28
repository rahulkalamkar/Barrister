package com.singular.barrister;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.singular.barrister.Activity.SubActivity.CasesNewHearingActivity;
import com.singular.barrister.Activity.SubActivity.HearingDateActivity;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Cases.CasePersons;

import org.w3c.dom.Text;

public class DisplayCaseActivity extends AppCompatActivity {

    TextView txtStatus, txtType, txtCourtName, txtCRNNumber, txtRegisterNumber, txtRegisterDate,
            txtHearing, txtClientName, txtClientEmailId, txtPhone, txtAddress, txtClientType, txtOppositionName,
            txtOppositionNumber, txtOppositionLawyerName, txtOppositionLawyerNumber;

    LinearLayout layout;
    Case aCaseDetail;
    PopupWindow changeStatusWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_case);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        layout = (LinearLayout) findViewById(R.id.linearLayout);
        initialization();

        aCaseDetail = (Case) getIntent().getExtras().getSerializable("Case");

        setTitle(aCaseDetail.getClient().getFirst_name() + " " + aCaseDetail.getClient().getLast_name() + " Vs " +
                getPerson("Client").getOpp_name());
        setData();
    }

    public CasePersons getPerson(String type) {
        if (aCaseDetail.getPersons().get(0).getType().equalsIgnoreCase(type))
            return aCaseDetail.getPersons().get(0);
        else
            return aCaseDetail.getPersons().get(1);
    }

    public void setData() {
        txtStatus.setText(aCaseDetail.getCase_status());
        txtType.setText(aCaseDetail.getCasetype().getCase_type_name());
        txtCourtName.setText(aCaseDetail.getCourt().getCourt_name());
        txtCRNNumber.setText(aCaseDetail.getCase_cnr_number());
        txtRegisterNumber.setText(aCaseDetail.getCase_register_number());
        txtRegisterDate.setText(aCaseDetail.getCase_register_date());


        txtHearing.setText(aCaseDetail.getHearing().getCase_decision());

        txtClientName.setText(aCaseDetail.getClient().getFirst_name() + " " + aCaseDetail.getClient().getLast_name());
        txtClientEmailId.setText(aCaseDetail.getClient().getEmail());
        txtPhone.setText(aCaseDetail.getClient().getMobile());
        txtAddress.setText(getAddress());
        txtClientType.setText(aCaseDetail.getClient_type());

        txtOppositionName.setText(getPerson("Client").getOpp_name());
        txtOppositionNumber.setText(getPerson("Client").getMobile());
        txtOppositionLawyerName.setText(getPerson("Lawyer").getOpp_name());
        txtOppositionNumber.setText(getPerson("Lawyer").getMobile());

    }

    public void initialization() {
        txtStatus = (TextView) findViewById(R.id.textViewCaseStatus);
        txtType = (TextView) findViewById(R.id.textViewCaseType);
        txtCourtName = (TextView) findViewById(R.id.textViewCourtName);
        txtCRNNumber = (TextView) findViewById(R.id.textViewCNRNumber);
        txtRegisterNumber = (TextView) findViewById(R.id.textViewCaseRegistrationNumber);
        txtRegisterDate = (TextView) findViewById(R.id.textViewCaseRegisterDate);
        txtHearing = (TextView) findViewById(R.id.textViewCaseNextHearing);
        txtClientName = (TextView) findViewById(R.id.textViewClientName);
        txtClientEmailId = (TextView) findViewById(R.id.textViewClientEmailId);
        txtPhone = (TextView) findViewById(R.id.textViewClientPhone);
        txtAddress = (TextView) findViewById(R.id.textViewClientAddress);
        txtClientType = (TextView) findViewById(R.id.textViewClientType);
        txtOppositionName = (TextView) findViewById(R.id.textViewOPOName);
        txtOppositionNumber = (TextView) findViewById(R.id.textViewOPOPhone);
        txtOppositionLawyerName = (TextView) findViewById(R.id.textViewOPOLawyerName);
        txtOppositionLawyerNumber = (TextView) findViewById(R.id.textViewOPOLawyerPhone);
    }

    public String getAddress() {
        String address = "";
        if (aCaseDetail.getCourt().getSubdistrict() != null && aCaseDetail.getCourt().getSubdistrict().getName() != null) {
            address = aCaseDetail.getCourt().getSubdistrict().getName() + ", ";
        }


        if (aCaseDetail.getCourt().getDistrict() != null && aCaseDetail.getCourt().getDistrict().getName() != null) {
            address = address + aCaseDetail.getCourt().getDistrict().getName() + ", ";
        }


        if (aCaseDetail.getCourt().getState() != null && aCaseDetail.getCourt().getState().getName() != null) {
            address = address + aCaseDetail.getCourt().getState().getName();
        }
        return address;
    }

    public void setTitle(String title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title);
        } else {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.casemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menuChangeCaseStatus:
                showChangeStatusWindow();
                break;

            case R.id.menuAddNewHearingDates:
                Intent intent = new Intent(getApplicationContext(), CasesNewHearingActivity.class);
                startActivity(intent);
                break;

            case R.id.menuViewAllPastHearingDates:
                Bundle bundle = new Bundle();
                bundle.putString("Id", aCaseDetail.getId());
                Intent intent1 = new Intent(getApplicationContext(), HearingDateActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
        }
        return true;
    }

    public void showChangeStatusWindow() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.change_case_status_window, null);

        changeStatusWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            changeStatusWindow.setElevation(5.0f);
        }

        RadioButton radioButtonCompleted = (RadioButton) customView.findViewById(R.id.radioButton2);
        RadioButton radioButtonInProgress = (RadioButton) customView.findViewById(R.id.radioButton1);

        if (aCaseDetail.getCase_status().equalsIgnoreCase("completed")) {
            radioButtonInProgress.setChecked(true);
            radioButtonInProgress.setChecked(false);
        } else {
            radioButtonInProgress.setChecked(false);
            radioButtonInProgress.setChecked(true);
        }

        Button cancelButton = (Button) customView.findViewById(R.id.buttonCancel);
        Button submitButton = (Button) customView.findViewById(R.id.buttonSave);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusWindow.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusWindow.dismiss();
            }
        });
        changeStatusWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }
}
