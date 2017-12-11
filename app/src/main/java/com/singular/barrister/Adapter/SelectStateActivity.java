package com.singular.barrister.Adapter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.singular.barrister.Interface.StateSelection;
import com.singular.barrister.Model.District;
import com.singular.barrister.Model.State;
import com.singular.barrister.Model.SubDistrict;
import com.singular.barrister.R;
import com.singular.barrister.Util.StatePopUpWindow;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class SelectStateActivity extends AppCompatActivity implements StateSelection {

    ArrayList<State> stateList;
    RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_state);
        if (getActionBar() != null) {
            getActionBar().setTitle("Select State");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select state");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);

        stateList = new ArrayList<State>();
        stateList.addAll((ArrayList<State>) getIntent().getExtras().getSerializable("State"));
        showList();
    }

    RecycleAdapter recycleAdapter;

    public void showList() {
        recycleAdapter = new RecycleAdapter(getApplicationContext(), stateList,this);
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

    public void returnDataToHome(int resultCode, State selected) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("State", (Serializable) selected);
        Intent returnIntent = getIntent();
        returnIntent.putExtra("result", resultCode);
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void getSelectedState(State state) {
        returnDataToHome(1, state);
    }

    @Override
    public void getSelectedDisstrict(District district) {

    }

    @Override
    public void getSelectedSubDistrict(SubDistrict subDistrict) {

    }
}
