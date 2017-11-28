package com.singular.barrister.Activity.SubActivity;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.singular.barrister.Adapter.HearingAdapter;
import com.singular.barrister.Model.CaseHearingResponse;
import com.singular.barrister.Model.Cases.CaseHearing;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.util.ArrayList;

public class HearingDateActivity extends AppCompatActivity implements IDataChangeListener<IModel> {
    RecyclerView mRecycleView;
    ProgressBar mProgressBar;
    RetrofitManager retrofitManager;
    ArrayList<CaseHearing> hearingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_date);
        if (getActionBar() != null) {
            getActionBar().setTitle("Last hearing dates");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle("Last hearing dates");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        hearingList = new ArrayList<CaseHearing>();
        getList();
    }

    public void getList() {
        if (new NetworkConnection(getApplicationContext()).isNetworkAvailable()) {
            retrofitManager = new RetrofitManager();
            retrofitManager.getCaseHearingList(this, new UserPreferance(getApplicationContext()).getToken(), getIntent().getExtras().getString("Id"));
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof CaseHearingResponse) {
            CaseHearingResponse caseHearingResponse = (CaseHearingResponse) response;
            hearingList.addAll(caseHearingResponse.getData().getHearing());
            HearingAdapter hearingAdapter = new HearingAdapter(getApplicationContext(), hearingList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecycleView.setLayoutManager(linearLayoutManager);
            mRecycleView.setAdapter(hearingAdapter);
        } else {

        }
    }

    @Override
    public void onDataFailed(WebServiceError error) {

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
}
