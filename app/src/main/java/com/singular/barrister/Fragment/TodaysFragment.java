package com.singular.barrister.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Adapter.CasesListAdapter;
import com.singular.barrister.Adapter.TodaysCaseAdapter;
import com.singular.barrister.Database.Tables.Case.CaseTable;
import com.singular.barrister.Database.Tables.Case.Query.CaseQuery;
import com.singular.barrister.Database.Tables.Today.Query.TodayCaseQuery;
import com.singular.barrister.Database.Tables.Today.TodayCaseTable;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Today.TodayResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rahul.kalamkar on 11/24/2017.
 */

public class TodaysFragment extends Fragment implements IDataChangeListener<IModel> {

    private ProgressBar progressBar;
    TextView errorTextView;
    private RetrofitManager retrofitManager;
    private ArrayList<Case> caseList;
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycleView);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        errorTextView = (TextView) getView().findViewById(R.id.textViewErrorText);
        retrofitManager = new RetrofitManager();
        caseList = new ArrayList<Case>();
        getCasesList();
    }

    public void getCasesList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getTodayCases(this, new UserPreferance(getActivity()).getToken());
        } else {
            if (getActivity() != null) {
                List<TodayCaseTable> list = new TodayCaseQuery(getActivity()).getList();
                if (list != null) {
                    caseList = (ArrayList<Case>) new TodayCaseQuery(getActivity()).convertListToOnLineList(list);
                    TodaysCaseAdapter todaysCaseAdapter = new TodaysCaseAdapter(getActivity(), caseList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(todaysCaseAdapter);
                    progressBar.setVisibility(View.GONE);
                }
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showError() {
        errorTextView.setText("There is no case for a day!");
        errorTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof TodayResponse) {
            TodayResponse todayResponse = (TodayResponse) response;
            if (todayResponse.getData().getCaseList() != null) {
                caseList.addAll(todayResponse.getData().getCaseList());
                if (getActivity() != null) {
                    TodaysCaseAdapter todaysCaseAdapter = new TodaysCaseAdapter(getActivity(), caseList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(todaysCaseAdapter);
                    progressBar.setVisibility(View.GONE);
                    if (caseList != null && caseList.size() > 0) {
                        TodayCaseQuery caseQuery = new TodayCaseQuery(getActivity());
                        caseQuery.addList(caseList);
                    } else {
                        showError();
                    }
                } else {
                    showError();
                }
            } else if (todayResponse.getError() != null && todayResponse.getError().getStatus_code() == 401) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                    new UserPreferance(getActivity()).logOut();
                    Intent intent = new Intent(getActivity(), LandingScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            } else
                showError();
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                new UserPreferance(getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LandingScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    public void onSearch(String text) {
        Log.e("TodaysFragment", text);
    }
}
