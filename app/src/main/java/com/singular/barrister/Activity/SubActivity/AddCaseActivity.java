package com.singular.barrister.Activity.SubActivity;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.DisplayCaseActivity;
import com.singular.barrister.Interface.CaseListeners;
import com.singular.barrister.Model.Cases.CaseType;
import com.singular.barrister.Model.CasesSubType;
import com.singular.barrister.Model.CasesTypeData;
import com.singular.barrister.Model.CasesTypeResponse;
import com.singular.barrister.Model.CasesTypes;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.DatePickerWindow;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.util.ArrayList;

public class AddCaseActivity extends AppCompatActivity implements CaseListeners,IDataChangeListener<IModel> {

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

        caseTypeDataList=new ArrayList<CasesTypeData>();
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
            }
        });
        retrofitManager=new RetrofitManager();
        edtCaseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new NetworkConnection(getApplicationContext()).isNetworkAvailable())
                {
                    retrofitManager.getCourtType(AddCaseActivity.this,new UserPreferance(getApplicationContext()).getToken());
                }
                else
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.network_error),Toast.LENGTH_SHORT).show();
                }
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
    public void selectCaseType()
    {
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

                SimpleSubListAdapter simpleSubListAdapter=new SimpleSubListAdapter(context,list.get(position).getSubCaseData());
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
                viewHorizontal=(View) itemView.findViewById(R.id.horizontalView);
                viewHorizontal.setVisibility(View.GONE);
                txtName = (TextView) itemView.findViewById(R.id.textViewName);
                txtName.setBackgroundColor(Color.parseColor("#99777777"));

                recyclerView=(RecyclerView)itemView.findViewById(R.id.caseSubTypeRecycleView);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void selectedCaseType(String aCaseType)
    {
        edtCaseType.setText(aCaseType);
    }

    public class SimpleSubListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        Context context;
        ArrayList<CasesSubType> casesSubTypes ;
        public SimpleSubListAdapter(Context context,ArrayList<CasesSubType> casesSubTypes)
        {
            this.context=context;
            this.casesSubTypes=casesSubTypes;
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
            TextView txtName,txtSubTypeName;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                txtName = (TextView) itemView.findViewById(R.id.textViewName);
                txtSubTypeName=(TextView)itemView.findViewById(R.id.textViewSubName);
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
        if(response != null && response instanceof CasesTypeResponse)
        {
            CasesTypeResponse casesTypeResponse=(CasesTypeResponse) response;
            if(casesTypeResponse.getData().getCasetype() !=null)
            {
                caseTypeDataList.addAll(casesTypeResponse.getData().getCasetype());
                selectCaseType();
            }
        }
    }
}
