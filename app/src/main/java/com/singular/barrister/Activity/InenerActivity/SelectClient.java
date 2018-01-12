package com.singular.barrister.Activity.InenerActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Activity.SubActivity.AddCaseActivity;
import com.singular.barrister.Activity.SubActivity.AddClientActivity;
import com.singular.barrister.Activity.SubActivity.AddCourtActivity;
import com.singular.barrister.Adapter.ClientListAdapter;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.Client.ClientTable;
import com.singular.barrister.Model.CasesSubType;
import com.singular.barrister.Model.CasesTypeData;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Client.ClientDetail;
import com.singular.barrister.Model.Client.ClientResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectClient extends AppCompatActivity implements IDataChangeListener<IModel> {

    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_client);
        if (getActionBar() != null) {
            getActionBar().setTitle("Select Client");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select Client");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtName = (TextView) findViewById(R.id.textViewErrorText);
        recyclerView = (RecyclerView) findViewById(R.id.courtRecycleView);
        fetchAndDisplayClient();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getApplicationContext(), AddClientActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (new NetworkConnection(this).isNetworkAvailable()) {
            retrofitManager.getClientList(SelectClient.this, new UserPreferance(SelectClient.this).getToken());
        } else {
            Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
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
                if (clientListAdapter != null)
                    clientListAdapter.getFilter().filter(newText);
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


    TextView txtName;
    RecyclerView recyclerView;

    ClientListAdapter clientListAdapter;

    public void show() {
        txtName.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        clientListAdapter = new ClientListAdapter(getApplicationContext(), clientList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(clientListAdapter);


    }

    RetrofitManager retrofitManager;

    public void fetchAndDisplayClient() {
        retrofitManager = new RetrofitManager();
        List<BaseClientTable> list = getLocalData();
        if (list != null && list.size() > 0) {
            convertAndDisplay(list);
        } else if (new NetworkConnection(SelectClient.this).isNetworkAvailable()) {
            if (clientList == null)
                clientList = new ArrayList<Client>();
            clientList.clear();
            retrofitManager.getClientList(SelectClient.this, new UserPreferance(SelectClient.this).getToken());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<Client> clientList;

    public void convertAndDisplay(List<BaseClientTable> list) {
        if (clientList == null)
            clientList = new ArrayList<Client>();
        clientList.clear();

        for (int i = 0; i < list.size(); i++) {
            BaseClientTable baseClientTable = list.get(i);

            ClientTable clientTable = baseClientTable.getClientTable();
            ClientDetail clientDetail = new ClientDetail(clientTable.getClient_id(), clientTable.getFirst_name(), clientTable.getLast_name(),
                    clientTable.getCountry_code(), clientTable.getMobile(), clientTable.getEmail(),
                    clientTable.getAddress(), clientTable.getUser_type(), clientTable.getReferral_code(),
                    clientTable.getParent_user_id(), clientTable.getUsed_referral_code(), clientTable.getDevice_type(),
                    clientTable.getDevice_token(), clientTable.getSubscription(), clientTable.getCreated_at(), clientTable.getUpdated_at());

            Client client = new Client(baseClientTable.getBase_id(), baseClientTable.getCreated_at(), baseClientTable.getClient_id(), clientDetail);
            clientList.add(client);
        }
        show();

    }

    DatabaseHelper databaseHelper;

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public List<BaseClientTable> getLocalData() {
        Dao<BaseClientTable, Integer> baseClientTables;
        try {
            baseClientTables = getHelper(getApplicationContext()).getBaseClientTableDao();
            return baseClientTables.queryForAll();
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof ClientResponse) {
            ClientResponse clientResponse = (ClientResponse) response;
            if (clientList == null)
                clientList = new ArrayList<Client>();
            clientList.clear();
            if (clientResponse.getData().getClient() != null) {
                clientList.addAll(clientResponse.getData().getClient());
                show();
            } else
                showError();
        }
    }

    public void showError() {
        txtName.setVisibility(View.VISIBLE);
        txtName.setText("No client available!");
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }


    public class ClientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        ArrayList<Client> clientList;
        ArrayList<Client> arrayList;
        Context context;

        public ClientListAdapter(Context context, ArrayList<Client> clientList) {
            this.clientList = clientList;
            arrayList = clientList;
            this.context = context;
        }


        @Override
        public int getItemCount() {
            return clientList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.court_list_item, parent, false);
            return new ClientListAdapter.ClientViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ClientListAdapter.ClientViewHolder) {
                ClientListAdapter.ClientViewHolder clientViewHolder = (ClientListAdapter.ClientViewHolder) holder;
                clientViewHolder.txtCourtName.setText(clientList.get(position).getClient().getFirst_name() + " " +
                        clientList.get(position).getClient().getLast_name());
                clientViewHolder.txtStateName.setText("+" + clientList.get(position).getClient().getCountry_code() + " " +
                        clientList.get(position).getClient().getMobile());
            }
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        ValueFilter valueFilter;

        int count = 0;

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (clientList == null)
                    return null;

                if (charString.isEmpty()) {

                    clientList = arrayList;
                } else {
                    if (count > charString.length() && clientList.size() == 0) {
                        clientList = arrayList;
                    }
                    count++;
                    ArrayList<Client> filteredList = new ArrayList<>();

                    for (Client client : clientList) {
                        if (client.getClient().getFirst_name().toLowerCase().contains(charString.toLowerCase()) ||
                                client.getClient().getLast_name().toLowerCase().contains(charString.toLowerCase()) ||
                                client.getClient().getEmail().toLowerCase().contains(charString.toLowerCase()) ||
                                client.getClient().getMobile().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(client);
                        }
                    }

                    clientList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = clientList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results != null) {
                    clientList = (ArrayList<Client>) results.values;
                    notifyDataSetChanged();
                }
            }

        }


        public class ClientViewHolder extends RecyclerView.ViewHolder {
            public TextView txtCourtName, txtStateName, txtDelete;
            public RelativeLayout bgLayout;
            public LinearLayout fgLayout;

            public ClientViewHolder(View itemView) {
                super(itemView);
                txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
                txtStateName = (TextView) itemView.findViewById(R.id.textViewState);
                bgLayout = (RelativeLayout) itemView.findViewById(R.id.view_background);
                fgLayout = (LinearLayout) itemView.findViewById(R.id.view_foreground);
                txtDelete = (TextView) itemView.findViewById(R.id.textViewDelete);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnData(clientList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    public void returnData(Client client) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Client", (Serializable) client);
        Intent returnIntent = getIntent();
        returnIntent.putExtra("result", 22);
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
