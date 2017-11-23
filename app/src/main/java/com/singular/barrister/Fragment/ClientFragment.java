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

import com.singular.barrister.Adapter.ClientListAdapter;
import com.singular.barrister.Adapter.CourtListAdapter;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Client.ClientResponse;
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

public class ClientFragment extends Fragment implements IDataChangeListener<IModel>{

    private RecyclerView mRecycleView;
    private ProgressBar progressBar;
    TextView errorTextView;
    private RetrofitManager retrofitManager;
    ArrayList<Client> clientList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_court, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);mRecycleView=(RecyclerView)getView().findViewById(R.id.courtRecycleView);
        progressBar=(ProgressBar)getView().findViewById(R.id.progressBar);
        errorTextView=(TextView)getView().findViewById(R.id.textViewErrorText);
        retrofitManager=new RetrofitManager();
        clientList=new ArrayList<Client>();
getClientList();
    }
    public void showError()
    {
        errorTextView.setText("There is no client added yet!");
        errorTextView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
    public void getClientList()
    {
        if(new NetworkConnection(getActivity()).isNetworkAvailable())
        {
            retrofitManager.getClientList(this, new UserPreferance(getActivity()).getToken());
        }
        else {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.network_error),Toast.LENGTH_SHORT).show();
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
        if(response!=null && response instanceof ClientResponse)
        {
            ClientResponse clientResponse =(ClientResponse)response;
            if(clientResponse.getData().getClient()!=null) {
                clientList.addAll(clientResponse.getData().getClient());
                ClientListAdapter clientListAdapter = new ClientListAdapter(getActivity(), clientList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecycleView.setLayoutManager(linearLayoutManager);
                mRecycleView.setAdapter(clientListAdapter);
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
