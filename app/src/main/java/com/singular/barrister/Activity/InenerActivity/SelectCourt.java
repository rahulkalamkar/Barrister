package com.singular.barrister.Activity.InenerActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Activity.SubActivity.AddCaseActivity;
import com.singular.barrister.Activity.SubActivity.AddClientActivity;
import com.singular.barrister.Activity.SubActivity.AddCourtActivity;
import com.singular.barrister.Adapter.CourtListAdapter;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.CourtTable;
import com.singular.barrister.Model.Cases.CaseDistrict;
import com.singular.barrister.Model.Cases.CaseState;
import com.singular.barrister.Model.Cases.CaseSubDistrict;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectCourt extends AppCompatActivity implements IDataChangeListener<IModel> {
    View horizontalLine;
    TextView txtName;
    RecyclerView recyclerView;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_court);
        if (getActionBar() != null) {
            getActionBar().setTitle("Select Court");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select Court");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtName = (TextView) findViewById(R.id.textViewErrorText);
        recyclerView = (RecyclerView) findViewById(R.id.caseSubTypeRecycleView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getApplicationContext(), AddCourtActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        fetchData();
    }

    public void fetchData() {
        List<CourtTable> list = getAllCourt();
        convertList(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (new NetworkConnection(this).isNetworkAvailable()) {
            retrofitManager.getCourtList(this, new UserPreferance(this).getToken());
        } else {
            Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
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
                if (courtListAdapter != null)
                    courtListAdapter.getFilter().filter(newText);
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
    }

    public void show() {
        courtListAdapter = new CourtListAdapter(getApplicationContext(), courtList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courtListAdapter);
        txtName.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
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

    DatabaseHelper databaseHelper;

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    ArrayList<CourtData> courtList;
    CourtListAdapter courtListAdapter;
    RetrofitManager retrofitManager;

    public void convertList(List<CourtTable> list) {
        if (list == null || list.size() == 0) {
            retrofitManager = new RetrofitManager();
            if (courtList == null)
                courtList = new ArrayList<>();
            courtList.clear();
            if (new NetworkConnection(this).isNetworkAvailable()) {
                retrofitManager.getCourtList(this, new UserPreferance(this).getToken());
            } else {
                Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        } else {
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
            show();
        }
    }

    public void showError() {
        txtName.setVisibility(View.VISIBLE);
        txtName.setText("You have not added any court yet,\\nclick on + to add new");
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof CourtResponse) {
            CourtResponse courtResponse = (CourtResponse) response;
            if (courtList == null)
                courtList = new ArrayList<>();
            courtList.clear();
            if (courtResponse.getData().getCourt() != null) {
                courtList.addAll(courtResponse.getData().getCourt());
                show();
            } else
                showError();
        } else showError();
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    public class CourtListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        ArrayList<CourtData> courtList;
        ArrayList<CourtData> arrayList;
        Context context;

        public CourtListAdapter(Context context, ArrayList<CourtData> courtList) {
            this.courtList = courtList;
            arrayList = courtList;
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

                if (courtList == null)
                    return null;

                if (charString.isEmpty()) {

                    courtList = arrayList;
                } else {
                    if (count > charString.length() && courtList.size() == 0) {
                        courtList = arrayList;
                    }
                    count++;
                    ArrayList<CourtData> filteredList = new ArrayList<>();

                    for (CourtData courtData : courtList) {
                        if (courtData.getCourt_name().toLowerCase().contains(charString.toLowerCase()) ||
                                getAddress(courtData).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(courtData);
                        }
                    }

                    courtList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = courtList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results != null) {
                    courtList = (ArrayList<CourtData>) results.values;
                    notifyDataSetChanged();
                }
            }

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
                        returnData(courtList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    public void returnData(CourtData data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Court", (Serializable) data);
        Intent returnIntent = getIntent();
        returnIntent.putExtra("result", 33);
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

}
