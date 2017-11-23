package com.singular.barrister.Fragment;

import android.content.Context;
import android.net.Uri;
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

import com.singular.barrister.Adapter.CourtListAdapter;
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

public class CourtFragment extends Fragment implements IDataChangeListener<IModel>{

    private RecyclerView mRecycleView;
    private ProgressBar progressBar;
    TextView errorTextView;
    private ArrayList<CourtData> courtList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_court, container, false);
    }

    private RetrofitManager retrofitManager;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleView=(RecyclerView)getView().findViewById(R.id.courtRecycleView);
        progressBar=(ProgressBar)getView().findViewById(R.id.progressBar);
        errorTextView=(TextView)getView().findViewById(R.id.textViewErrorText);
        courtList=new ArrayList<CourtData>();
        retrofitManager=new RetrofitManager();
        getCourtList();
    }

    public void showError()
    {
        errorTextView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void getCourtList()
    {
        if(new NetworkConnection(getActivity()).isNetworkAvailable())
        {
            retrofitManager.getCourtList(this, new UserPreferance(getActivity()).getToken());
        }
        else {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.network_error),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if(response!=null && response instanceof CourtResponse)
        {
            CourtResponse courtResponse =(CourtResponse)response;
            if(courtResponse.getData().getCourt()!=null) {
                courtList.addAll(courtResponse.getData().getCourt());
                CourtListAdapter courtListAdapter = new CourtListAdapter(getActivity(), courtList);
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

    @Override
    public void onDataFailed(WebServiceError error) {

    }
}
