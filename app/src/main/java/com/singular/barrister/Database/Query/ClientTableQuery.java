package com.singular.barrister.Database.Query;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.singular.barrister.Database.DB.DatabaseHelper;

/**
 * Created by rahul.kalamkar on 12/1/2017.
 */

public class ClientTableQuery {

    DatabaseHelper databaseHelper = null;
    Context context;

    public void addClient(Context context) {
        this.context = context;
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
