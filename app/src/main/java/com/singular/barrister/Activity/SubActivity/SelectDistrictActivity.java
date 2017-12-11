package com.singular.barrister.Activity.SubActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Adapter.RecycleAdapter;
import com.singular.barrister.Interface.StateSelection;
import com.singular.barrister.Model.District;
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
import java.lang.reflect.Field;
import java.util.ArrayList;

public class SelectDistrictActivity extends AppCompatActivity implements StateSelection, IDataChangeListener<IModel> {
    ArrayList<District> districtList;
    EditText editText;
    RecyclerView mRecycleView;
    String stateId;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_district);
        if (getActionBar() != null) {
            getActionBar().setTitle("Select District");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select District");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        editText = (EditText) findViewById(R.id.editTextSearch);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        districtList = new ArrayList<District>();
        stateId = getIntent().getExtras().getString("StateId");
        getDistrictList();
    }

    public void getDistrictList() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            mProgressBar.setVisibility(View.VISIBLE);
            RetrofitManager retrofitManager = new RetrofitManager();
            retrofitManager.getDistrictList(this, new UserPreferance(getApplicationContext()).getToken(), stateId);
        } else {
            Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    RecycleAdapter recycleAdapter;

    public void showList() {
        recycleAdapter = new RecycleAdapter(getApplicationContext(), districtList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(recycleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);

        return true;
    }

    SearchView searchView;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setVisibility(View.GONE);
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
                recycleAdapter.getFilter().filter(newText);
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

    public void returnDataToHome(int resultCode, District district) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("District", (Serializable) district);
        Intent returnIntent = getIntent();
        returnIntent.putExtra("result", resultCode);
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void getSelectedState(State state) {

    }

    @Override
    public void getSelectedDisstrict(District district) {
        returnDataToHome(2, district);
    }

    @Override
    public void getSelectedSubDistrict(SubDistrict subDistrict) {

    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof StateResponse) {
            StateResponse stateResponse = (StateResponse) response;
            if (stateResponse.getData().getDistrict() != null) {
                districtList.addAll(stateResponse.getData().getDistrict());
                mProgressBar.setVisibility(View.GONE);
                showList();
                searchView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        Context context;
        ArrayList<District> mArrayList;
        ArrayList<District> districtArrayList;
        StateSelection stateSelection;

        public RecycleAdapter(Context context, ArrayList<District> stateList, StateSelection stateSelection) {
            this.context = context;
            mArrayList = stateList;
            this.districtArrayList = stateList;
            this.stateSelection = stateSelection;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item, parent, false);
            return new DistrictViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder != null && holder instanceof DistrictViewHolder) {
                DistrictViewHolder districtViewHolder = (DistrictViewHolder) holder;
                districtViewHolder.txtName.setText(districtArrayList.get(position).getName());
            }
        }

        @Override
        public int getItemCount() {
            return districtArrayList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    String charString = charSequence.toString();

                    if (charString.isEmpty()) {

                        districtArrayList = mArrayList;
                    } else {

                        ArrayList<District> filteredList = new ArrayList<>();

                        for (District state : districtArrayList) {

                            if (state.getName().toLowerCase().contains(charString) /*|| state.getName().toLowerCase().contains(charString) || state.getVer().toLowerCase().contains(charString)*/) {

                                filteredList.add(state);
                            }
                        }

                        districtArrayList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = districtArrayList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    districtArrayList = (ArrayList<District>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class DistrictViewHolder extends RecyclerView.ViewHolder {
            TextView txtName;

            public DistrictViewHolder(View itemView) {
                super(itemView);
                txtName = (TextView) itemView.findViewById(R.id.textViewName);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stateSelection.getSelectedDisstrict(districtArrayList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }
}
