package com.singular.barrister.Activity.SubActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.singular.barrister.Activity.InenerActivity.SelectCaseType;
import com.singular.barrister.Activity.InenerActivity.SelectClient;
import com.singular.barrister.Activity.InenerActivity.SelectCourt;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.CaseTypeTable;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.Client.ClientTable;
import com.singular.barrister.Database.Tables.CourtTable;
import com.singular.barrister.Database.Tables.SubCaseTypeTable;
import com.singular.barrister.DisplayCaseActivity;
import com.singular.barrister.Interface.CaseListeners;
import com.singular.barrister.Interface.RecycleItem;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Cases.CaseDistrict;
import com.singular.barrister.Model.Cases.CaseState;
import com.singular.barrister.Model.Cases.CaseSubDistrict;
import com.singular.barrister.Model.Cases.SubCaseType;
import com.singular.barrister.Model.CasesSubType;
import com.singular.barrister.Model.CasesTypeData;
import com.singular.barrister.Model.CasesTypeResponse;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Client.ClientDetail;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.DateTimeWindow;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddCaseActivity extends AppCompatActivity implements CaseListeners, IDataChangeListener<IModel> {

    TextInputLayout txtILCaseStatus, txtILNextHearingDate, txtILCaseType, txtILSelectCourt, txtILCaseCNRNumber, txtILCaseRegisterNumber, txtILCaseRegisterDate,
            txtILCaseNotes, txtILOpoName, txtILOpoNumber, txtILOpoLawName, txtILOpoLawNumber;

    TextInputEditText edtCaseStatus, edtNextHearingDate, edtCaseType, edtSelectCourt, edtCaseCNRNumber, edtCaseRegisterNumber, edtCaseRegisterDate,
            edtCaseNotes, edtOpoName, edtOpoNumber, edtOpoLawyerName, edtOpoLawyerNumber;

    TextView txtSelectClient;
    ProgressBar mProgressBar;
    RadioButton rdtPetitioner, rdtDefender, rdtThirdParty;
    ArrayList<CasesTypeData> caseTypeDataList;

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

        caseTypeDataList = new ArrayList<CasesTypeData>();
        initView();
        disableError();
    }

    public void disableError() {
        txtILCaseStatus.setErrorEnabled(false);
        txtILNextHearingDate.setErrorEnabled(false);
        txtILCaseType.setErrorEnabled(false);
        txtILSelectCourt.setErrorEnabled(false);
        txtILCaseCNRNumber.setErrorEnabled(false);
        txtILCaseRegisterNumber.setErrorEnabled(false);
        txtILCaseRegisterDate.setErrorEnabled(false);
        txtILCaseNotes.setErrorEnabled(false);
        txtILOpoName.setErrorEnabled(false);
        txtILOpoNumber.setErrorEnabled(false);
        txtILOpoLawName.setErrorEnabled(false);
        txtILOpoLawNumber.setErrorEnabled(false);
    }

    public void initView() {
        txtSelectClient = (TextView) findViewById(R.id.textViewSelectClient);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        rdtPetitioner = (RadioButton) findViewById(R.id.radioButtonPetitioner);
        rdtDefender = (RadioButton) findViewById(R.id.radioButtonDefender);
        rdtThirdParty = (RadioButton) findViewById(R.id.radioButtonThirdParty);

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

        txtILOpoName = (TextInputLayout) findViewById(R.id.EditTextOPONameError);
        edtOpoName = (TextInputEditText) findViewById(R.id.EditTextOPOName);

        txtILOpoNumber = (TextInputLayout) findViewById(R.id.EditTextOPOMobileError);
        edtOpoNumber = (TextInputEditText) findViewById(R.id.EditTextOPOMobile);

        txtILOpoLawName = (TextInputLayout) findViewById(R.id.EditTextOPOLawyerNameError);
        edtOpoLawyerName = (TextInputEditText) findViewById(R.id.EditTextOPOLawyerName);

        txtILOpoLawNumber = (TextInputLayout) findViewById(R.id.EditTextOPOLawyerPhoneError);
        edtOpoLawyerNumber = (TextInputEditText) findViewById(R.id.EditTextOPOLawyerPhone);


        edtCaseStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatusAlert();
            }
        });

        edtNextHearingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeWindow datePickerWindow = new DateTimeWindow(AddCaseActivity.this, AddCaseActivity.this);
                datePickerWindow.showTimer(true);
            }
        });

        edtSelectCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  List<CourtTable> list = getAllCourt();
                convertList(list);*/
                Intent i = new Intent(getApplicationContext(), SelectCourt.class);
                startActivityForResult(i, 11);
            }
        });

        edtCaseRegisterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isForRegisterDate = true;
                DateTimeWindow datePickerWindow = new DateTimeWindow(AddCaseActivity.this, AddCaseActivity.this);
                datePickerWindow.showTimer(true);
            }
        });

        retrofitManager = new RetrofitManager();
        edtCaseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SelectCaseType.class);
                startActivityForResult(i, 11);
                /*
                if (caseTypeDataList == null)
                    caseTypeDataList = new ArrayList<CasesTypeData>();
                caseTypeDataList.addAll(convertListToCaseType());
                if (caseTypeDataList != null && caseTypeDataList.size() > 0) {
                    selectCaseType();
                } else if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                    retrofitManager.getCourtType(AddCaseActivity.this, new UserPreferance(getApplicationContext()).getToken());
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        txtSelectClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SelectClient.class);
                startActivityForResult(i, 22);

                //  fetchAndDisplayClient();
            }
        });

    }

    boolean isForRegisterDate = false;

    public List<CaseTypeTable> checkValuesForCaseTypeLocally() {
        Dao<CaseTypeTable, Integer> caseTypeTablesDao;
        try {
            caseTypeTablesDao = getHelper(getApplicationContext()).getCaseTypeTableDao();
            return caseTypeTablesDao.queryForAll();
        } catch (SQLException e) {
            return null;
        }
    }


    RetrofitManager retrofitManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_done, menu);
        return true;
    }

    String selectedItem;

    public void selectStatusAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCaseActivity.this);
        builder.setTitle("Select case status");

        int i = 0;

        //list of items
        final String[] items = {"In-Progress", "completed"};
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
                            edtCaseStatus.setText(selectedItem);
                        } else {
                            selectedItem = items[0];
                            edtCaseStatus.setText(selectedItem);
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
    public void dateTime(String date) {
        if (isForRegisterDate) {
            edtCaseRegisterDate.setText(date);
        } else {
            edtNextHearingDate.setText(date);
        }
        isForRegisterDate = false;
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

    public void checkValues() {
        if (rdtDefender.isChecked()) {
            selectedClientType = rdtDefender.getText().toString();
        } else if (rdtPetitioner.isChecked()) {
            selectedClientType = rdtPetitioner.getText().toString();
        } else if (rdtThirdParty.isChecked()) {
            selectedClientType = rdtThirdParty.getText().toString();
        }

        if (TextUtils.isEmpty(edtCaseStatus.getText().toString())) {
            disableError();
            txtILCaseStatus.setErrorEnabled(true);
            txtILCaseStatus.setError("enter case status");
        } else if (TextUtils.isEmpty(edtNextHearingDate.getText().toString())) {
            disableError();
            txtILNextHearingDate.setErrorEnabled(true);
            txtILNextHearingDate.setError("enter next hearing date");
        } else if (TextUtils.isEmpty(edtCaseType.getText().toString())) {
            disableError();
            txtILCaseType.setErrorEnabled(true);
            txtILCaseType.setError("enter case type");
        } else if (TextUtils.isEmpty(edtSelectCourt.getText().toString())) {
            disableError();
            txtILSelectCourt.setErrorEnabled(true);
            txtILSelectCourt.setError("select court");
        } else if (TextUtils.isEmpty(edtCaseCNRNumber.getText().toString())) {
            disableError();
            txtILCaseCNRNumber.setErrorEnabled(true);
            txtILCaseCNRNumber.setError("enter case cnr number");
        } else if (TextUtils.isEmpty(edtCaseRegisterNumber.getText().toString())) {
            disableError();
            txtILCaseRegisterNumber.setErrorEnabled(true);
            txtILCaseRegisterNumber.setError("enter case register number");
        } else if (TextUtils.isEmpty(edtCaseRegisterDate.getText().toString())) {
            disableError();
            txtILCaseRegisterDate.setErrorEnabled(true);
            txtILCaseRegisterDate.setError("enter case register date");
        } else if (TextUtils.isEmpty(edtCaseNotes.getText().toString())) {
            disableError();
            txtILCaseNotes.setErrorEnabled(true);
            txtILCaseNotes.setError("enter case notes");
        } else if (TextUtils.isEmpty(edtOpoName.getText().toString())) {
            disableError();
            txtILOpoName.setErrorEnabled(true);
            txtILOpoName.setError("enter opposition name");
        } else if (TextUtils.isEmpty(edtOpoNumber.getText().toString())) {
            disableError();
            txtILOpoNumber.setErrorEnabled(true);
            txtILOpoNumber.setError("enter  opposition number");
        } else if (edtOpoNumber.getText().toString().length() < 10 || edtOpoNumber.getText().toString().length() != 10) {
            disableError();
            txtILOpoNumber.setErrorEnabled(true);
            txtILOpoNumber.setError("enter  valid opposition number");
        } else if (TextUtils.isEmpty(edtOpoLawyerName.getText().toString())) {
            disableError();
            txtILOpoLawName.setErrorEnabled(true);
            txtILOpoLawName.setError("enter opposition lawyer name");
        } else if (TextUtils.isEmpty(edtOpoLawyerNumber.getText().toString())) {
            disableError();
            txtILOpoLawNumber.setErrorEnabled(true);
            txtILOpoLawNumber.setError("enter opposition lawyer number");
        } else if (edtOpoLawyerNumber.getText().toString().length() < 10 || edtOpoLawyerNumber.getText().toString().length() != 10) {
            disableError();
            txtILOpoLawNumber.setErrorEnabled(true);
            txtILOpoLawNumber.setError("enter valid opposition lawyer number");
        } else if (txtSelectClient.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Select client name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selectedClientType)) {
            Toast.makeText(getApplicationContext(), "select client type", Toast.LENGTH_SHORT).show();
        } else {
            if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                disableError();
                mProgressBar.setVisibility(View.VISIBLE);
                retrofitManager.addCase(this, new UserPreferance(getApplicationContext()).getToken(), selectedClient.getClient_id(), selectedClientType, selectedCourt.getId(), edtCaseCNRNumber.getText().toString(),
                        edtCaseRegisterNumber.getText().toString(),
                        edtCaseRegisterDate.getText().toString(),
                        selectedCaseType.getCase_type_id(), selectedCaseSubType.getSubcase_type_id(),
                        edtCaseStatus.getText().toString(),
                        getJSONFORMAT(edtOpoLawyerName.getText().toString(), "91", edtOpoLawyerNumber.getText().toString()),
                        getJSONFORMAT(edtOpoName.getText().toString(), "91", edtOpoNumber.getText().toString()),
                        getJSONFORMAT(edtOpoName.getText().toString(), "91", edtOpoNumber.getText().toString()),
                        getJSON());
            } else {
                Toast.makeText(getApplicationContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
            }
            selectedClientType = null;
        }
    }

    public String getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("case_hearing_date", edtNextHearingDate.getText().toString());
            jsonObject.put("case_decision", edtCaseNotes.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getJSONFORMAT(String name, String country_code, String mobile) {
        JSONObject map = new JSONObject();
        try {
            map.put("opp_name", name);
            map.put("country_code", country_code);
            map.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map.toString();
    }

    String selectedClientType = "";

    CasesSubType selectedCaseSubType;
    CasesTypeData selectedCaseType;

    public void selectedCaseType(CasesSubType casesSubType) {
        selectedCaseSubType = casesSubType;
        getSelectedCaseType(casesSubType);
        edtCaseType.setText(casesSubType.getSubcase_type_name());
    }

    public void getSelectedCaseType(CasesSubType casesSubType) {
        for (CasesTypeData casesTypeData : caseTypeDataList) {
            for (CasesSubType subType : casesTypeData.getSubCaseData()) {
                if (subType.getSubcase_type_id().equalsIgnoreCase(casesSubType.getSubcase_type_id())) {
                    selectedCaseType = casesTypeData;
                    return;
                }
            }
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof CasesTypeResponse) {
            CasesTypeResponse casesTypeResponse = (CasesTypeResponse) response;
            if (casesTypeResponse.getData().getCasetype() != null) {
                caseTypeDataList.addAll(casesTypeResponse.getData().getCasetype());
                saveCaseType(caseTypeDataList);

            }
        } else if (response != null && response instanceof SimpleMessageResponse) {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Case added successFully", Toast.LENGTH_SHORT).show();
            updateHomeScreen();
            finish();
        }
    }

    public void updateHomeScreen() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.singular.barrister");
        sendBroadcast(intent);
    }

    public void saveCaseType(ArrayList<CasesTypeData> caseTypeDataList) {
        if (caseTypeDataList == null)
            return;

        for (int i = 0; i < caseTypeDataList.size(); i++) {
            CasesTypeData casesTypeData = caseTypeDataList.get(i);
            CaseTypeTable caseTypeTable = new CaseTypeTable(casesTypeData.getCase_type_id(), casesTypeData.getCase_type_name());
            insertDataCaseType(caseTypeTable);
            for (int j = 0; j < caseTypeDataList.get(i).getSubCaseData().size(); j++) {
                CasesSubType casesSubType = caseTypeDataList.get(i).getSubCaseData().get(j);
                SubCaseTypeTable subCaseTypeTable = new SubCaseTypeTable(casesTypeData.getCase_type_id(), casesSubType.getSubcase_type_id(), casesSubType.getSubcase_type_name());
                insertDataSubCaseType(subCaseTypeTable);
            }
        }
    }

    public void insertDataCaseType(CaseTypeTable casesTypeData) {
        Dao<CaseTypeTable, Integer> caseTypeTablesDao;
        try {
            caseTypeTablesDao = getHelper(getApplicationContext()).getCaseTypeTableDao();
            caseTypeTablesDao.create(casesTypeData);
            Log.e("case type Table", "inserted");
        } catch (SQLException e) {
            Log.e("case type table", "" + e);
        }
    }

    public void insertDataSubCaseType(SubCaseTypeTable subCaseTypeTable) {
        Dao<SubCaseTypeTable, Integer> subCaseTypeTables;
        try {
            subCaseTypeTables = getHelper(getApplicationContext()).getSubCaseTypeTableDao();
            subCaseTypeTables.create(subCaseTypeTable);
            Log.e("sub case type Table", "inserted");
        } catch (SQLException e) {
            Log.e("sub case type table", "" + e);
        }
    }

    public List<CourtTable> getAllCourt() {
        Dao<CourtTable, Integer> courtTableDao;
        try {
            courtTableDao = getHelper(getApplicationContext()).getCourtTableDao();
            return courtTableDao.queryForAll();
        } catch (SQLException e) {
            return null;
        }
    }

    ArrayList<CourtData> courtList;

    public void convertList(List<CourtTable> list) {
        if (list == null)
            return;

        if (courtList == null)
            courtList = new ArrayList<>();
        courtList.clear();
        for (int i = 0; i < list.size(); i++) {
            CaseState state = null;
            CaseDistrict district = null;
            CaseSubDistrict subDistrict = null;

            CourtTable courtTable = list.get(i);
            if (courtTable.getCourtState() != null) {
                state = new CaseState(courtTable.getCourtState().getName(), courtTable.getCourtState().getId(), courtTable.getCourtState().getParent_id(),
                        courtTable.getCourtState().getExternal_id(), courtTable.getCourtState().getLocation_type(), courtTable.getCourtState().getPin());
            }
            if (courtTable.getCourtDistrict() != null) {
                district = new CaseDistrict(courtTable.getCourtDistrict().getName(), courtTable.getCourtDistrict().getId(), courtTable.getCourtDistrict().getParent_id(),
                        courtTable.getCourtDistrict().getExternal_id(), courtTable.getCourtDistrict().getLocation_type(), courtTable.getCourtDistrict().getPin());
            }
            if (courtTable.getCourtSubDistrict() != null) {
                subDistrict = new CaseSubDistrict(courtTable.getCourtSubDistrict().getName(), courtTable.getCourtSubDistrict().getId(), courtTable.getCourtSubDistrict().getParent_id(),
                        courtTable.getCourtSubDistrict().getExternal_id(), courtTable.getCourtSubDistrict().getLocation_type(), courtTable.getCourtSubDistrict().getPin());
            }

            CourtData courtData = new CourtData(courtTable.getCourt_id(), courtTable.getCourt_name(), courtTable.getCourt_type(), courtTable.getCourt_number(),
                    courtTable.getState_id(), courtTable.getDistrict_id(), courtTable.getSub_district_id(),
                    state, district, subDistrict);

            courtList.add(courtData);
        }
        selectCourt();
    }


    PopupWindow selectCourtWindow;

    public void selectCourt() {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.simple_list_item, null);

        selectCourtWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            selectCourtWindow.setElevation(5.0f);
        }

        selectCourtWindow.setOutsideTouchable(true);
        selectCourtWindow.setFocusable(true);
        TextView txtName = (TextView) customView.findViewById(R.id.textViewName);
        RecyclerView recyclerView = (RecyclerView) customView.findViewById(R.id.caseSubTypeRecycleView);
        txtName.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);/*
        recyclerView.setBackgroundColor(Color.parseColor("#ffffff"));*/
        CourtListAdapter courtListAdapter = new CourtListAdapter(getApplicationContext(), courtList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courtListAdapter);
        selectCourtWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#99777777")));
        selectCourtWindow.showAtLocation(edtSelectCourt, Gravity.CENTER, 0, 0);
    }

    DatabaseHelper databaseHelper;

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void releaseHelper() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseHelper();
    }

    public class CourtListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<CourtData> courtList;
        Context context;

        public CourtListAdapter(Context context, ArrayList<CourtData> courtList) {
            this.courtList = courtList;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return courtList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
            return new CourtViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CourtViewHolder) {
                CourtViewHolder courtViewHolder = (CourtViewHolder) holder;
                courtViewHolder.txtCourtName.setText(courtList.get(position).getCourt_name());
                courtViewHolder.txtStateName.setText(getAddress(courtList.get(position)));

            }
        }

        public String getAddress(CourtData aCaseDetail) {
            String address = "";
            if (aCaseDetail.getSubdistrict() != null && aCaseDetail.getSubdistrict().getName() != null) {
                address = aCaseDetail.getSubdistrict().getName() + ", ";
            }


            if (aCaseDetail.getDistrict() != null && aCaseDetail.getDistrict().getName() != null) {
                address = address + aCaseDetail.getDistrict().getName() + ", ";
            }


            if (aCaseDetail.getState() != null && aCaseDetail.getState().getName() != null) {
                address = address + aCaseDetail.getState().getName();
            }
            return address;
        }

        public class CourtViewHolder extends RecyclerView.ViewHolder {
            TextView txtCourtName, txtStateName;

            public CourtViewHolder(View itemView) {
                super(itemView);
                txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
                txtStateName = (TextView) itemView.findViewById(R.id.textViewState);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedCourt(courtList.get(getAdapterPosition()));
                        selectCourtWindow.dismiss();
                    }
                });
            }
        }
    }

    CourtData selectedCourt;

    public void selectedCourt(CourtData data) {
        selectedCourt = data;
        edtSelectCourt.setText(data.getCourt_name());
    }

    public List<BaseClientTable> getLocalData() {
        Dao<BaseClientTable, Integer> baseClientTables;
        try {
            baseClientTables = getHelper(getApplicationContext()).getBaseClientTableDao();
            return baseClientTables.queryForAll();
        } catch (SQLException e) {
            return null;
        }
    }


    Client selectedClient;

    public void selectedClient(Client client) {
        selectedClient = client;
        txtSelectClient.setText(client.getClient().getFirst_name() + " " + client.getClient().getLast_name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            if (bundle.getSerializable("CaseType") != null) {
                selectedCaseType = (CasesTypeData) bundle.getSerializable("CaseType");
                selectedCaseSubType = (CasesSubType) bundle.getSerializable("SubCaseType");
                edtCaseType.setText(selectedCaseSubType.getSubcase_type_name());
            } else if (bundle.getSerializable("Client") != null) {
                selectedClient((Client) bundle.getSerializable("Client"));
            } else if (bundle.getSerializable("Court") != null) {
                selectedCourt((CourtData) bundle.getSerializable("Court"));
            }
        }

    }
}
