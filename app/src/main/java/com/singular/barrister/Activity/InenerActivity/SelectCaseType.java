package com.singular.barrister.Activity.InenerActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Activity.SubActivity.AddCaseActivity;
import com.singular.barrister.Adapter.CasesListAdapter;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.CaseTypeTable;
import com.singular.barrister.Database.Tables.SubCaseTypeTable;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.CasesSubType;
import com.singular.barrister.Model.CasesTypeData;
import com.singular.barrister.Model.CasesTypeResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.API;
import com.singular.barrister.RetrofitManager.APIClient;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCaseType extends AppCompatActivity implements IDataChangeListener<IModel> {

    ArrayList<CasesTypeData> caseTypeDataList;
    RetrofitManager retrofitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_case_type);

        if (getActionBar() != null) {
            getActionBar().setTitle("Select CaseType");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select CaseType");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        caseTypeDataList = new ArrayList<CasesTypeData>();
        retrofitManager = new RetrofitManager();
        if (caseTypeDataList == null)
            caseTypeDataList = new ArrayList<CasesTypeData>();
        caseTypeDataList.addAll(convertListToCaseType());
        if (caseTypeDataList != null && caseTypeDataList.size() > 0) {
            showList();
        } else if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            retrofitManager.getCourtType(SelectCaseType.this, new UserPreferance(getApplicationContext()).getToken());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();


        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                simpleRecycleAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }*/
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
          case android.R.id.home:
              finish();
              break;
      }

      return true;
  }
    SimpleRecycleAdapter simpleRecycleAdapter;

    public void showList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        simpleRecycleAdapter = new SimpleRecycleAdapter(getApplicationContext(), caseTypeDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(simpleRecycleAdapter);
    }

    public List<CasesTypeData> convertListToCaseType() {
        List<CasesTypeData> casesTypeDatas = null;
        if (casesTypeDatas == null)
            casesTypeDatas = new ArrayList<CasesTypeData>();
        casesTypeDatas.clear();

        List<CaseTypeTable> caseTypeTablesList = checkValuesForCaseTypeLocally();
        if (caseTypeTablesList != null) {
            for (int i = 0; i < caseTypeTablesList.size(); i++) {
                CaseTypeTable caseTypeTable = caseTypeTablesList.get(i);
                List<SubCaseTypeTable> subCaseList = getSubCaseTypeTableValue(caseTypeTable.getCase_type_id());
                CasesTypeData casesTypeData = getCasesTypeData(caseTypeTable, subCaseList);
                casesTypeDatas.add(casesTypeData);
            }
        }
        return casesTypeDatas;
    }

    public CasesTypeData getCasesTypeData(CaseTypeTable caseTypeTable, List<SubCaseTypeTable> subCaseList) {
        CasesTypeData casesTypeData1 = new CasesTypeData(caseTypeTable.getCase_type_id(), caseTypeTable.getCase_type_name(), convertListToCasesSubType(subCaseList));
        return casesTypeData1;
    }

    public ArrayList<CasesSubType> convertListToCasesSubType(List<SubCaseTypeTable> subCaseList) {
        ArrayList<CasesSubType> casesSubTypes = null;
        if (casesSubTypes == null)
            casesSubTypes = new ArrayList<CasesSubType>();
        casesSubTypes.clear();
        for (int i = 0; i < subCaseList.size(); i++) {
            SubCaseTypeTable subCaseTypeTable = subCaseList.get(i);
            CasesSubType casesSubType = new CasesSubType(subCaseTypeTable.getSub_case_type_id(), subCaseTypeTable.getSub_case_type_name());
            casesSubTypes.add(casesSubType);
        }
        return casesSubTypes;
    }

    public List<SubCaseTypeTable> getSubCaseTypeTableValue(String caseTypeId) {
        Dao<SubCaseTypeTable, Integer> subCaseTypeTables;
        try {
            subCaseTypeTables = getHelper(getApplicationContext()).getSubCaseTypeTableDao();
            return subCaseTypeTables.queryForEq("case_type_id", caseTypeId);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<CaseTypeTable> checkValuesForCaseTypeLocally() {
        Dao<CaseTypeTable, Integer> caseTypeTablesDao;
        try {
            caseTypeTablesDao = getHelper(getApplicationContext()).getCaseTypeTableDao();
            return caseTypeTablesDao.queryForAll();
        } catch (SQLException e) {
            return null;
        }
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

    public void getCourtType(final IDataChangeListener<IModel> callbackListener, String token) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);
        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/casetype");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<CasesTypeResponse> call = api.getCaseType(apiName, headerMap);
            call.enqueue(new Callback<CasesTypeResponse>() {
                @Override
                public void onResponse(Call<CasesTypeResponse> call, Response<CasesTypeResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<CasesTypeResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof CasesTypeResponse) {
            CasesTypeResponse casesTypeResponse = (CasesTypeResponse) response;
            if (casesTypeResponse.getData().getCasetype() != null) {
                caseTypeDataList.addAll(casesTypeResponse.getData().getCasetype());
                saveCaseType(caseTypeDataList);
                showList();
            }
        }
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

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    public class SimpleRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        Context context;
        ArrayList<CasesTypeData> list;
        ArrayList<CasesTypeData> arrayList;

        public SimpleRecycleAdapter(Context context, ArrayList<CasesTypeData> list) {
            this.context = context;
            this.list = list;
            arrayList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);
            return new SimpleRecycleAdapter.SimpleViewHolder(v);
        }

        SimpleSubListAdapter simpleSubListAdapter;

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SimpleRecycleAdapter.SimpleViewHolder) {
                SimpleRecycleAdapter.SimpleViewHolder simpleViewHolder = (SimpleRecycleAdapter.SimpleViewHolder) holder;
                simpleViewHolder.txtName.setText(list.get(position).getCase_type_name());

                simpleSubListAdapter = new SimpleSubListAdapter(context, list.get(position).getSubCaseData());
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

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        ValueFilter valueFilter;

        int count = 0;

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (list == null)
                    return null;

                if (charString.isEmpty()) {

                    list = arrayList;
                } else {
                    if (count > charString.length() && list.size() == 0) {
                        list = arrayList;
                    }
                    count++;
                    ArrayList<CasesTypeData> filteredList = new ArrayList<>();

                    for (CasesTypeData cases : list) {
                        if (cases.getCase_type_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(cases);
                        }
                    }

                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results != null) {
                    //list = (ArrayList<CasesTypeData>) results.values;
                    list = arrayList;
                    notifyDataSetChanged();

                    for (CasesTypeData casesTypeData : arrayList) {
                        simpleSubListAdapter.getFilter().filter(constraint);
                    }
                }
            }

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

    public ArrayList<CasesSubType> checkSubCaseType(ArrayList<CasesSubType> casesSubTypes, String charString) {
        ArrayList<CasesSubType> filteredList = new ArrayList<>();
        for (CasesSubType cases : casesSubTypes) {
            if (cases.getSubcase_type_name().toLowerCase().contains(charString.toLowerCase())) {
                filteredList.add(cases);
            }
        }
        return filteredList;
    }

    public class SimpleSubListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        Context context;
        ArrayList<CasesSubType> casesSubTypes;
        ArrayList<CasesSubType> arrayList;

        public SimpleSubListAdapter(Context context, ArrayList<CasesSubType> casesSubTypes) {
            this.context = context;
            this.casesSubTypes = casesSubTypes;
            arrayList = casesSubTypes;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SimpleSubListAdapter.SimpleViewHolder) {
                SimpleSubListAdapter.SimpleViewHolder simpleViewHolder = (SimpleSubListAdapter.SimpleViewHolder) holder;
                simpleViewHolder.txtSubTypeName.setText(casesSubTypes.get(position).getSubcase_type_name());
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);
            return new SimpleSubListAdapter.SimpleViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return casesSubTypes.size();
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        ValueFilter valueFilter;

        int count = 0;

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (casesSubTypes == null)
                    return null;

                if (charString.isEmpty()) {

                    casesSubTypes = arrayList;
                } else {
                    if (count > charString.length() && casesSubTypes.size() == 0) {
                        casesSubTypes = arrayList;
                    }
                    count++;
                    ArrayList<CasesSubType> filteredList = new ArrayList<>();

                    for (CasesSubType cases : casesSubTypes) {
                        if (cases.getSubcase_type_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(cases);
                        }
                    }

                    casesSubTypes = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = casesSubTypes;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results != null) {
                    casesSubTypes = (ArrayList<CasesSubType>) results.values;
                    notifyDataSetChanged();
                }
            }

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
                        returnData(casesSubTypes.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    public void returnData(CasesSubType casesSubType) {
        selectedCaseSubType = casesSubType;
        getSelectedCaseType(casesSubType);
        Bundle bundle = new Bundle();
        bundle.putSerializable("CaseType", (Serializable) selectedCaseType);
        bundle.putSerializable("SubCaseType", (Serializable) selectedCaseSubType);
        Intent returnIntent = getIntent();
        returnIntent.putExtra("result", 11);
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    CasesSubType selectedCaseSubType;
    CasesTypeData selectedCaseType;

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
}
