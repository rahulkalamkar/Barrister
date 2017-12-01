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
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.WebServiceError;

import org.w3c.dom.Text;

public class DisplayCaseActivity extends AppCompatActivity implements IDataChangeListener<IModel> {

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


        txtHearing.setText(aCaseDetail.getHearing() != null ? (aCaseDetail.getHearing().getCase_decision() != null ? aCaseDetail.getHearing().getCase_decision() : "") : "");

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
                Bundle bundle1 = new Bundle();
                bundle1.putString("CaseID", aCaseDetail.getId());
                Intent intent = new Intent(getApplicationContext(), CasesNewHearingActivity.class);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 1);
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
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            changeStatusWindow.setElevation(5.0f);
        }

        final RadioButton radioButtonCompleted = (RadioButton) customView.findViewById(R.id.radioButton2);
        final RadioButton radioButtonInProgress = (RadioButton) customView.findViewById(R.id.radioButton1);

        if (aCaseDetail.getCase_status().equalsIgnoreCase("completed")) {
            radioButtonCompleted.setChecked(true);
            radioButtonInProgress.setChecked(false);
        } else {
            radioButtonCompleted.setChecked(false);
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
                boolean isChanged = false;
                if (radioButtonCompleted.isChecked()) {
                    txtStatus.setText("completed");
                    if (!aCaseDetail.getCase_status().equals("completed"))
                        isChanged = true;
                } else {
                    txtStatus.setText("In-Progress");
                    if (!aCaseDetail.getCase_status().equals("In-Progress"))
                        isChanged = true;
                }

                if (isChanged) {
                    RetrofitManager retrofitManager = new RetrofitManager();
                    retrofitManager.changeCaseStatus(DisplayCaseActivity.this, new UserPreferance(getApplicationContext()).getToken(), aCaseDetail.getId(), radioButtonCompleted.isChecked() ? "completed" : "In-Progress");
                }

                changeStatusWindow.dismiss();
            }
        });
        changeStatusWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && data.getStringExtra("hearing") != null) {
            txtHearing.setText(data.getStringExtra("hearing"));
        } else if (data != null && data.getStringExtra("hearing") != null) {
            txtHearing.setText(data.getStringExtra("hearing"));
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
