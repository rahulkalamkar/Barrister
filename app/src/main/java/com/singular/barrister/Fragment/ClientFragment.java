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

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Activity.SubActivity.DisplayClientActivity;
import com.singular.barrister.Adapter.ClientListAdapter;
import com.singular.barrister.Adapter.CourtListAdapter;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.Client.ClientTable;
import com.singular.barrister.Database.Tables.CourtTable;
import com.singular.barrister.Interface.RecycleItem;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Client.ClientDetail;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class ClientFragment extends Fragment implements IDataChangeListener<IModel>, RecycleItem {

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
            List<BaseClientTable> list = getLocalData();
            if(list!=null)
                convertAndDisplay(list);
        }
    }

    public void convertAndDisplay(List<BaseClientTable> list) {
        for (int i = 0; i < list.size(); i++) {
            BaseClientTable baseClientTable=list.get(i);

            ClientTable clientTable=baseClientTable.getClientTable();
            ClientDetail clientDetail = new ClientDetail(clientTable.getClient_id(), clientTable.getFirst_name(), clientTable.getLast_name(),
                    clientTable.getCountry_code(), clientTable.getMobile(), clientTable.getEmail(),
                    clientTable.getAddress(), clientTable.getUser_type(), clientTable.getReferral_code(),
                    clientTable.getParent_user_id(), clientTable.getUsed_referral_code(), clientTable.getDevice_type(),
                    clientTable.getDevice_token(), clientTable.getSubscription(), clientTable.getCreated_at(), clientTable.getUpdated_at());

            Client client=new Client(baseClientTable.getBase_id(),baseClientTable.getClient_id(),baseClientTable.getCreated_at(),clientDetail);
            if(clientList==null)
            clientList=new ArrayList<Client>();
            clientList.add(client);
        }

        clientListAdapter = new ClientListAdapter(getActivity(), clientList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(clientListAdapter);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    ClientListAdapter clientListAdapter;

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof ClientResponse) {
            ClientResponse clientResponse = (ClientResponse) response;
            if (clientResponse.getData().getClient() != null) {
                clientList.addAll(clientResponse.getData().getClient());
                saveClient();
                clientListAdapter = new ClientListAdapter(getActivity(), clientList, this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecycleView.setLayoutManager(linearLayoutManager);
                mRecycleView.setAdapter(clientListAdapter);
                progressBar.setVisibility(View.GONE);
            } else if (clientResponse.getError() != null && clientResponse.getError().getStatus_code() == 401) {
                Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                new UserPreferance(getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LandingScreen.class);
                startActivity(intent);
                getActivity().finish();
            } else
                showError();
        } else {
            Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
            new UserPreferance(getActivity()).logOut();
            Intent intent = new Intent(getActivity(), LandingScreen.class);
            startActivity(intent);
            getActivity().finish();
        }
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

    public void showMenu(final int position) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.simple_list, null);

        final PopupWindow menuWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        menuWindow.setOutsideTouchable(false);
        menuWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= 21) {
            menuWindow.setElevation(5.0f);
        }

        TextView textViewDelete = (TextView) customView.findViewById(R.id.menuDeleteButton);
        TextView textViewCall = (TextView) customView.findViewById(R.id.menuCallButton);
        TextView textViewMessage = (TextView) customView.findViewById(R.id.menuMessageButton);

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofitManager.deleteClient(new UserPreferance(getActivity()).getToken(), clientList.get(position).getClient_id());
                clientList.remove(position);
                clientListAdapter.notifyDataSetChanged();
                menuWindow.dismiss();
            }
        });

        textViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuWindow.dismiss();
            }
        });

        textViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuWindow.dismiss();
            }
        });

        menuWindow.showAtLocation(mRecycleView, Gravity.CENTER, 0, 0);
    }

    public List<BaseClientTable> getLocalData() {
        Dao<BaseClientTable, Integer> baseClientTables;
        try {
            baseClientTables = getHelper().getBaseClientTableDao();
            return baseClientTables.queryForAll();
        } catch (SQLException e) {
            return null;
        }
    }

    DatabaseHelper databaseHelper = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseHelper();
    }


    public void saveClient() {
        if (clientList == null)
            return;
        for (int i = 0; i < clientList.size(); i++) {
            addDataLocally(clientList.get(i));
        }
    }

    public void addDataLocally(Client client) {
        ClientTable clientTable = new ClientTable(client.getClient().getId(), client.getClient().getFirst_name(), client.getClient().getLast_name(),
                client.getClient().getCountry_code(), client.getClient().getMobile(), client.getClient().getEmail(),
                client.getClient().getAddress(), client.getClient().getUser_type(), client.getClient().getReferral_code(),
                client.getClient().getParent_user_id(), client.getClient().getUsed_referral_code(), client.getClient().getDevice_type(),
                client.getClient().getDevice_token(), client.getClient().getSubscription(), client.getClient().getCreated_at(), client.getClient().getUpdated_at());

        BaseClientTable baseClientTable = new BaseClientTable(client.getId(), client.getCreated_at(), client.getClient_id(), clientTable);

        Dao<BaseClientTable, Integer> baseClientTablesDao;
        try {
            baseClientTablesDao = getHelper().getBaseClientTableDao();
            baseClientTablesDao.create(baseClientTable);
            Log.e("BAseClient Table", "inserted");
        } catch (SQLException e) {
            Log.e("BAseClient table", "" + e);
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void releaseHelper() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
