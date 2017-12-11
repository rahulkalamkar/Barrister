package com.singular.barrister.Activity.SubActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Adapter.SelectStateActivity;
import com.singular.barrister.Model.Cases.CaseDistrict;
import com.singular.barrister.Model.Cases.CaseState;
import com.singular.barrister.Model.Cases.CaseSubDistrict;
import com.singular.barrister.Model.CasesTypeResponse;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Model.District;
import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.Model.State;
import com.singular.barrister.Model.States.StateResponse;
import com.singular.barrister.Model.SubDistrict;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.io.Serializable;
import java.util.ArrayList;

public class EditCourtActivity extends AppCompatActivity implements IDataChangeListener<IModel> {
    TextInputLayout txtIPCourtName, txtIPCourtNumber, txtIPCourtType, txtIPState, txtIPDistrict, txtIPSubDistrict;
    TextInputEditText edtCourtName, edtCourtNumber, edtCourtType, edtState, edtDistrict, edtSubDistrict;
    RetrofitManager retrofitManager;
    State selectedState;
    District selectedDistrict;
    SubDistrict selectedSubDistrict;
    ArrayList<State> stateList;
    ArrayList<District> districtList;
    ArrayList<SubDistrict> subDistrictList;
    ArrayList<String> courtTypeList;

    String courtId;
    CourtData aCourtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_court);

        if (getActionBar() != null) {
            getActionBar().setTitle("Edit Court");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit court");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        aCourtData = (CourtData) getIntent().getExtras().getSerializable("Court");
        courtId = aCourtData.getId();

        selectedState = convertCaseState(aCourtData.getState());
        selectedDistrict = convertCaseDistrict(aCourtData.getDistrict());
        selectedSubDistrict = convertCaseSubDistrict(aCourtData.getSubdistrict());

        initView();
        retrofitManager = new RetrofitManager();
        setData();
        getCourtType();
    }

    public State convertCaseState(CaseState caseState) {
        if (caseState == null)
            return null;
        else
            return new State(caseState.getName(),
                    caseState.getId(),
                    caseState.getParent_id(),
                    caseState.getExternal_id(),
                    caseState.getLocation_type(),
                    caseState.getPin());
    }

    public District convertCaseDistrict(CaseDistrict caseDistrict) {
        if (caseDistrict == null)
            return null;
        else
            return new District(caseDistrict.getName(),
                    caseDistrict.getId(),
                    caseDistrict.getParent_id(),
                    caseDistrict.getExternal_id(),
                    caseDistrict.getLocation_type(),
                    caseDistrict.getPin());
    }

    public SubDistrict convertCaseSubDistrict(CaseSubDistrict caseSubDistrict) {
        if (caseSubDistrict == null)
            return null;
        else
            return new SubDistrict(caseSubDistrict.getName(),
                    caseSubDistrict.getId(),
                    caseSubDistrict.getParent_id(),
                    caseSubDistrict.getExternal_id(),
                    caseSubDistrict.getLocation_type(),
                    caseSubDistrict.getPin());
    }

    public void setData() {
        edtCourtName.setText(aCourtData.getCourt_name());
        edtCourtNumber.setText(aCourtData.getCourt_number());
        edtCourtType.setText(aCourtData.getCourt_type());
        edtSubDistrict.setText(getSubDistrict());
        edtDistrict.setText(getDistrict());
        edtState.setText(getState());
    }

    public String getState() {
        if (aCourtData.getState() != null && aCourtData.getState().getName() != null) {
            return aCourtData.getState().getName();
        } else
            return "";
    }

    public String getDistrict() {
        if (aCourtData.getDistrict() != null && aCourtData.getDistrict().getName() != null) {
            return aCourtData.getDistrict().getName();
        } else
            return "";
    }

    public String getSubDistrict() {
        if (aCourtData.getSubdistrict() != null && aCourtData.getSubdistrict().getName() != null) {
            return aCourtData.getSubdistrict().getName();
        } else
            return "";
    }

    ProgressBar mProgressBar;

    public void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        txtIPCourtName = (TextInputLayout) findViewById(R.id.editTextCourtNameError);
        txtIPCourtNumber = (TextInputLayout) findViewById(R.id.editTextCourtNumberError);
        txtIPCourtType = (TextInputLayout) findViewById(R.id.editTextCourtTypeError);
        txtIPState = (TextInputLayout) findViewById(R.id.editTextCourtStateError);
        txtIPDistrict = (TextInputLayout) findViewById(R.id.editTextCourtDistrictError);
        txtIPSubDistrict = (TextInputLayout) findViewById(R.id.editTextCourtSubDistrictError);

        edtCourtName = (TextInputEditText) findViewById(R.id.editTextCourtName);
        edtCourtNumber = (TextInputEditText) findViewById(R.id.editTextCourtNumber);
        edtCourtType = (TextInputEditText) findViewById(R.id.editTextCourtType);
        edtState = (TextInputEditText) findViewById(R.id.editTextCourtState);
        edtDistrict = (TextInputEditText) findViewById(R.id.editTextCourtDistrict);
        edtSubDistrict = (TextInputEditText) findViewById(R.id.editTextCourtSubDistrict);

        edtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateList != null) {
                    if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("State", stateList);
                        Intent intent = new Intent(getApplicationContext(), SelectStateActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    getStateList();
                }
            }
        });

        edtDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedState != null) {
                    if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                        selectedSubDistrict = null;
                        edtSubDistrict.setText("");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("StateId", selectedState.getId());
                        Intent intent = new Intent(getApplicationContext(), SelectDistrictActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 2);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showOtherError();
                    txtIPState.setError("Enter State");
                }
            }
        });

        edtSubDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedDistrict != null) {
                    if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DistrictId", selectedDistrict.getId());
                        Intent intent = new Intent(getApplicationContext(), SelectSubDistrictActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 3);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showOtherError();
                    txtIPDistrict.setError("Enter District");
                }
            }
        });

        edtCourtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCourtType(edtCourtType);
            }
        });
    }

    public void getCourtType() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            retrofitManager.getCourtType(this, new UserPreferance(getApplicationContext()).getToken());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void getStateList() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            retrofitManager.getState(this, new UserPreferance(getApplicationContext()).getToken());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void returnDataToHome(int resultCode) {
        Intent returnIntent = getIntent();
        Bundle bundle = new Bundle();
        returnIntent.putExtra("result", resultCode);
        bundle.putSerializable("NewCourt", (Serializable) getCourt());
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public CourtData getCourt() {
        CourtData courtData = new CourtData();
        courtData.setCourt_name(edtCourtName.getText().toString());
        courtData.setCourt_number(edtCourtNumber.getText().toString());
        courtData.setCourt_type(edtCourtType.getText().toString());
        courtData.setState(new CaseState(selectedState.getName(),
                selectedState.getId(),
                selectedState.getParent_id(),
                selectedState.getExternal_id(),
                selectedState.getLocation_type(),
                selectedState.getPin()));
        courtData.setDistrict(new CaseDistrict(selectedDistrict.getName(),
                selectedDistrict.getId(),
                selectedDistrict.getParent_id(),
                selectedDistrict.getExternal_id(),
                selectedDistrict.getLocation_type(),
                selectedDistrict.getPin()));
        if (selectedSubDistrict != null) {
            courtData.setSubdistrict(new CaseSubDistrict(selectedSubDistrict.getName(),
                    selectedSubDistrict.getId(),
                    selectedSubDistrict.getParent_id(),
                    selectedSubDistrict.getExternal_id(),
                    selectedSubDistrict.getLocation_type(),
                    selectedSubDistrict.getPin()));
        } else {
            courtData.setSubdistrict(null);
        }
        return courtData;
    }

    public void checkValues() {
        if (TextUtils.isEmpty(edtCourtName.getText().toString())) {
            showOtherError();
            txtIPCourtName.setError("Enter court name");
        } else if (TextUtils.isEmpty(edtCourtNumber.getText().toString())) {
            showOtherError();
            txtIPCourtNumber.setError("Enter court number");
        } else if (TextUtils.isEmpty(edtCourtType.getText().toString())) {
            showOtherError();
            txtIPCourtType.setError("Enter court type");
        } else if (TextUtils.isEmpty(edtState.getText().toString())) {
            showOtherError();
            txtIPState.setError("Enter State");
        } else if (TextUtils.isEmpty(edtDistrict.getText().toString())) {
            showOtherError();
            txtIPDistrict.setError("Enter District");
        } else {
            showOtherError();
            saveCourt(edtCourtName.getText().toString(),
                    edtCourtNumber.getText().toString(),
                    edtCourtType.getText().toString(),
                    selectedState.getId(),
                    selectedDistrict.getId(),
                    selectedSubDistrict != null ? selectedSubDistrict.getId() : "");
        }
    }

    boolean isUpdated = false;

    public void saveCourt(String courtName, String courtNumber, String courtType, String stateName, String districtName, String subDistrictName) {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            isUpdated = true;
            mProgressBar.setVisibility(View.VISIBLE);
            retrofitManager.editCourt(this, new UserPreferance(getApplicationContext()).getToken(), courtName, courtNumber, courtType, stateName, districtName, subDistrictName, courtId);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }

    }

    public void showOtherError() {
        txtIPCourtName.setError(null);
        txtIPCourtNumber.setError(null);
        txtIPCourtType.setError(null);
        txtIPState.setError(null);
        txtIPDistrict.setError(null);
        txtIPSubDistrict.setError(null);
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
                checkValues();
                break;
        }
        return true;
    }


    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof StateResponse) {
            StateResponse stateResponse = (StateResponse) response;
            if (stateResponse.getData().getState() != null) {
                stateList = new ArrayList<State>();
                stateList.addAll(stateResponse.getData().getState());
            } else if (stateResponse.getData().getDistrict() != null) {
                districtList = new ArrayList<District>();
                districtList.addAll(stateResponse.getData().getDistrict());
            } else if (stateResponse.getData().getSubdistrict() != null) {
                subDistrictList = new ArrayList<SubDistrict>();
                subDistrictList.addAll(stateResponse.getData().getSubdistrict());
            }
        } else if (response != null && response instanceof SimpleMessageResponse) {
            SimpleMessageResponse simpleMessageResponse = (SimpleMessageResponse) response;
            if (simpleMessageResponse.getId() != null) {
                returnDataToHome(1);
            } else {
                Toast.makeText(this, "OOPS! something went wrong", Toast.LENGTH_SHORT).show();
            }

            if (isUpdated)
                returnDataToHome(1);
        } else if (response != null && response instanceof CasesTypeResponse) {
            CasesTypeResponse casesTypeResponse = (CasesTypeResponse) response;
            courtTypeList = new ArrayList<String>();
            if (casesTypeResponse.getData().getCourttype() != null) {
                courtTypeList.addAll(casesTypeResponse.getData().getCourttype());
            }
            getStateList();
        } else {
            if (isUpdated)
                returnDataToHome(1);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            selectedState = (State) data.getExtras().getSerializable("State");
            edtState.setText(selectedState.getName());
            selectedDistrict = null;
            selectedSubDistrict = null;
            edtDistrict.setText("");
            edtSubDistrict.setText("");
        } else if (resultCode == 2) {
            selectedDistrict = (District) data.getExtras().getSerializable("District");
            edtDistrict.setText(selectedDistrict.getName());
        } else if (resultCode == 3) {
            selectedSubDistrict = (SubDistrict) data.getExtras().getSerializable("SubDistrict");
            edtSubDistrict.setText(selectedSubDistrict.getName());
        } else if (data != null && data.getExtras() != null) {
            if (data.getExtras().getSerializable("State") != null) {
                selectedState = (State) data.getExtras().getSerializable("State");
                edtState.setText(selectedState.getName());
            } else if (data.getExtras().getSerializable("District") != null) {
                selectedDistrict = (District) data.getExtras().getSerializable("District");
                edtDistrict.setText(selectedDistrict.getName());
            } else if (data.getExtras().getSerializable("SubDistrict") != null) {
                selectedSubDistrict = (SubDistrict) data.getExtras().getSerializable("SubDistrict");
                edtSubDistrict.setText(selectedSubDistrict.getName());
            }
        }
        showOtherError();
    }

    PopupWindow courtTypeWindow;

    public void selectCourtType(View layout) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.activity_select_state, null);

        courtTypeWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            courtTypeWindow.setElevation(5.0f);
        }
        LinearLayout linearLayout = (LinearLayout) customView.findViewById(R.id.layout);
        linearLayout.setBackgroundColor(Color.parseColor("#99777777"));
        RecyclerView recyclerView = (RecyclerView) customView.findViewById(R.id.recycleView);
        SimpleRecycleAdapter simpleRecycleAdapter = new SimpleRecycleAdapter(getApplicationContext(), courtTypeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(simpleRecycleAdapter);

        courtTypeWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    public class SimpleRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<String> list;

        public SimpleRecycleAdapter(Context context, ArrayList<String> list) {
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
                simpleViewHolder.txtName.setText(list.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            TextView txtName;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                txtName = (TextView) itemView.findViewById(R.id.textViewName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedCourtType(list.get(getAdapterPosition()));
                        courtTypeWindow.dismiss();
                    }
                });
            }
        }
    }

    public void selectedCourtType(String name) {
        edtCourtType.setText(name);
    }
}
