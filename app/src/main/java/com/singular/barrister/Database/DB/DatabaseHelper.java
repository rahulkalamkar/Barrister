package com.singular.barrister.Database.DB;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.Client.ClientTable;
import com.singular.barrister.Database.Tables.CourtDistrict;
import com.singular.barrister.Database.Tables.CourtState;
import com.singular.barrister.Database.Tables.CourtSubDistrict;
import com.singular.barrister.Database.Tables.CourtTable;
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
    private Dao<CourtTable, Integer> courtTableDao;
    private Dao<CourtState, Integer> courtStateDao;
    private Dao<CourtDistrict, Integer> courtDistrictDao;
    private Dao<CourtSubDistrict, Integer> courtSubDistrictDao;
    private Dao<ClientTable, Integer> clientTableDao;
    private Dao<BaseClientTable, Integer> baseClientTableDao;

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
            TableUtils.createTable(connectionSource, CourtTypeTable.class);
            TableUtils.createTable(connectionSource, CourtTable.class);
            TableUtils.createTable(connectionSource, CourtState.class);
            TableUtils.createTable(connectionSource, CourtDistrict.class);
            TableUtils.createTable(connectionSource, CourtSubDistrict.class);
            TableUtils.createTable(connectionSource, BaseClientTable.class);
            TableUtils.createTable(connectionSource, ClientTable.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, StateTable.class, true);
            TableUtils.dropTable(connectionSource, CourtTypeTable.class, true);
            TableUtils.createTable(connectionSource, CourtTable.class);
            TableUtils.createTable(connectionSource, CourtState.class);
            TableUtils.createTable(connectionSource, CourtDistrict.class);
            TableUtils.createTable(connectionSource, CourtSubDistrict.class);
            TableUtils.createTable(connectionSource, BaseClientTable.class);
            TableUtils.createTable(connectionSource, ClientTable.class);
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

    public Dao<CourtTable, Integer> getCourtTableDao() throws SQLException {
        if (courtTableDao == null) {
            courtTableDao = getDao(CourtTable.class);
        }
        return courtTableDao;
    }

    public Dao<CourtState, Integer> getCourtStateDao() throws SQLException {
        if (courtStateDao == null) {
            courtStateDao = getDao(CourtState.class);
        }
        return courtStateDao;
    }

    public Dao<CourtDistrict, Integer> getCourtDistrictDao() throws SQLException {
        if (courtDistrictDao == null) {
            courtDistrictDao = getDao(CourtDistrict.class);
        }
        return courtDistrictDao;
    }

    public Dao<CourtSubDistrict, Integer> getCourtSubDistrictDao() throws SQLException {
        if (courtSubDistrictDao == null) {
            courtSubDistrictDao = getDao(CourtSubDistrict.class);
        }
        return courtSubDistrictDao;
    }

    public Dao<BaseClientTable, Integer> getBaseClientTableDao() throws SQLException {
        if (baseClientTableDao == null) {
            baseClientTableDao = getDao(BaseClientTable.class);
        }
        return baseClientTableDao;
    }

    public Dao<ClientTable, Integer> getClientTableDao() throws SQLException {
        if (clientTableDao == null) {
            clientTableDao = getDao(ClientTable.class);
        }
        return clientTableDao;
    }
}
