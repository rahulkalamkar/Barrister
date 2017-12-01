package com.singular.barrister.Database.Query;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.CourtTypeTable;
import com.singular.barrister.Database.Tables.StateTable;
import com.singular.barrister.Model.State;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul.kalamkar on 12/1/2017.
 */

public class CourtTypeTableQuery {
    DatabaseHelper databaseHelper = null;
    Context context;
    List<CourtTypeTable> courtTypeList = null;

    public void valuesFromServer(Context context, ArrayList<String> courtTypeList) {
        for (int i = 0; i < courtTypeList.size(); i++) {
            addCourtType(context, courtTypeList.get(i));
        }
        releaseHelper();
    }

    public void addCourtType(Context context, String type) {
        this.context = context;
        CourtTypeTable courtTypeTable = new CourtTypeTable(type);

        final Dao<CourtTypeTable, Integer> courtyTypeDao;
        try {
            courtyTypeDao = getHelper().getCourtTypeTableDao();
            courtyTypeDao.create(courtTypeTable);
            Log.e("Court Type", "Value added ");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Court Type", "Error " + e);
        }
    }

    public void getAllCourtType(Context context) {
        final Dao<CourtTypeTable, Integer> courtTypeDao;
        try {
            courtTypeDao = getHelper().getCourtTypeTableDao();
            courtTypeList = courtTypeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("StateTable", "Error " + e);
        }
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
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
