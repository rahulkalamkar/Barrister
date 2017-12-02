package com.singular.barrister.Database.Query;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.StateTable;
import com.singular.barrister.Model.State;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul.kalamkar on 12/1/2017.
 */

public class StateTableQuery {
    DatabaseHelper databaseHelper = null;
    Context context;
    List<StateTable> stateList = null;

    public StateTableQuery() {

    }

    public void valuesFromServer(Context context, ArrayList<State> stateList) {
        for (int i = 0; i < stateList.size(); i++) {
            State state = stateList.get(i);
            addState(context, state.getId(), state.getParent_id(), state.getExternal_id(), state.getName(), state.getLocation_type(), state.getPin());
        }
        releaseHelper();
    }

    public void addState(Context context, String id, String parent_id, String external_id, String name, String location_type, String pin) {
        this.context = context;
        StateTable stateTable = new StateTable(id, parent_id, external_id, name, location_type, pin);

        final Dao<StateTable, Integer> studentDao;
        try {
            studentDao = getHelper(context).getStateTableDao();
            studentDao.create(stateTable);
            Log.e("StateTable", "Value added ");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("StateTable", "Error " + e);
        }
    }

    public List<StateTable> getAllState(Context context) {
        final Dao<StateTable, Integer> stateDao;
        try {
            stateDao = getHelper(context).getStateTableDao();
            stateList = stateDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("StateTable", "Error " + e);
        }
        return stateList;
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
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
}
