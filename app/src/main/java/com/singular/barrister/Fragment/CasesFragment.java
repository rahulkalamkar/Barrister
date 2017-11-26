package com.singular.barrister.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Adapter.CasesListAdapter;
import com.singular.barrister.Adapter.CourtListAdapter;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Cases.CasesData;
import com.singular.barrister.Model.Cases.CasesResponse;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.util.ArrayList;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasesFragment extends Fragment implements IDataChangeListener<IModel>{

    private RecyclerView mRecycleView;
    private ProgressBar progressBar;
    TextView errorTextView;
    private RetrofitManager retrofitManager;
    private ArrayList<Case> caseList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_court, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleView=(RecyclerView)getView().findViewById(R.id.courtRecycleView);
        progressBar=(ProgressBar)getView().findViewById(R.id.progressBar);
        errorTextView=(TextView)getView().findViewById(R.id.textViewErrorText);
        retrofitManager=new RetrofitManager();
        caseList=new ArrayList<Case>();
        getCasesList();
        
    }

    public void getCasesList()
    {
        if(new NetworkConnection(getActivity()).isNetworkAvailable())
        {
            retrofitManager.getCasesList(this, new UserPreferance(getActivity()).getToken());
        }
        else {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.network_error),Toast.LENGTH_SHORT).show();
        }
    }

    public void showError()
    {
        errorTextView.setText("There is no case added yet!");
        errorTextView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    @Override
    public void onDataReceived(IModel response) {
        if(response!=null && response instanceof CasesResponse)
        {
            CasesResponse casesResponse =(CasesResponse) response;
            if(casesResponse.getData().getCaseList()!=null) {
                caseList.addAll(casesResponse.getData().getCaseList());
                CasesListAdapter courtListAdapter = new CasesListAdapter(getActivity(), caseList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecycleView.setLayoutManager(linearLayoutManager);
                mRecycleView.setAdapter(courtListAdapter);
                progressBar.setVisibility(View.GONE);
            }
            else
                showError();
        }
        else {
            showError();
        }
    }
}
