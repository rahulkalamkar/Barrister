package com.singular.barrister.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Activity.HomeScreen;
import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Adapter.CourtListAdapter;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.CourtDistrict;
import com.singular.barrister.Database.Tables.CourtState;
import com.singular.barrister.Database.Tables.CourtSubDistrict;
import com.singular.barrister.Database.Tables.CourtTable;
import com.singular.barrister.Database.Tables.StateTable;
import com.singular.barrister.DisplayCourtActivity;
import com.singular.barrister.Interface.RecycleItem;
import com.singular.barrister.Interface.RecycleItemCourt;
import com.singular.barrister.Model.Cases.CaseDistrict;
import com.singular.barrister.Model.Cases.CaseState;
import com.singular.barrister.Model.Cases.CaseSubDistrict;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Model.District;
import com.singular.barrister.Model.State;
import com.singular.barrister.Model.SubDistrict;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourtFragment extends Fragment implements IDataChangeListener<IModel>, RecycleItemCourt {

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
        mRecycleView = (RecyclerView) getView().findViewById(R.id.courtRecycleView);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        errorTextView = (TextView) getView().findViewById(R.id.textViewErrorText);
        courtList = new ArrayList<CourtData>();
        retrofitManager = new RetrofitManager();
        registerForContextMenu(mRecycleView);
        ads();
        getCourtList();
    }

    private AdView mAdView;

    public void ads() {
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void refreshData() {
        try {
            if (courtList == null)
                courtList = new ArrayList<>();
            courtList.clear();
            if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                retrofitManager.getCourtList(this, new UserPreferance(getActivity()).getToken());
            } else {
                if (getActivity() != null) {
                    List<CourtTable> list = getAllCourt();
                    if (list != null) {
                        convertList(list);
                    }

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
        }
    }

    public void showError() {
        errorTextView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void getCourtList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            List<CourtTable> list = getAllCourt();
            if (list != null) {
                convertList(list);
            }
            if (courtList != null && courtList.size() == 0)
                progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getCourtList(this, new UserPreferance(getActivity()).getToken());
        } else {
            if (getActivity() != null) {
                List<CourtTable> list = getAllCourt();
                if (list != null && list.size() != 0) {
                    convertList(list);
                } else {
                    showError();
                }
                //          Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDataChanged() {

    }

    CourtListAdapter courtListAdapter;

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof CourtResponse) {
            CourtResponse courtResponse = (CourtResponse) response;
            if (courtResponse.getData().getCourt() != null && courtResponse.getData().getCourt().size() > 0) {
                courtList.clear();
                courtList.addAll(courtResponse.getData().getCourt());
                if (getActivity() != null) {
                    if (courtListAdapter == null) {
                        courtListAdapter = new CourtListAdapter(getActivity(), courtList, this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        mRecycleView.setLayoutManager(linearLayoutManager);
                        mRecycleView.setAdapter(courtListAdapter);
                    } else
                        courtListAdapter.notifyDataSetChanged();
                    saveLocally();
                    progressBar.setVisibility(View.GONE);
                }
            } else if (courtResponse.getError() != null && courtResponse.getError().getStatus_code() == 401) {
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

    public List<CourtTable> getAllCourt() {
        Dao<CourtTable, Integer> courtTableDao = null;
        try {
            if (getActivity() != null && getHelper(getActivity()) != null)
                courtTableDao = getHelper(getActivity()).getCourtTableDao();
            return courtTableDao.queryForAll();
        } catch (SQLException e) {
            return null;
        }
    }

    public void convertList(List<CourtTable> list) {
        for (int i = 0; i < list.size(); i++) {
            CaseState state = null;
            CaseDistrict district = null;
            CaseSubDistrict subDistrict = null;

            CourtTable courtTable = list.get(i);
            if (courtTable.getCourtState() != null) {
                state = new CaseState(courtTable.getCourtState().getName(), courtTable.getCourtState().getId(), courtTable.getCourtState().getParent_id(),
                        courtTable.getCourtState().getExternal_id(), courtTable.getCourtState().getLocation_type(), courtTable.getCourtState().getPin());
            }
            if (courtTable.getCourtDistrict() != null) {
                district = new CaseDistrict(courtTable.getCourtDistrict().getName(), courtTable.getCourtDistrict().getId(), courtTable.getCourtDistrict().getParent_id(),
                        courtTable.getCourtDistrict().getExternal_id(), courtTable.getCourtDistrict().getLocation_type(), courtTable.getCourtDistrict().getPin());
            }
            if (courtTable.getCourtSubDistrict() != null) {
                subDistrict = new CaseSubDistrict(courtTable.getCourtSubDistrict().getName(), courtTable.getCourtSubDistrict().getId(), courtTable.getCourtSubDistrict().getParent_id(),
                        courtTable.getCourtSubDistrict().getExternal_id(), courtTable.getCourtSubDistrict().getLocation_type(), courtTable.getCourtSubDistrict().getPin());
            }

            CourtData courtData = new CourtData(courtTable.getCourt_id(), courtTable.getCourt_name(), courtTable.getCourt_type(), courtTable.getCourt_number(),
                    courtTable.getState_id(), courtTable.getDistrict_id(), courtTable.getSub_district_id(),
                    state, district, subDistrict);

            courtList.add(courtData);
        }
        if (getActivity() != null) {
            courtListAdapter = new CourtListAdapter(getActivity(), courtList, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            mRecycleView.setLayoutManager(linearLayoutManager);
            mRecycleView.setAdapter(courtListAdapter);
        }
        progressBar.setVisibility(View.GONE);
    }

    public void saveLocally() {
        if (courtList == null)
            return;
        for (int i = 0; i < courtList.size(); i++) {
            if (checkCourt(courtList.get(i))) {
                addCourtToDataBase(courtList.get(i), true);
            } else {
                addCourtToDataBase(courtList.get(i), false);
            }
        }
        checkAndSyncDB(courtList);
    }

    public void checkAndSyncDB(ArrayList<CourtData> courtDataArrayList) {
        if (courtDataArrayList == null)
            return;
        List<CourtTable> list = getAllCourt();
        if (list == null)
            return;
        for (CourtTable courtTable : list) {
            boolean delete = true;
            for (CourtData data : courtDataArrayList) {
                if (data.getId().equalsIgnoreCase(courtTable.getCourt_id())) {
                    delete = false;
                    break;
                }
            }
            if (delete)
                deleteCourt(courtTable);
        }
    }

    public void deleteCourt(CourtData courtData) {
        List<CourtTable> list = null;
        Dao<CourtTable, Integer> courtTableDao = null;
        try {
            if (getActivity() != null && getHelper(getActivity()) != null)
                courtTableDao = getHelper(getActivity()).getCourtTableDao();
            list = courtTableDao.queryForEq("court_id", courtData.getId());
        } catch (SQLException e) {
        }
        if (list != null && list.size() > 0) {
            for (CourtTable courtTable : list) {
                deleteCourt(courtTable);
            }
        }

    }

    public void deleteCourt(CourtTable courtTable) {
        Dao<CourtTable, Integer> courtTableDao;
        try {
            if (getActivity() != null && getHelper(getActivity()) != null) {
                courtTableDao = getHelper(getActivity()).getCourtTableDao();
                courtTableDao.delete(courtTable);
            }
            Log.e("Court Table", "delete");
        } catch (SQLException e) {
            Log.e("Court table", "" + e);
        }
    }

    public CourtTable getCourt(CourtTable courtData) {
        List<CourtTable> list = null;
        Dao<CourtTable, Integer> courtTableIntegerDao;
        try {
            if (getActivity() != null && getHelper(getActivity()) != null) {
                courtTableIntegerDao = getHelper(getActivity()).getCourtTableDao();
                list = courtTableIntegerDao.queryForEq("court_id", courtData.getCourt_id());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (list != null && list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    public boolean checkCourt(CourtData courtData) {
        List<CourtTable> list = null;
        Dao<CourtTable, Integer> courtTableIntegerDao;
        try {
            if (getActivity() != null && getHelper(getActivity()) != null) {
                courtTableIntegerDao = getHelper(getActivity()).getCourtTableDao();
                list = courtTableIntegerDao.queryForEq("court_id", courtData.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (list != null && list.size() > 0);
    }

    public void addCourtToDataBase(CourtData courtData, boolean update) {
        CourtSubDistrict courtSubDistrict = null;
        CourtDistrict courtDistrict = null;
        CourtState courtState = null;

        if (courtData.getState() != null) {
            courtState = new CourtState(courtData.getState().getId(), courtData.getState().getParent_id(), courtData.getState().getExternal_id(),
                    courtData.getState().getName(), courtData.getState().getLocation_type(), courtData.getState().getPin());
        }
        if (courtData.getDistrict() != null) {
            courtDistrict = new CourtDistrict(courtData.getDistrict().getId(), courtData.getDistrict().getParent_id(), courtData.getDistrict().getExternal_id(),
                    courtData.getDistrict().getName(), courtData.getDistrict().getLocation_type(), courtData.getDistrict().getPin());
        }
        if (courtData.getSubdistrict() != null) {
            courtSubDistrict = new CourtSubDistrict(courtData.getSubdistrict().getId(), courtData.getSubdistrict().getParent_id(), courtData.getSubdistrict().getExternal_id(),
                    courtData.getSubdistrict().getName(), courtData.getSubdistrict().getLocation_type(), courtData.getSubdistrict().getPin());
        }

        CourtTable courtTable = new CourtTable(courtData.getId(), courtData.getCourt_name(), courtData.getCourt_number(), courtData.getCourt_type(), courtData.getState_id(),
                courtData.getDistrict_id(), courtData.getSub_district_id(), courtState, courtDistrict, courtSubDistrict);

        Dao<CourtTable, Integer> courtTableDao;
        try {
            if (getActivity() != null && getHelper(getActivity()) != null) {
                courtTableDao = getHelper(getActivity()).getCourtTableDao();
                if (update) {
                    CourtTable table = getCourt(courtTable);
                    if (table != null) {
                        updateTable(table, courtTable);
                        courtTable.setId(table.getId());
                    }
                    int i = courtTableDao.update(courtTable);
                    Log.e("Court Table", "update" + i);
                } else {
                    courtTableDao.create(courtTable);
                    Log.e("Court Table", "inserted");
                }
            }
        } catch (SQLException e) {
            Log.e("Court table", "" + e);
        }
    }

    public void updateTable(CourtTable table, CourtTable newTable) {
        try {
            if (table == null)
                return;
            if (getActivity() != null && getHelper(getActivity()) != null) {
                CourtState courtState = newTable.getCourtState();
                if (courtState != null && table.getCourtState() != null) {
                    courtState.setId(table.getCourtState().getIndex());
                    Dao<CourtState, Integer> courtStateIntegerDao = getHelper(getActivity()).getCourtStateDao();
                    courtStateIntegerDao.update(courtState);
                } else {
                    Dao<CourtState, Integer> courtStateIntegerDao = getHelper(getActivity()).getCourtStateDao();
                    courtStateIntegerDao.create(courtState);
                }

                CourtDistrict courtDistrict = newTable.getCourtDistrict();
                if (courtDistrict != null && table.getCourtDistrict() != null) {
                    courtDistrict.setId(table.getCourtDistrict().getIndex());
                    Dao<CourtDistrict, Integer> courtDistrictIntegerDao = getHelper(getActivity()).getCourtDistrictDao();
                    courtDistrictIntegerDao.update(courtDistrict);
                } else {
                    Dao<CourtDistrict, Integer> courtDistrictIntegerDao = getHelper(getActivity()).getCourtDistrictDao();
                    courtDistrictIntegerDao.create(courtDistrict);
                }

                CourtSubDistrict courtSubDistrict = newTable.getCourtSubDistrict();
                if (courtSubDistrict != null && table.getCourtSubDistrict() != null) {
                    courtSubDistrict.setId(table.getCourtSubDistrict().getIndex());
                    Dao<CourtSubDistrict, Integer> courtSubDistrictIntegerDao = getHelper(getActivity()).getCourtSubDistrictDao();
                    courtSubDistrictIntegerDao.update(courtSubDistrict);
                } else {
                    if (courtSubDistrict != null) {
                        Dao<CourtSubDistrict, Integer> courtSubDistrictIntegerDao = getHelper(getActivity()).getCourtSubDistrictDao();
                        courtSubDistrictIntegerDao.create(courtSubDistrict);
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Court table", "" + e);
        }
    }

    DatabaseHelper databaseHelper;

    private DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null && context != null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void releaseHelper() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseHelper();
    }

    public void onSearch(String text) {
        if (courtListAdapter != null)
            courtListAdapter.getFilter().filter(text);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete") {
            if (selectedItem != null && getActivity() != null) {
                retrofitManager.deleteCourt(new UserPreferance(getActivity()).getToken(), selectedItem.getId());
                deleteCourt(selectedItem);
                courtList.remove(selectedItem);
                courtListAdapter.notifyDataSetChanged();
                if (courtList.size() == 0) {
                    showError();
                }
            }
        } else {
            return false;
        }
        return true;
    }

    CourtData selectedItem;

    @Override
    public void onItemClick(CourtData court) {
        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Court", (Serializable) court);
            Intent intent = new Intent(getActivity(), DisplayCourtActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(CourtData court) {
        selectedItem = court;
    }
}
