package com.singular.barrister.Database.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.singular.barrister.Database.Tables.CourtTypeTable;
import com.singular.barrister.Database.Tables.StateTable;

import android.util.Log;

import java.sql.SQLException;

/**
 * Created by rahulbabanaraokalamkar on 11/29/17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "barrister.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<StateTable, Integer> stateTableDao;
    private Dao<CourtTypeTable, Integer> courtTypeTableDao;
    public SQLiteDatabase sqLiteDatabase;
    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        try {
            sqLiteDatabase = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, StateTable.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, StateTable.class, true);
            TableUtils.dropTable(connectionSource, CourtTypeTable.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<StateTable, Integer> getStateTableDao() throws SQLException {
        if (stateTableDao == null) {
            stateTableDao = getDao(StateTable.class);
        }
        return stateTableDao;
    }

    public Dao<CourtTypeTable, Integer> getCourtTypeTableDao() throws SQLException {
        if (courtTypeTableDao == null) {
            courtTypeTableDao = getDao(CourtTypeTable.class);
        }
        return courtTypeTableDao;
    }
}
