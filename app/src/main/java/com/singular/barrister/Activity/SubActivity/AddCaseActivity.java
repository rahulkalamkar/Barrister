package com.singular.barrister.Activity.SubActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.Client.ClientTable;
import com.singular.barrister.Database.Tables.CourtTable;
import com.singular.barrister.Interface.CaseListeners;
import com.singular.barrister.Interface.RecycleItem;
import com.singular.barrister.Model.Cases.CaseDistrict;
import com.singular.barrister.Model.Cases.CaseState;
import com.singular.barrister.Model.Cases.CaseSubDistrict;
import com.singular.barrister.Model.CasesSubType;
import com.singular.barrister.Model.CasesTypeData;
import com.singular.barrister.Model.CasesTypeResponse;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Client.ClientDetail;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.DatePickerWindow;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
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
                selectCaseStatus();
            }
        });

        edtNextHearingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerWindow datePickerWindow = new DatePickerWindow(getApplicationContext(), edtNextHearingDate, AddCaseActivity.this);
            datePickerWindow.showTimer(true);
            }
        });

        edtSelectCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CourtTable> list = getAllCourt();
                convertList(list);
            }
        });
        retrofitManager = new RetrofitManager();
        edtCaseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                    retrofitManager.getCourtType(AddCaseActivity.this, new UserPreferance(getApplicationContext()).getToken());
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtSelectClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchAndDisplayClient();
            }
        });

    }

    RetrofitManager retrofitManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_done, menu);
        return true;
    }

    PopupWindow changeStatusWindow;

    public void selectCaseStatus() {

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

        changeStatusWindow.setOutsideTouchable(true);
        changeStatusWindow.setFocusable(true);
        final RadioButton radioButtonCompleted = (RadioButton) customView.findViewById(R.id.radioButton2);
        final RadioButton radioButtonInProgress = (RadioButton) customView.findViewById(R.id.radioButton1);

        if (edtCaseStatus.getText().toString() != null && edtCaseStatus.getText().toString().equalsIgnoreCase("completed")) {
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
                    edtCaseStatus.setText("completed");
                } else {
                    edtCaseStatus.setText("In-Progress");
                }
                changeStatusWindow.dismiss();
            }
        });
        changeStatusWindow.showAtLocation(edtCaseStatus, Gravity.CENTER, 0, 0);
    }

    @Override
    public void dateTime(String date) {
        edtNextHearingDate.setText(date);
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

    PopupWindow caseTypeWindow;

    public void selectCaseType() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.activity_select_state, null);

        caseTypeWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            caseTypeWindow.setElevation(5.0f);
        }
        LinearLayout linearLayout = (LinearLayout) customView.findViewById(R.id.layout);
        linearLayout.setBackgroundColor(Color.parseColor("#99777777"));
        caseTypeWindow.setOutsideTouchable(true);
        caseTypeWindow.setFocusable(true);

        RecyclerView recyclerView = (RecyclerView) customView.findViewById(R.id.recycleView);
        SimpleRecycleAdapter simpleRecycleAdapter = new SimpleRecycleAdapter(getApplicationContext(), caseTypeDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(simpleRecycleAdapter);

        caseTypeWindow.showAtLocation(edtCaseType, Gravity.CENTER, 0, 0);
    }

    public class SimpleRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<CasesTypeData> list;

        public SimpleRecycleAdapter(Context context, ArrayList<CasesTypeData> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);
            return new SimpleRecycleAdapter.SimpleViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SimpleRecycleAdapter.SimpleViewHolder) {
                SimpleRecycleAdapter.SimpleViewHolder simpleViewHolder = (SimpleRecycleAdapter.SimpleViewHolder) holder;
                simpleViewHolder.txtName.setText(list.get(position).getCase_type_name());

                SimpleSubListAdapter simpleSubListAdapter = new SimpleSubListAdapter(context, list.get(position).getSubCaseData());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                simpleViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
                simpleViewHolder.recyclerView.setAdapter(simpleSubListAdapter);
                // simpleViewHolder.recyclerView.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            TextView txtName;
            View viewHorizontal;
            RecyclerView recyclerView;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                viewHorizontal = (View) itemView.findViewById(R.id.horizontalView);
                viewHorizontal.setVisibility(View.GONE);
                txtName = (TextView) itemView.findViewById(R.id.textViewName);
                txtName.setBackgroundColor(Color.parseColor("#99777777"));

                recyclerView = (RecyclerView) itemView.findViewById(R.id.caseSubTypeRecycleView);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void selectedCaseType(String aCaseType) {
        edtCaseType.setText(aCaseType);
    }

    public class SimpleSubListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<CasesSubType> casesSubTypes;

        public SimpleSubListAdapter(Context context, ArrayList<CasesSubType> casesSubTypes) {
            this.context = context;
            this.casesSubTypes = casesSubTypes;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SimpleViewHolder) {
                SimpleViewHolder simpleViewHolder = (SimpleViewHolder) holder;
                simpleViewHolder.txtSubTypeName.setText(casesSubTypes.get(position).getSubcase_type_name());
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);
            return new SimpleViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return casesSubTypes.size();
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            TextView txtName, txtSubTypeName;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                txtName = (TextView) itemView.findViewById(R.id.textViewName);
                txtSubTypeName = (TextView) itemView.findViewById(R.id.textViewSubName);
                txtName.setVisibility(View.GONE);
                txtSubTypeName.setVisibility(View.VISIBLE);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedCaseType(casesSubTypes.get(getAdapterPosition()).getSubcase_type_name());
                        caseTypeWindow.dismiss();
                    }
                });
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
                selectCaseType();
            }
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

    public void selectedCourt(CourtData data) {
        edtSelectCourt.setText(data.getCourt_name());
    }

    public void fetchAndDisplayClient()
    {
        List<BaseClientTable> list = getLocalData();
        if(list!=null)
            convertAndDisplay(list);
    }

    ArrayList<Client> clientList;
    public void convertAndDisplay(List<BaseClientTable> list) {
        if(clientList==null)
            clientList=new ArrayList<Client>();
        clientList.clear();

        for (int i = 0; i < list.size(); i++) {
            BaseClientTable baseClientTable=list.get(i);

            ClientTable clientTable=baseClientTable.getClientTable();
            ClientDetail clientDetail = new ClientDetail(clientTable.getClient_id(), clientTable.getFirst_name(), clientTable.getLast_name(),
                    clientTable.getCountry_code(), clientTable.getMobile(), clientTable.getEmail(),
                    clientTable.getAddress(), clientTable.getUser_type(), clientTable.getReferral_code(),
                    clientTable.getParent_user_id(), clientTable.getUsed_referral_code(), clientTable.getDevice_type(),
                    clientTable.getDevice_token(), clientTable.getSubscription(), clientTable.getCreated_at(), clientTable.getUpdated_at());

            Client client=new Client(baseClientTable.getBase_id(),baseClientTable.getClient_id(),baseClientTable.getCreated_at(),clientDetail);
            clientList.add(client);
        }
        selectClient();

    }

    PopupWindow selectClientWindow;

    public void selectClient() {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.simple_list_item, null);

        selectClientWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            selectClientWindow.setElevation(5.0f);
        }
        selectClientWindow.setOutsideTouchable(true);
        selectClientWindow.setFocusable(true);
        selectClientWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#99777777")));
        TextView txtName = (TextView) customView.findViewById(R.id.textViewName);
        RecyclerView recyclerView = (RecyclerView) customView.findViewById(R.id.caseSubTypeRecycleView);
        txtName.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        ClientListAdapter clientListAdapter = new ClientListAdapter(getApplicationContext(), clientList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(clientListAdapter);

        selectClientWindow.showAtLocation(edtSelectCourt, Gravity.CENTER, 0, 0);
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


    public class ClientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<Client> clientList;
        Context context;

        public ClientListAdapter(Context context, ArrayList<Client> clientList) {
            this.clientList = clientList;
            this.context = context;
        }


        @Override
        public int getItemCount() {
            return clientList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
            return new ClientListAdapter.ClientViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ClientListAdapter.ClientViewHolder) {
                ClientListAdapter.ClientViewHolder clientViewHolder = (ClientListAdapter.ClientViewHolder) holder;
                clientViewHolder.txtCourtName.setText(clientList.get(position).getClient().getFirst_name() + " " +
                        clientList.get(position).getClient().getLast_name());
                clientViewHolder.txtStateName.setText("+" + clientList.get(position).getClient().getCountry_code() + " " +
                        clientList.get(position).getClient().getMobile());
            }
        }

        public class ClientViewHolder extends RecyclerView.ViewHolder {
            public TextView txtCourtName, txtStateName, txtDelete;
            public RelativeLayout bgLayout;
            public LinearLayout fgLayout;

            public ClientViewHolder(View itemView) {
                super(itemView);
                txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
                txtStateName = (TextView) itemView.findViewById(R.id.textViewState);
                bgLayout = (RelativeLayout) itemView.findViewById(R.id.view_background);
                fgLayout = (LinearLayout) itemView.findViewById(R.id.view_foreground);
                txtDelete = (TextView) itemView.findViewById(R.id.textViewDelete);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedClient(clientList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    public void selectedClient(Client client)
    {
        txtSelectClient.setText(client.getClient().getFirst_name() +" "+client.getClient().getLast_name() +"\n"+client.getClient().getMobile());
        selectClientWindow.dismiss();
    }

}
