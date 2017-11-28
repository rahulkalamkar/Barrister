package com.singular.barrister.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Activity.SubActivity.DisplayClientActivity;
import com.singular.barrister.Adapter.ClientListAdapter;
import com.singular.barrister.Adapter.CourtListAdapter;
import com.singular.barrister.Interface.RecycleItem;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Client.ClientResponse;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.RecyclerItemTouchHelper;
import com.singular.barrister.Util.WebServiceError;

import java.util.ArrayList;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class ClientFragment extends Fragment implements IDataChangeListener<IModel>,RecycleItem {

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
        super.onViewCreated(view, savedInstanceState);
        mRecycleView = (RecyclerView) getView().findViewById(R.id.courtRecycleView);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        errorTextView = (TextView) getView().findViewById(R.id.textViewErrorText);
        retrofitManager = new RetrofitManager();
        clientList = new ArrayList<Client>();
        getClientList();
    }

    public void showError() {
        errorTextView.setText("There is no client added yet!");
        errorTextView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void getClientList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            retrofitManager.getClientList(this, new UserPreferance(getActivity()).getToken());
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
        if (response != null && response instanceof ClientResponse) {
            ClientResponse clientResponse = (ClientResponse) response;
            if (clientResponse.getData().getClient() != null) {
                clientList.addAll(clientResponse.getData().getClient());
                ClientListAdapter clientListAdapter = new ClientListAdapter(getActivity(), clientList,this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecycleView.setLayoutManager(linearLayoutManager);
                mRecycleView.setAdapter(clientListAdapter);
                progressBar.setVisibility(View.GONE);
            }
            else if(clientResponse.getError()!=null && clientResponse.getError().getStatus_code() ==401)
            {
                Toast.makeText(getActivity(),"Your session is Expired",Toast.LENGTH_SHORT).show();
                new UserPreferance(getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LandingScreen.class);
                startActivity(intent);
                getActivity().finish();
            }else
                showError();
        } else
        {Toast.makeText(getActivity(),"Your session is Expired",Toast.LENGTH_SHORT).show();
            new UserPreferance(getActivity()).logOut();
            Intent intent = new Intent(getActivity(), LandingScreen.class);
            startActivity(intent);
            getActivity().finish();}
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Client", clientList.get(position));
        Intent intent = new Intent(getActivity(), DisplayClientActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    private int newPosition;
    @Override
    public void onItemLongClick(int position) {
        showMenu(position);
    }

    public void showMenu(int position)
    {
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.simple_list, null);

        PopupWindow menuWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        menuWindow.setOutsideTouchable(false);
        menuWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= 21) {
            menuWindow.setElevation(5.0f);
        }
        menuWindow.showAtLocation(mRecycleView, Gravity.CENTER, 0, 0);
    }

}
