package com.singular.barrister.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.singular.barrister.Interface.RecycleItemClient;
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

public class ClientFragment extends Fragment implements IDataChangeListener<IModel>, RecycleItemClient {

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

        registerForContextMenu(mRecycleView);
    }

    public void showError() {
        errorTextView.setText("There is no client added yet!");
        errorTextView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void refreshData() {
        try {
            if (clientList == null)
                clientList = new ArrayList<Client>();
            clientList.clear();

            if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                retrofitManager.getClientList(this, new UserPreferance(getActivity()).getToken());
            } else {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    List<BaseClientTable> list = getLocalData();
                    if (list != null)
                        convertAndDisplay(list);
                }
            }
        } catch (Exception e) {

        }
    }

    public void getClientList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            List<BaseClientTable> list = getLocalData();
            if (list != null)
                convertAndDisplay(list);

            if (clientList != null && clientList.size() == 0)
                progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getClientList(this, new UserPreferance(getActivity()).getToken());
        } else {
            if (getActivity() != null) {
                //     Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                List<BaseClientTable> list = getLocalData();
                if (list != null)
                    convertAndDisplay(list);
            }
        }
    }

    public void convertAndDisplay(List<BaseClientTable> list) {
        for (int i = 0; i < list.size(); i++) {
            BaseClientTable baseClientTable = list.get(i);

            ClientTable clientTable = baseClientTable.getClientTable();
            ClientDetail clientDetail = new ClientDetail(clientTable.getClient_id(), clientTable.getFirst_name(), clientTable.getLast_name(),
                    clientTable.getCountry_code(), clientTable.getMobile(), clientTable.getEmail(),
                    clientTable.getAddress(), clientTable.getUser_type(), clientTable.getReferral_code(),
                    clientTable.getParent_user_id(), clientTable.getUsed_referral_code(), clientTable.getDevice_type(),
                    clientTable.getDevice_token(), clientTable.getSubscription(), clientTable.getCreated_at(), clientTable.getUpdated_at());

            Client client = new Client(baseClientTable.getBase_id(), baseClientTable.getClient_id(), baseClientTable.getCreated_at(), clientDetail);
            if (clientList == null)
                clientList = new ArrayList<Client>();
            clientList.add(client);
        }

        if (getActivity() != null) {
            clientListAdapter = new ClientListAdapter(getActivity(), clientList, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            mRecycleView.setLayoutManager(linearLayoutManager);
            mRecycleView.setAdapter(clientListAdapter);
            progressBar.setVisibility(View.GONE);
        }

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
                clientList.clear();
                clientList.addAll(clientResponse.getData().getClient());
                if (getActivity() != null) {
                    saveClient();
                    if (clientListAdapter == null) {
                        clientListAdapter = new ClientListAdapter(getActivity(), clientList, this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        mRecycleView.setLayoutManager(linearLayoutManager);
                        mRecycleView.setAdapter(clientListAdapter);
                    } else
                        clientListAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            } else if (clientResponse.getError() != null && clientResponse.getError().getStatus_code() == 401) {
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
    public void onItemClick(Client client) {
        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Client", client);
            Intent intent = new Intent(getActivity(), DisplayClientActivity.class);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    private int newPosition;

    Client selectedClient;

    @Override
    public void onItemLongClick(Client client) {
        selectedClient = client;
        //showMenu(client);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Call");
        menu.add(0, v.getId(), 0, "SMS");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Call") {
            String phone = "";
            phone = selectedClient.getClient().getCountry_code() + "" + selectedClient.getClient().getMobile();
            if (!TextUtils.isEmpty(phone)) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        } else if (item.getTitle() == "SMS") {
            String phone = "";
            phone = selectedClient.getClient().getCountry_code() + "" + selectedClient.getClient().getMobile();
            sendSMS(phone);
        } else if (item.getTitle() == "Delete") {
            if (getActivity() != null) {
                retrofitManager.deleteClient(new UserPreferance(getActivity()).getToken(), selectedClient.getClient_id());
                clientList.remove(selectedClient);
                clientListAdapter.notifyDataSetChanged();
            }
        } else {
            return false;
        }
        return true;
    }

    public void sendSMS(String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            try {

                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

                Intent sendIntent = new Intent(Intent.ACTION_SEND, Uri.parse(number));
                sendIntent.setType("text/plain");

                if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                // any app that support this intent.
                {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                startActivity(sendIntent);

            } catch (Exception e) {
                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");

                if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                // any app that support this intent.
                {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                startActivity(sendIntent);
            }
        } else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", number);
            startActivity(smsIntent);
        }
    }

    public List<BaseClientTable> getLocalData() {
        Dao<BaseClientTable, Integer> baseClientTables = null;
        try {
            if (getHelper() != null) {
                baseClientTables = getHelper().getBaseClientTableDao();
            }
            return baseClientTables.queryForAll();
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
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
            if (checkClient(clientList.get(i))) {
                addDataLocally(clientList.get(i), true);
            } else {
                addDataLocally(clientList.get(i), false);
            }
        }
        checkAndSyncDB(clientList);
    }

    public void addDataLocally(Client client, boolean update) {
        ClientTable clientTable = new ClientTable(client.getClient().getId(), client.getClient().getFirst_name(), client.getClient().getLast_name(),
                client.getClient().getCountry_code(), client.getClient().getMobile(), client.getClient().getEmail(),
                client.getClient().getAddress(), client.getClient().getUser_type(), client.getClient().getReferral_code(),
                client.getClient().getParent_user_id(), client.getClient().getUsed_referral_code(), client.getClient().getDevice_type(),
                client.getClient().getDevice_token(), client.getClient().getSubscription(), client.getClient().getCreated_at(), client.getClient().getUpdated_at());


        Dao<BaseClientTable, Integer> baseClientTablesDao;
        try {
            if (getHelper() != null) {
                baseClientTablesDao = getHelper().getBaseClientTableDao();
                if (update) {
                    clientTable.setId(getClient(client.getClient_id()).getClientTable().getId());
                    BaseClientTable baseClientTable = new BaseClientTable(client.getId(), client.getCreated_at(), client.getClient_id(), clientTable);


                    Dao<ClientTable,Integer> clientTableDao=getHelper().getClientTableDao();
                    clientTableDao.update(clientTable);

                    baseClientTable.setId(getClient(client.getClient_id()).getId());


                    int i = baseClientTablesDao.update(baseClientTable);
                    Log.e("BAseClient Table", "updated" + i);
                } else {
                    BaseClientTable baseClientTable = new BaseClientTable(client.getId(), client.getCreated_at(), client.getClient_id(), clientTable);
                    baseClientTablesDao.create(baseClientTable);
                    Log.e("BAseClient Table", "inserted");
                }
            }
        } catch (SQLException e) {
            Log.e("BAseClient table", "" + e);
        } catch (Exception e) {
            Log.e("BAseClient table", "" + e);
        }
    }

    public BaseClientTable getClient(String id) {
        List<BaseClientTable> list = null;
        Dao<BaseClientTable, Integer> baseClientTablesDao = null;
        try {
            if (getHelper() != null)
                baseClientTablesDao = getHelper().getBaseClientTableDao();
            list = baseClientTablesDao.queryForEq("client_id", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    public boolean checkClient(Client client) {
        List<BaseClientTable> list = null;
        Dao<BaseClientTable, Integer> baseClientTablesDao = null;
        try {
            if (getHelper() != null)
                baseClientTablesDao = getHelper().getBaseClientTableDao();
            list = baseClientTablesDao.queryForEq("client_id", client.getClient_id());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (list != null && list.size() > 0);
    }

    public void checkAndSyncDB(ArrayList<Client> clients) {
        if (clients == null)
            return;
        List<BaseClientTable> list = getLocalData();
        if (list == null)
            return;
        for (BaseClientTable baseClientTable : list) {
            boolean delete = true;
            for (Client client : clients) {
                if (client.getClient_id().equalsIgnoreCase(baseClientTable.getClient_id())) {
                    delete = false;
                    break;
                }
            }
            if (delete) {
                deleteClient(baseClientTable);
            }
        }

    }

    public void deleteClient(BaseClientTable baseClientTable) {
        Dao<BaseClientTable, Integer> baseClientTables;
        try {
            if (getHelper() != null) {
                baseClientTables = getHelper().getBaseClientTableDao();
                baseClientTables.delete(baseClientTable);
            }
            Log.e("BAseClient table", "deleted");
        } catch (Exception e) {
            Log.e("BAseClient table", "" + e);
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            if (getActivity() != null)
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

    public void onSearch(String text) {
        Log.e("ClientFragment", text);
        if (clientListAdapter != null) {
            clientListAdapter.getFilter().filter(text);
        }
    }

}
