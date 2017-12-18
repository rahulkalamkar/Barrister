package com.singular.barrister;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.SubActivity.CasesNewHearingActivity;
import com.singular.barrister.Activity.SubActivity.HearingDateActivity;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Cases.CasePersons;
import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.Utils;
import com.singular.barrister.Util.WebServiceError;

import org.w3c.dom.Text;

public class DisplayCaseActivity extends AppCompatActivity implements IDataChangeListener<IModel> {

    TextView txtStatus, txtType, txtCourtName, txtCRNNumber, txtRegisterNumber, txtRegisterDate,
            txtHearing, txtHearingDate, txtClientName, txtClientEmailId, txtPhone, txtAddress, txtClientType, txtOppositionName,
            txtOppositionNumber, txtOppositionLawyerName, txtOppositionLawyerNumber;

    LinearLayout layout;
    Case aCaseDetail;
    ProgressBar mProgressBar;

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
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        initialization();

        aCaseDetail = (Case) getIntent().getExtras().getSerializable("Case");

        if (aCaseDetail.getClient() != null)
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

    public void call(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        startActivity(intent);
    }

    public void setData() {
        txtStatus.setText(aCaseDetail.getCase_status());
        if (aCaseDetail.getCasetype() != null)
            txtType.setText(aCaseDetail.getCasetype().getCase_type_name());
        txtCourtName.setText(aCaseDetail.getCourt().getCourt_name());
        txtCRNNumber.setText(aCaseDetail.getCase_cnr_number());
        txtRegisterNumber.setText(aCaseDetail.getCase_register_number());
        txtRegisterDate.setText(Utils.getDateFormatTime(aCaseDetail.getCase_register_date()));


        txtHearing.setText(aCaseDetail.getHearing() != null ? (aCaseDetail.getHearing().getCase_decision() != null ? aCaseDetail.getHearing().getCase_decision() : "") : "");
        txtHearingDate.setText(aCaseDetail.getHearing() != null ? (aCaseDetail.getHearing().getCase_hearing_date() != null ? Utils.getDateFormat(aCaseDetail.getHearing().getCase_hearing_date()) : "") : "");
        if (aCaseDetail.getClient() != null) {
            txtClientName.setText(aCaseDetail.getClient().getFirst_name() + " " + aCaseDetail.getClient().getLast_name());
            txtClientEmailId.setText(aCaseDetail.getClient().getEmail());
            txtPhone.setText(aCaseDetail.getClient().getMobile());
            txtAddress.setText(getAddress());
            txtClientType.setText(aCaseDetail.getClient_type());
        }
        txtOppositionName.setText(getPerson("Client").getOpp_name());
        txtOppositionNumber.setText(getPerson("Client").getMobile());
        txtOppositionLawyerName.setText(getPerson("Lawyer").getOpp_name());
        txtOppositionLawyerNumber.setText(getPerson("Lawyer").getMobile());

    }

    public void initialization() {
        txtStatus = (TextView) findViewById(R.id.textViewCaseStatus);
        txtType = (TextView) findViewById(R.id.textViewCaseType);
        txtCourtName = (TextView) findViewById(R.id.textViewCourtName);
        txtCRNNumber = (TextView) findViewById(R.id.textViewCNRNumber);
        txtRegisterNumber = (TextView) findViewById(R.id.textViewCaseRegistrationNumber);
        txtRegisterDate = (TextView) findViewById(R.id.textViewCaseRegisterDate);
        txtHearing = (TextView) findViewById(R.id.textViewCaseNextHearing);
        txtHearingDate = (TextView) findViewById(R.id.textViewCaseNextDate);
        txtClientName = (TextView) findViewById(R.id.textViewClientName);
        txtClientEmailId = (TextView) findViewById(R.id.textViewClientEmailId);
        txtPhone = (TextView) findViewById(R.id.textViewClientPhone);
        txtAddress = (TextView) findViewById(R.id.textViewClientAddress);
        txtClientType = (TextView) findViewById(R.id.textViewClientType);
        txtOppositionName = (TextView) findViewById(R.id.textViewOPOName);
        txtOppositionNumber = (TextView) findViewById(R.id.textViewOPOPhone);
        txtOppositionLawyerName = (TextView) findViewById(R.id.textViewOPOLawyerName);
        txtOppositionLawyerNumber = (TextView) findViewById(R.id.textViewOPOLawyerPhone);

        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+91" + txtPhone.getText().toString());
            }
        });

        txtOppositionLawyerNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+91" + txtOppositionLawyerNumber.getText().toString());
            }
        });

        txtOppositionNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+91" + txtOppositionNumber.getText().toString());
            }
        });

        txtClientEmailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(txtClientEmailId.getText().toString());
            }
        });
    }

    public void sendEmail(String emailId) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailId, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
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
                changeStatusAlert();
                //showChangeStatusWindow();
                break;

            case R.id.menuAddNewHearingDates:
                Bundle bundle1 = new Bundle();
                bundle1.putString("CaseID", aCaseDetail.getId());
                Intent intent = new Intent(getApplicationContext(), CasesNewHearingActivity.class);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 1);
                break;

            case R.id.menuViewAllPastHearingDates:
                if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Id", aCaseDetail.getId());
                    Intent intent1 = new Intent(getApplicationContext(), HearingDateActivity.class);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                } else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    String selectedItem;

    public void changeStatusAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayCaseActivity.this);
        builder.setTitle("Change status");

        int i = 0;

        if (aCaseDetail.getCase_status().equalsIgnoreCase("completed")) {
            i = 1;
        } else {
            i = 0;
        }

        //list of items
        final String[] items = {"in-Progress", "completed"};
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
                            txtStatus.setText(selectedItem);
                            mProgressBar.setVisibility(View.VISIBLE);
                            RetrofitManager retrofitManager = new RetrofitManager();
                            retrofitManager.changeCaseStatus(DisplayCaseActivity.this, new UserPreferance(getApplicationContext()).getToken(), aCaseDetail.getId(), selectedItem);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && data.getStringExtra("hearing") != null) {
            txtHearing.setText(data.getStringExtra("hearing"));
            txtHearingDate.setText(data.getStringExtra("date"));
            updateHomeScreen();
        } else if (data != null && data.getStringExtra("hearing") != null) {
            txtHearing.setText(data.getStringExtra("hearing"));
            txtHearingDate.setText(data.getStringExtra("date"));
            updateHomeScreen();
        }
    }

    public void updateHomeScreen() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.singular.barrister");
        sendBroadcast(intent);
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        updateHomeScreen();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
